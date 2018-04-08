package com.tmall.controller.portal;

import com.tmall.common.Const;
import com.tmall.common.ResponseCode;
import com.tmall.common.ServerResponse;
import com.tmall.pojo.User;
import com.tmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * create by sintai
 */
@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired private IUserService iUserService;
    /**
     * 用户登陆
     * @param username
     * @param password
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(username, password);
        //如果登陆成功保存到session中
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 注销登陆
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value="logout.do",method = RequestMethod.POST)
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 检查用户名是否有效
     * @param str
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str,type);
    }

    /**
     * 获取登陆用户信息
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccessData(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
    }

    /**
     * 忘记密码[申请]
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    /**
     * 提交问题答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        System.out.println(username+"usr"+question+"question"+answer+"answer");
        return iUserService.checkAnswer(username,question,answer);
    }

    /**
     * 忘记密码的重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    /**
     * 登陆状态中修改密码
     * @param passwordOld
     * @param passwordNew
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    /**
     * 登录状态更新个人信息
     * @param session
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    public ServerResponse<User> updataInformation(HttpSession session, User user) {
       //判断是否登陆状态
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updataInformation(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    @ResponseBody
    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    public ServerResponse<User> getInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登陆，需要强制登陆status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }

}

