package com.tmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tmall.common.ServerResponse;
import com.tmall.dao.ShippingMapper;
import com.tmall.pojo.Shipping;
import com.tmall.service.IShippingService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * create by sintai
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId,Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功", result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    public ServerResponse<String> delete(Integer userId, Integer shippingId) {
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId, shippingId);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    public ServerResponse update(Integer userId,Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    public ServerResponse<Shipping> select(Integer userId,Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if (shipping == null) {
            return ServerResponse.createByErrorMessage("无法查询该地址");
        }
        return ServerResponse.createBySuccess("查询成功", shipping);
    }

    public ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess("查询成功", pageInfo);
    }
}
