package com.tmall.service;

import com.github.pagehelper.PageInfo;
import com.tmall.common.ServerResponse;
import com.tmall.pojo.Product;
import com.tmall.vo.ProductDetialVo;

/**
 * create by sintai
 */

public interface IProductService {
    public ServerResponse<String> saveOrUpdateProduct(Product product);

    public ServerResponse<String> setSaleStauts(Integer productId, Integer status);

    public ServerResponse<ProductDetialVo> manageProductDetial(Integer productId);

    public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

    public ServerResponse<PageInfo> productSearch(String productName, Integer productId, Integer pageNum, Integer pageSize);

    public ServerResponse<ProductDetialVo> getProductDetial(Integer productId);

    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy);
}
