package com.tmall.dao;

import com.tmall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
    //多参数，就要使用@param注解
    Order selectByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNod);

    Order selectByOrderNo(Long orderNo);

    List<Order> selectByUserId(@Param("userId") Integer userId);

    List<Order> selectAllOrder();
}