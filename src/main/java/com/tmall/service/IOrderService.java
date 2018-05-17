package com.tmall.service;

import com.github.pagehelper.PageInfo;
import com.tmall.common.ServerResponse;
import com.tmall.vo.OrderVo;

import java.util.Map;

public interface IOrderService {
    public ServerResponse pay(Integer userId, long orderNo, String path);

    public ServerResponse aliCallBack(Map<String, String> params);

    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    public ServerResponse createOrder(Integer userId, Integer shippingId);

    public ServerResponse cancelOrder(Integer userId, Long orderNo);

    public ServerResponse getOrderCartProduct(Integer userId);

    public ServerResponse getOrderDetail(Integer userId, Long orderNo);

    public ServerResponse<PageInfo> getOrderList(Integer userId, Integer pageNum, Integer pageSize);

    public ServerResponse<PageInfo> manageList(int pageNum, int pageSize);
    public ServerResponse<OrderVo> manageDetail(Long orderNo);
}
