package com.liuywei.housekeeping.service;

import com.liuywei.housekeeping.domain.OrderLine;
import com.liuywei.housekeeping.domain.extend.OrderLineExtend;
import com.liuywei.housekeeping.utils.CustomException;

import java.util.List;

public interface IOrderLineService {

    /**
     * 查询所有订单项
     *
     * @return
     */
    List<OrderLine> findAll();

    /**
     * 根据id查询订单项
     *
     * @param id
     * @return
     */
    OrderLine findById(long id);

    /**
     * 保存或修改订单项信息
     *
     * @param orderLine
     */
    void saveOrUpdate(OrderLine orderLine) throws CustomException;

    /**
     * 根据id删除订单项
     *
     * @param id
     */
    void deleteById(long id) throws CustomException;

    /**
     * 通过id查询现有产品信息
     *
     * @param orderId
     * @return
     */
    List<OrderLineExtend> findByOrderId(long orderId);

}
