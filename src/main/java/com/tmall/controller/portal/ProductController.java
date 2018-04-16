package com.tmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.tmall.common.ServerResponse;
import com.tmall.service.IProductService;
import com.tmall.vo.ProductDetialVo;
import com.tmall.vo.ProductListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * create by sintai
 */

@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    /**
     * 产品detail
     * @param productId
     * @return
     */
    @ResponseBody
    @RequestMapping("detial.do")
    public ServerResponse<ProductDetialVo> getProducctDetial(Integer productId) {
        return iProductService.getProductDetial(productId);
    }

    /**
     * 产品搜索及动态排序List
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @ResponseBody
    @RequestMapping("list.do")
    public ServerResponse<PageInfo> list(@RequestParam(value ="keyword" ,required = false) String keyword,
                                                        @RequestParam(value ="categoryId" ,required = false)Integer categoryId,
                                                        @RequestParam(value ="pageNum" ,defaultValue = "1")Integer pageNum,
                                                        @RequestParam(value ="pageSize" ,defaultValue ="10")Integer pageSize,
                                                        @RequestParam(value ="orderBy" ,defaultValue ="")String orderBy) {//添加动态排序参数
        return iProductService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }
}
