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
        return null;
    }
}
