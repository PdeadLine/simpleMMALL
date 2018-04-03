package com.tmall.service;

import com.tmall.common.ServerResponse;
import com.tmall.pojo.User;

public interface IUserService {
    public ServerResponse<User> login(String username, String password);

    public ServerResponse<String> register(User user);

    public ServerResponse<String> checkValid(String str, String type);
}
