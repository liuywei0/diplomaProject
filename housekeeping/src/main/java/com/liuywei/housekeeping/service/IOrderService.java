package com.liuywei.housekeeping.service;

import com.liuywei.housekeeping.domain.Account;
import com.liuywei.housekeeping.domain.Order;
import com.liuywei.housekeeping.domain.extend.AccountExtend;
import com.liuywei.housekeeping.domain.extend.OrderExtend;
import com.liuywei.housekeeping.utils.CustomException;
import com.liuywei.housekeeping.vm.OrderVM;

import java.util.List;

public interface IOrderService {

    /**
     * 查询所有订单信息
     *
     * @return
     */
    List<OrderExtend> findAll();

    /**
     * 根据用户id查询订单信息
     * 用户查询个人订单信息
     *
     * @param customerId
     * @return
     */
    List<OrderExtend> findByCustomerId(long customerId);

    /**
     * 根据订单时间查询订单信息
     *
     * @param starTime
     * @param endTime
     * @return
     */
    List<OrderExtend> findByTime(Long starTime, Long endTime);

    /**
     * 保存或修改订单信息
     *
     * @param order
     */
    void saveOrUpdate(Order order) throws CustomException;

    /**
     * 根据id删除订单
     *
     * @param id
     */
    void deleteById(long id) throws CustomException;

    /**
     * 提交订单
     */
    void commit(OrderVM orderVM) throws CustomException;

    /**
     * 通过id查询订单详细信息
     *
     * @param id
     * @return
     */
    OrderExtend findById(long id);

    /**
     * 根据订单状态查询订单信息
     *
     * @param status
     * @return
     */
    List<OrderExtend> findByStatus(String status);

    /**
     * 支付订单
     *
     * @param orderId
     */
    void pay(long orderId);

    /**
     * 派单
     *
     * @param orderId
     */
    void sendOrder(long orderId, long employeeId);

    /**
     * 员工拒绝接单
     *
     * @param orderId
     */
    void refOrder(long orderId);

    /**
     * 员工服务
     *
     * @param orderId
     */
    void serviceCus(long orderId);

    /**
     * 顾客评价
     *
     * @param orderId
     */
    void comments(long orderId);

    /**
     * 根据订单状态查询某个顾客的订单信息
     *
     * @param status
     * @return
     */
    List<OrderExtend> findByCus(String status, Long customerId);

    /**
     * 员工虽坏家具或是商品价格算错，可申请退款
     *
     * @param id
     * @param money
     * @param msg
     */
    void refund(long id, String money, String msg);

    /**
     * 查询员工所有的接单信息
     *
     * @param status
     * @return
     */
    List<OrderExtend> findByEmp(String status, Long employeeId);
}
