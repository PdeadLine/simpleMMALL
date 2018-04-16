package com.tmall.service.impl;

import com.tmall.common.Const;
import com.tmall.common.ServerResponse;
import com.tmall.dao.CartMapper;
import com.tmall.pojo.Cart;
import com.tmall.service.ICartService;
import com.tmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by sintai
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;

    public ServerResponse add(Integer userId,Integer productId, Integer count) {//加多一个userId，省去了验证用户是否登陆的逻辑
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            //如果为空，说明这个产品没有在购物车里，需要新增一个该产品记录
            Cart cartIteme=new Cart();
            cartIteme.setQuantity(count);
            cartIteme.setChecked(Const.Cart.CHECKED);
            cartIteme.setProductId(productId);
            cartIteme.setUserId(userId);
            cartMapper.insert(cartIteme);
        }else{
            //该产品已存在，则数量增加
            count=cart.getQuantity()+count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return null;
    }

    private CartVo getCartVoLimit(Integer userId) {
        return null;
    }
}
