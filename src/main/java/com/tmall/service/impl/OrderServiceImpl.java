package com.tmall.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tmall.common.ServerResponse;
import com.tmall.dao.OrderItemMapper;
import com.tmall.dao.OrderMapper;
import com.tmall.pojo.Order;
import com.tmall.pojo.OrderItem;
import com.tmall.service.IOrderService;
import com.tmall.util.BigDecimalUtil;
import com.tmall.util.FTPUtil;
import com.tmall.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * create by sintai
 */
@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    private Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    /**
     * 订单付款
     * @param userId
     * @param orderNo
     * @param path
     * @return
     */
    public ServerResponse pay(Integer userId, long orderNo, String path) {
        //前台约定：返回二维码和订单号
        Map<String,String> resultMap= Maps.newHashMap();
        Order order=orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }
        resultMap.put("orderNo", String.valueOf(order.getOrderNo()));

            // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
            // 需保证商户系统端不能重复，建议通过数据库sequence生成，
            String outTradeNo = order.getOrderNo().toString();

            // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("tmall商城扫码支付，订单：").append(outTradeNo).toString();

            // (必填) 订单总金额，单位为元，不能超过1亿元
            // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
            String totalAmount = order.getPayment().toString();

            // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
            // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
            String undiscountableAmount = "0";

            // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
            // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
            String sellerId = "";

            // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
            String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append(totalAmount).append("元").toString();

            // 商户操作员编号，添加此参数可以为商户操作员做销售统计
            String operatorId = "test_operator_id";

            // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
            String storeId = "test_store_id";

            // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
            ExtendParams extendParams = new ExtendParams();
            extendParams.setSysServiceProviderId("2088100200300400500");

            // 支付超时，定义为120分钟
            String timeoutExpress = "120m";

            // 商品明细列表，需填写购买商品详细信息，
            List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

             List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(userId, orderNo);

            for (OrderItem orderItem : orderItemList) {
                GoodsDetail goods1 = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                        BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(),new Double(100).doubleValue()).longValue(),
                        orderItem.getQuantity());
                goodsDetailList.add(goods1);
            }
            // 创建扫码支付请求builder，设置请求参数
            AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                    .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                    .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                    .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                    .setTimeoutExpress(timeoutExpress)
                    .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                    .setGoodsDetailList(goodsDetailList);

        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

            AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
            switch (result.getTradeStatus()) {
                case SUCCESS:
                    log.info("支付宝预下单成功: )");
                    AlipayTradePrecreateResponse response = result.getResponse();
                    dumpResponse(response);

                    File foler = new File(path);
                    if (!foler.exists()) {
                        foler.setWritable(true);
                        foler.mkdirs();
                    }

                    // 需要修改为运行机器上的路径
                    //细节来了  细节
                    String qrPath = String.format(path+"/qr-%s.png", response.getOutTradeNo());
                    String qrFileName=String.format("qr-%s.png",response.getOutTradeNo());
                    ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                    File targetFile = new File(qrPath, qrFileName);
                    try {
                        FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                    } catch (IOException e) {
                        log.error("上传维码失败",e);
                    }
                    log.info("qrPath:" + qrPath);
                    String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFile.getName();
                    resultMap.put("qrUrl", qrUrl);
                    return ServerResponse.createBySuccessData(resultMap);
                case FAILED:
                    log.error("支付宝预下单失败!!!");
                    return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");

                case UNKNOWN:
                    log.error("系统异常，预下单状态未知!!!");
                    return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");

                default:
                    log.error("不支持的交易状态，交易返回异常!!!");
                    return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
            }

    }
    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
}
