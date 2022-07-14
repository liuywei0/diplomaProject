package com.liuywei.housekeeping.service;

import com.liuywei.housekeeping.domain.Category;
import com.liuywei.housekeeping.domain.extend.CategoryExtend;
import com.liuywei.housekeeping.utils.CustomException;

import java.util.List;

public interface ICategoryService {

    /**
     * 级联查询所有
     *
     * @return
     */
    List<CategoryExtend> findAllWithChildren();

    /**
     * 查询所有
     *
     * @return
     */
    List<Category> findAll();

    /**
     * 根据id删除栏目信息
     *
     * @param id
     */
    void deleteById(long id) throws CustomException;

    /**
     * 保存或修改栏目信息
     *
     * @param category
     */
    void saveOrUpdate(Category category) throws CustomException;

    /**
     * 通过名称查询商品
     * 模糊查询
     *
     * @param name
     * @return
     */
    List<Category> findByName(String name);
}
