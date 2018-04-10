package com.tmall.service.impl;

import com.tmall.common.ResponseCode;
import com.tmall.common.ServerResponse;
import com.tmall.dao.ProductMapper;
import com.tmall.pojo.Product;
import com.tmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by sintai
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 新增或更新产品
     * @param product
     * @return
     */
    public ServerResponse<String> saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray=product.getMainImage().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("更新产品信息成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");
            }else{
                int rowCount=productMapper.insert(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("添加产品信息成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");
            }
        }
        return ServerResponse.createByErrorMessage("更新或新增产品参数错误!");
    }

    public ServerResponse<String> setSaleStauts(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("设置上架或下架成功");
        }
        return ServerResponse.createByErrorMessage("上架或下架商品失败");
    }

    public ServerResponse<Object> manageProductDetial(Integer productId) {
        if (productId==null){
            ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        return null;
    }
}
