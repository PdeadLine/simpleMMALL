package com.tmall.service;

import com.tmall.common.ServerResponse;
import com.tmall.vo.CartVo;

/**
 * create by sintai
 */

public interface ICartService {
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    public ServerResponse<CartVo> updateCart(Integer userId, Integer productId, Integer count);

    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);
}
