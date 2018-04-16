package com.tmall.controller.portal;

import com.tmall.common.Const;
import com.tmall.common.ResponseCode;
import com.tmall.common.ServerResponse;
import com.tmall.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * create by sintai
 */
@Controller
@RequestMapping("/cart/")
public class CartController {
    /**
     * 添加到购物车
     * @param session
     * @param productId
     * @param count
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ServerResponse add(HttpSession session,Integer productId,Integer count) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return null;
    }
}
