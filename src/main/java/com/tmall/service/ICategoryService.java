package com.tmall.service;

import com.tmall.common.ServerResponse;
import com.tmall.pojo.Category;

import java.util.List;

public interface ICategoryService {
    public ServerResponse addCategory(String categoryName, Integer parentId);

    public ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
