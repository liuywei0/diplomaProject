package com.liuywei.housekeeping.service.impl;

import com.liuywei.housekeeping.dao.ProductMapper;
import com.liuywei.housekeeping.dao.extend.ProductExtendMapper;
import com.liuywei.housekeeping.domain.Product;
import com.liuywei.housekeeping.domain.ProductExample;
import com.liuywei.housekeeping.domain.extend.ProductExtend;
import com.liuywei.housekeeping.service.IProductService;
import com.liuywei.housekeeping.utils.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Resource
    private ProductExtendMapper productExtendMapper;

    @Resource
    private ProductMapper productMapper;

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<Product> findAll() {
        return productMapper.selectByExample(new ProductExample());
    }

    /**
     * 级联查询所有，包括其父类
     *
     * @return
     */
    @Override
    public List<ProductExtend> findAllWithCategory() {
        return productExtendMapper.selectAll();
    }

    /**
     * 通过id删除商品
     *
     * @param id
     */
    @Override
    public void deleteById(long id) throws CustomException {

        if (productMapper.selectByPrimaryKey(id) == null) {
            throw new CustomException("删除失败，未查询到要删除的数据！");
        }

        productMapper.deleteByPrimaryKey(id);
    }


    /**
     * 保存或修改商品信息
     *
     * @param product
     */
    @Override
    public void saveOrUpdate(Product product) throws CustomException {
        if (product.getId() != null) {
            //productMapper.updateByPrimaryKey(product);
            productExtendMapper.updateByProduct(product);
        } else {
            productMapper.insert(product);
        }
    }

}
