package com.tmall.controller.portal;

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
    /**
     * 用户登陆
     * @param username
     * @param Password
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    public Object login(String username, String Password, HttpSession session){
       //service

        return null;
    }
}
