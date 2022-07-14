package com.liuywei.housekeeping.dao.extend;

import com.liuywei.housekeeping.domain.extend.OrderLineExtend;

import java.util.List;

public interface OrderLineExtendMapper {

    /**
     * 根据orderId查询
     *
     * @return
     */
    List<OrderLineExtend> selectByOrderId(long orderId);

}
