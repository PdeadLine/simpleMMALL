package com.tmall.service;

import com.tmall.common.ServerResponse;
import com.tmall.pojo.Product;

/**
 * create by sintai
 */

public interface IProductService {
    public ServerResponse<String> saveOrUpdateProduct(Product product);

    public ServerResponse<String> setSaleStauts(Integer productId, Integer status);
}
