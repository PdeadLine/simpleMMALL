package com.tmall.dao;

import com.tmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(User record);
    int insertSelective(User record);
    User selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(User record);
    int updateByPrimaryKey(User record);
    int checkUserEmail(String email);
    int checkUserName(String username);
    User selectLogin(@Param("username") String username, @Param("password") String password);
    String selectQuetionByUsername(String username);
    int checkAnswer(@Param("username") String username,@Param("question") String question, @Param("answer")String answer);

    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    int checkUserPassword(@Param("password") String password, @Param("userId") Integer userId);

    int checkEmailByUserId(@Param("email") String email,@Param("userId") Integer userId);
}