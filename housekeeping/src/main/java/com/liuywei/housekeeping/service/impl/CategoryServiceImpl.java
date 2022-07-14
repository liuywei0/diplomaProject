package com.liuywei.housekeeping.service.impl;

import com.liuywei.housekeeping.dao.CategoryMapper;
import com.liuywei.housekeeping.dao.extend.CategoryExtendMapper;
import com.liuywei.housekeeping.domain.Category;
import com.liuywei.housekeeping.domain.CategoryExample;
import com.liuywei.housekeeping.domain.extend.CategoryExtend;
import com.liuywei.housekeeping.service.ICategoryService;
import com.liuywei.housekeeping.utils.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Resource
    private CategoryExtendMapper categoryExtendMapper;

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 级联查询所有
     *
     * @return
     */
    @Override
    public List<CategoryExtend> findAllWithChildren() {
        return categoryExtendMapper.selectAll();
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<Category> findAll() {
        return categoryMapper.selectByExample(new CategoryExample());
    }

    /**
     * 根据id删除栏目信息
     *
     * @param id
     */
    @Override
    public void deleteById(long id) throws CustomException {
//         判断id是否存在，存在->删除，不存在->报错
        Category category = categoryMapper.selectByPrimaryKey(id);
        if (category == null) {
            throw new CustomException("删除失败，未查询到要删除的数据");
        }
        categoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 保存或修改栏目信息
     *
     * @param category
     */
    @Override
    public void saveOrUpdate(Category category) throws CustomException{
        if (category.getId() != null) {
            categoryMapper.updateByPrimaryKey(category);
        } else {
            categoryMapper.insert(category);
        }
    }

    /**
     * 通过名称查询商品
     * 模糊查询
     *
     * @param name
     * @return
     */
    @Override
    public List<Category> findByName(String name) {

        CategoryExample categoryExample = new CategoryExample();
        categoryExample.createCriteria().andNameLike(name);

        return categoryMapper.selectByExample(categoryExample);
    }
}
