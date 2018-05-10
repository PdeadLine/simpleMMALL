package com.tmall.service;

import com.tmall.common.ServerResponse;

public interface IOrderService {
    public ServerResponse pay(Integer userId, long orderNo, String path);
}
