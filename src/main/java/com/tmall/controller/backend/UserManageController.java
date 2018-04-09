package com.tmall.controller.backend;

import com.tmall.common.Const;
import com.tmall.common.ServerResponse;
import com.tmall.pojo.User;
import com.tmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * create by sintai
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {
    @Autowired
    private IUserService iUserService;

    /**
     * 后台管理员登陆
     * @param username
     * @param password
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "lgoin.do",method = RequestMethod.POST)
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            if (user.getRole().equals(Const.Role.ROLE_ADMIN)) {
                user.setPassword(StringUtils.EMPTY);
                session.setAttribute(Const.CURRENT_USER,user);
                return ServerResponse.createBySuccess("登陆成功：管理员", user);
            }
            return ServerResponse.createByErrorMessage("非管理员用户，无法登陆，请联系xxxx");
        }
        return response;
    }

    public ServerResponse<User> list(@RequestParam(value = "pageSize",defaultValue = "10") String pageSize,
                                     @RequestParam(value = "pageNum",defaultValue = "1") String pageNum) {
        return null;
    }
}
