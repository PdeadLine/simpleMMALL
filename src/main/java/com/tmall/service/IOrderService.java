package com.tmall.service;

import com.tmall.common.ServerResponse;

import java.util.Map;

public interface IOrderService {
    public ServerResponse pay(Integer userId, long orderNo, String path);

    public ServerResponse aliCallBack(Map<String, String> params);

    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    public ServerResponse createOrder(Integer userId, Integer shippingId);
}
