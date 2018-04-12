package com.tmall.controller.backend;

import com.tmall.common.Const;
import com.tmall.common.ResponseCode;
import com.tmall.common.ServerResponse;
import com.tmall.pojo.Product;
import com.tmall.pojo.User;
import com.tmall.service.IProductService;
import com.tmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * create by sintai
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;

    /**
     * 新增产品或更新产品
     * @param session
     * @param product
     * @return
     */
    @ResponseBody
    @RequestMapping("save.do")
    public ServerResponse sava(HttpSession session,Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            return iProductService.saveOrUpdateProduct(product);
        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }

    /**
     * 设置商品上下架状态
     * @param productId
     * @param status
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("set_sale_status.do")
    public ServerResponse setSaleStatus(Integer productId,Integer status,HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            //业务逻辑处理
            return iProductService.setSaleStauts(productId,status);
        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }

    /**
     * 产品list
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("list.do")
    public ServerResponse getProductList(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                               HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            //业务逻辑处理
            return iProductService.getProductList(pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }

    @ResponseBody
    @RequestMapping("detial.do")
    public ServerResponse getDetial(Integer productId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            //业务逻辑处理

            return iProductService.manageProductDetial(productId);
        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(String productName,
                                 Integer productId,
                                 @RequestParam(value ="pageNum",defaultValue = "1")
                                         Integer pageNum,
                                 @RequestParam(value = "pageSize",defaultValue = "10")
                                 Integer pageSize,
                                 HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {
            //业务逻辑处理
            return iProductService.productSearch(productName,productId,pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("非管理员用户，无权限操作");
    }

    public ServerResponse upload(MultipartFile file, HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        return null;
    }

}
