package com.tmall.service.impl;

import com.tmall.common.ServerResponse;
import com.tmall.dao.UserMapper;
import com.tmall.pojo.User;
import com.tmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * create by sintai
 */
public class UserServiceImpl implements IUserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUserName(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //todo 密码登陆MD5

        return null;
    }
}
