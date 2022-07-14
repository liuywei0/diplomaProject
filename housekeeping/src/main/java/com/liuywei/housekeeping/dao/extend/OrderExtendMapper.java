package com.liuywei.housekeeping.dao.extend;

import com.liuywei.housekeeping.domain.extend.OrderExtend;

import java.util.List;

public interface OrderExtendMapper {

//    List<OrderExtend> selectAll();
//
//    OrderExtend selectById(long id);

    List<OrderExtend> query(Long id, String status, Long customerId, Long employeeId);
}
