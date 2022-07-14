package com.liuywei.housekeeping.service;

import com.liuywei.housekeeping.domain.Product;
import com.liuywei.housekeeping.domain.extend.ProductExtend;
import com.liuywei.housekeeping.utils.CustomException;

import java.util.List;

public interface IProductService {

    /**
     * 查询所有
     * @return
     */
    List<Product> findAll();

    /**
     * 级联查询所有，包括其父类
     * @return
     */
    List<ProductExtend> findAllWithCategory();

    /**
     * 通过id删除商品
     * @param id
     */
    void deleteById(long id) throws CustomException;

    /**
     * 保存或修改商品信息
     * @param product
     */
    void saveOrUpdate(Product product) throws CustomException;
}
