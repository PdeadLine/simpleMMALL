package com.tmall.controller.backend;

import com.tmall.common.Const;
import com.tmall.common.ServerResponse;
import com.tmall.pojo.User;
import com.tmall.service.ICategoryService;
import com.tmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.sasl.SaslServer;
import javax.servlet.http.HttpSession;

/**
 * create by sintai
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired private IUserService iUserService;
    @Autowired private ICategoryService iCategoryService;

    /**
     * 添加分类信息
     * @param parentId
     * @param categoryName
     * @param session
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCotegory(@RequestParam(value = "parentId",defaultValue = "0") int parentId,
                                      String categoryName, HttpSession session) {
        //是否为登陆状态
        //先校验是否为管理员用户
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆，请登陆后继续操作");
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {//链式调用
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 修改分类名字
     * @param categoryId
     * @param categoryName
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("set_category_name.do")
    public ServerResponse setCategoryName(Integer categoryId, String categoryName, HttpSession session) {
        //是否为登陆状态
        //先校验是否为管理员用户
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆，请登陆后继续操作");
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {//链式调用
            //业务逻辑处理
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 获取品类子节点(平级)
     * @param session
     * @param categoryId
     * @return
     */
    @ResponseBody
    @RequestMapping(value ="get_category.do" ,method = RequestMethod.POST)
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId) {
        //是否为登陆状态
        //先校验是否为管理员用户
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆，请登陆后继续操作");
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {//链式调用
            //业务逻辑处理
            //查询子节点 的category信息，并且不递归保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @ResponseBody
    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.POST)
    public ServerResponse getDeepCategroy(HttpSession session, Integer categoryId) {
        //是否为登陆状态
        //先校验是否为管理员用户
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆，请登陆后继续操作");
        }
        if (iUserService.checkAdminRoll(user).isSuccess()) {//链式调用
            //业务逻辑处理
            //查询子节点 的category信息，且递归所有子节点信息
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
}
