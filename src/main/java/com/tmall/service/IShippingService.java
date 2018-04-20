package com.tmall.service;

import com.github.pagehelper.PageInfo;
import com.tmall.common.ServerResponse;
import com.tmall.pojo.Shipping;

public interface IShippingService {
    public ServerResponse add(Integer userId, Shipping shipping);

    public ServerResponse<String> delete(Integer userId, Integer shippingId);

    public ServerResponse update(Integer userId, Shipping shipping);

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    public ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);
}
