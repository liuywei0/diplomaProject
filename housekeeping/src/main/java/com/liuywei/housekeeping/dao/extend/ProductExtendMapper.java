package com.liuywei.housekeeping.dao.extend;

import com.liuywei.housekeeping.domain.Product;
import com.liuywei.housekeeping.domain.extend.ProductExtend;

import java.util.List;

public interface ProductExtendMapper {

    /**
     * 级联查询所有，包括其父类信息
     * @return
     */
    List<ProductExtend> selectAll();

    void updateByProduct(Product product);
}
