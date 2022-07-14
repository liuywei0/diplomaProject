package com.liuywei.housekeeping.dao;

import com.liuywei.housekeeping.domain.OrderLine;
import com.liuywei.housekeeping.domain.OrderLineExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderLineMapper {
    long countByExample(OrderLineExample example);

    int deleteByExample(OrderLineExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OrderLine record);

    int insertSelective(OrderLine record);

    List<OrderLine> selectByExample(OrderLineExample example);

    OrderLine selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OrderLine record, @Param("example") OrderLineExample example);

    int updateByExample(@Param("record") OrderLine record, @Param("example") OrderLineExample example);

    int updateByPrimaryKeySelective(OrderLine record);

    int updateByPrimaryKey(OrderLine record);
}