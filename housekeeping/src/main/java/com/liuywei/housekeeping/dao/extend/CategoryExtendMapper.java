package com.liuywei.housekeeping.dao.extend;

import com.liuywei.housekeeping.domain.Category;
import com.liuywei.housekeeping.domain.extend.CategoryExtend;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface CategoryExtendMapper {

    /**
     * 级联查询所有
     * @return
     */
    List<CategoryExtend> selectAll();

    /**
     * 查询子数据
     * id:parent_id
     * @return
     */
    List<Category> selectChildren(long id);
}
