package com.tmall.service.impl;

import com.google.common.collect.Maps;
import com.tmall.common.ServerResponse;
import com.tmall.dao.OrderMapper;
import com.tmall.pojo.Order;
import com.tmall.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * create by sintai
 */
@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    public ServerResponse pay(Integer userId, long orderNo, String path) {
        //前台约定：返回二维码和订单号
        Map<String,String> resultMap= Maps.newHashMap();
        Order order=orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }
        resultMap.put("orderNo", String.valueOf(order.getOrderNo()));
       //todo 生成支付宝订单！！！
        return null;
    }
}
