package com.tmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.tmall.common.Const;
import com.tmall.common.ResponseCode;
import com.tmall.common.ServerResponse;
import com.tmall.pojo.User;
import com.tmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * create by sintai
 */
@Controller
@RequestMapping("/order/")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;


    @ResponseBody
    @RequestMapping("create.do")
    public ServerResponse createOrder(HttpSession session,Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.createOrder(user.getId(), shippingId);
    }
    @ResponseBody
    @RequestMapping("cancel.do")
    public ServerResponse cancelOrder(HttpSession session,Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancelOrder(user.getId(), orderNo);
    }
    @ResponseBody
    @RequestMapping("get_order_cart_product.do")
    public ServerResponse getOrderCartProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }
    @ResponseBody
    @RequestMapping("detial.do")
    public ServerResponse detial(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        //todo 业务实现
        return iOrderService.x;
    }























    /**
     *  支付宝付款
     * @param session
     * @param orderNo
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("pay.do")
    public ServerResponse pay(HttpSession session, long orderNo, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(user.getId(),orderNo,path);
    }

    @ResponseBody
    @RequestMapping("alipay_callback.do")
    public Object alipayCallBack(HttpServletRequest request) {  //支付宝的回调所有的数据都放在request中
        Map<String,String> params= Maps.newHashMap();   //取得request中的数据，拼接，放到map中


        Map requestParams=request.getParameterMap();
        //这里使用迭代器iterator  --技巧---map的迭代
        for (Iterator iter=requestParams.keySet().iterator();iter.hasNext();) {
            String name =(String)iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                //技巧又来了---简化了很多代码
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i];
            }
            params.put(name, valueStr);
        }
        logger.info("支付宝回调，sign:{},trade_status：{}，参数：{}",params.get("sign"),params.get("trade_status"),params.toString());
        //非常重要，验证回调的正确性，是不是支付宝发的，并且还要避免重复通知。
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return ServerResponse.createByErrorMessage("非法请求，验证未通过，再恶意请求就关闭你的电脑！！！");
            }
        } catch (AlipayApiException e) {
           logger.error("支付宝验证回调异常",e);
        }
        //todo 验证各种数据
        ServerResponse serverResponse = iOrderService.aliCallBack(params);
        if (serverResponse.isSuccess()) {
            return Const.AlipayCallBack.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallBack.RESPONSE_FAILED;
    }

    /**
     * 查询订单状态
     * @param session
     * @param orderNo
     * @return
     */
    @ResponseBody
    @RequestMapping("query_order_pay_status.do")
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createBySuccessData(true);
        }
        return ServerResponse.createBySuccessData(false);
    }

}
