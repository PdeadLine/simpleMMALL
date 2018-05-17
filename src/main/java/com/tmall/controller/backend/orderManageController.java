package com.tmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.tmall.common.Const;
import com.tmall.common.ResponseCode;
import com.tmall.common.ServerResponse;
import com.tmall.pojo.User;
import com.tmall.service.IOrderService;
import com.tmall.service.IUserService;
import com.tmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * create by sintai
 */
@Controller
@RequestMapping("/manage/order")
public class orderManageController {
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IUserService iUserService;

    @ResponseBody
    @RequestMapping("list.do")
    public ServerResponse<PageInfo> getOrderList(HttpSession session,
                                                 @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                                 @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            //业务逻辑
            return iOrderService.manageList(pageNum, pageSize);

        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }

    @ResponseBody
    @RequestMapping("detail.do")
    public ServerResponse<OrderVo> getOrderDetail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            //业务逻辑
            return iOrderService.manageDetail(orderNo);

        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }

    @ResponseBody
    @RequestMapping("search.do")
    public ServerResponse<PageInfo> orderSearch(HttpSession session, Long orderNo,
                                               @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            //业务逻辑
            return iOrderService.manageSearch(orderNo,pageNum,pageSize);

        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }

    @ResponseBody
    @RequestMapping("send_goods.do")
    public ServerResponse<String> orderSendGoods(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            //业务逻辑
            return iOrderService.manageSendGoods(orderNo);

        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }
}
