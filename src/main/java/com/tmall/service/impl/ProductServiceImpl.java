package com.tmall.service.impl;

import com.tmall.common.ResponseCode;
import com.tmall.common.ServerResponse;
import com.tmall.dao.CategoryMapper;
import com.tmall.dao.ProductMapper;
import com.tmall.pojo.Category;
import com.tmall.pojo.Product;
import com.tmall.service.IProductService;
import com.tmall.util.DateTimeUtill;
import com.tmall.util.PropertiesUtil;
import com.tmall.vo.ProductDetialVo;
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

    @Autowired
    private CategoryMapper categoryMapper;
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

    public ServerResponse<ProductDetialVo> manageProductDetial(Integer productId) {
        if (productId==null){
            ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架或已删除");
        }
        //VO对象
        ProductDetialVo productDetialVo = assembleProductDetialVo(product);
        return ServerResponse.createBySuccessData(productDetialVo);
    }

    /**
     * 装配ProductDetialVo;
     * @param product
     * @return
     */
    private ProductDetialVo assembleProductDetialVo(Product product) {
        ProductDetialVo productDetialVo = new ProductDetialVo();
        productDetialVo.setId(product.getId());
        productDetialVo.setName(product.getName());
        productDetialVo.setSubTitle(product.getSubtitle());
        productDetialVo.setMainImage(product.getMainImage());
        productDetialVo.setSubImages(product.getSubImages());
        productDetialVo.setDetial(product.getDetail());
        productDetialVo.setPrice(product.getPrice());
        productDetialVo.setStock(product.getStock());
        productDetialVo.setStatus(product.getStatus());

        //imageHost;parentCategoryId;createTime;updateTime;
        productDetialVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://192.168.10.171/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetialVo.setCategoryParentId(0);
        }else {
            productDetialVo.setCategoryParentId(category.getParentId());
        }
        productDetialVo.setCreateTime(DateTimeUtill.dateToString(product.getCreateTime()));
        productDetialVo.setUpdateTime(DateTimeUtill.dateToString(product.getUpdateTime()));
        return productDetialVo;
    }
}
