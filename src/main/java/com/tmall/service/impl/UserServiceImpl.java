package com.tmall.service.impl;

import com.tmall.common.Const;
import com.tmall.common.ResponseCode;
import com.tmall.common.ServerResponse;
import com.tmall.common.TokenCache;
import com.tmall.dao.UserMapper;
import com.tmall.pojo.User;
import com.tmall.service.IUserService;
import com.tmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * create by sintai
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    UserMapper userMapper;//要把spring 自动注入扫描设为警告级别，不然会出错。
    @Override
    public ServerResponse<User> login(String username, String password) {
//        int resultCount = userMapper.checkUsername(username);
//        if (resultCount == 0) {
//            return ServerResponse.createByErrorMessage("用户名不存在");
//        }
        ServerResponse response=this.checkValid(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功",user);
    }

    public ServerResponse<String> register(User user) {
        //简化重复代码
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);//添加角色信息
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount=userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

//    public ServerResponse<String> checkValid(String str, String type) {
//        if (StringUtils.isNotBlank(type)) {
//            //校验
//            if (Const.USERNAME.equals(xxx)) {
//                int resultCount = userMapper.checkUserName(str);
//                if (resultCount > 0) {
//                    return ServerResponse.createByErrorMessage("用户名已存在");
//                }
//            }
//            if (Const.EMAIL.equals(xxxx)) {
//                int resultCount = userMapper.checkUserEmail(str);
//                if (resultCount > 0) {
//                    return ServerResponse.createByErrorMessage("邮箱已注册");
//                }
//            }
//        }else{
//            return ServerResponse.createByErrorMessage("参数错误！");
//        }
//        return ServerResponse.createBySuccessMessage("校验成功");
//    }

    public ServerResponse<String> checkValid(String str,String type){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuetionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccessData(question);
        }
        return ServerResponse.createByErrorMessage("找回密码问题未设置");
    }

    public ServerResponse<String> checkAnswer(String username,String question,String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            //说明问题及问题答案正确
            String forgetToken = UUID.randomUUID().toString();
            //本地缓存token
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccessData(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案是错误的");
    }

   public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
    if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
        return ServerResponse.createByErrorMessage("参数错误,token需要传递");
    }
    ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
    if(validResponse.isSuccess()){
        //用户不存在
        return ServerResponse.createByErrorMessage("用户不存在");
    }
    String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
    if(org.apache.commons.lang3.StringUtils.isBlank(token)){
        return ServerResponse.createByErrorMessage("token无效或者过期");
    }

    if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
        String md5Password  = MD5Util.MD5EncodeUtf8(passwordNew);
        int rowCount = userMapper.updatePasswordByUsername(username,md5Password);

        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }
    }else{
        return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");
    }
    return ServerResponse.createByErrorMessage("修改密码失败");
}


    public ServerResponse<String> resetPassword(String passwordOld,String passowrdNew,User user) {
        //防止横向越权,校验一下这个用户的旧 密码，一定要指定这个用户。
        int resultCount=userMapper.checkUserPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount==0) {
            return ServerResponse.createByErrorMessage("旧密码输入错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passowrdNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码修改成功");
        }
        return ServerResponse.createByErrorMessage("密码修改失败");
    }

    public ServerResponse<User> updataInformation(User user){
        //username是不能被更新的
        //email也要进行一个校验，新设的email是否已经存在，如果相同就不能被使用
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if(resultCount > 0){
            return ServerResponse.createByErrorMessage("邮箱已存在");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updataCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updataCount>0){
            return ServerResponse.createBySuccess("更新信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新信息失败");
    }

    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccessData(user);
    }

    //校验是否管理员
    public ServerResponse checkAdminRoll(User user) {
        if (user!=null&&user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
