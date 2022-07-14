package com.liuywei.housekeeping.service.impl;

import com.liuywei.housekeeping.dao.AccountMapper;
import com.liuywei.housekeeping.dao.OrderLineMapper;
import com.liuywei.housekeeping.dao.OrderMapper;
import com.liuywei.housekeeping.dao.UserMapper;
import com.liuywei.housekeeping.dao.extend.OrderExtendMapper;
import com.liuywei.housekeeping.domain.*;
import com.liuywei.housekeeping.domain.extend.AccountExtend;
import com.liuywei.housekeeping.domain.extend.OrderExtend;
import com.liuywei.housekeeping.service.IOrderService;
import com.liuywei.housekeeping.utils.CustomException;
import com.liuywei.housekeeping.vm.OrderVM;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderLineMapper orderLineMapper;

    @Resource
    private OrderExtendMapper orderExtendMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private AccountMapper accountMapper;

    /**
     * 查询所有订单信息
     *
     * @return
     */
    @Override
    public List<OrderExtend> findAll() {
        //return orderMapper.selectByExample(new OrderExample());
        return orderExtendMapper.query(null, null, null, null);
    }

    /**
     * 根据用户id查询订单信息
     * 用户查询个人订单信息
     *
     * @param customerId
     * @return
     */
    @Override
    public List<OrderExtend> findByCustomerId(long customerId) {

        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andCustomerIdEqualTo(customerId);

        return getOrderExtends(orderExample);
    }

    /**
     * 根据订单时间查询订单信息
     * <p>
     * 时间区间
     *
     * @param starTime
     * @param endTime
     * @return
     */
    @Override
    public List<OrderExtend> findByTime(Long starTime, Long endTime) {

        endTime = endTime == null ? new Date().getTime() : endTime;

        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andOrderTimeBetween(starTime, endTime);


        return getOrderExtends(orderExample);
    }

    /**
     * 通过将查询到的order信息查询order的全部详细信息
     *
     * @param orderExample
     * @return
     */
    private List<OrderExtend> getOrderExtends(OrderExample orderExample) {
        List<Order> order = orderMapper.selectByExample(orderExample);

        List<OrderExtend> orders = new ArrayList<>();
        for (Order order1 : order) {
            orders.add(orderExtendMapper.query(order1.getId(), null, null, null).get(0));
        }
        return orders;
    }

    /**
     * 保存或修改订单信息
     *
     * @param order
     */
    @Override
    public void saveOrUpdate(Order order) throws CustomException {
        if (order.getId() != null) {
            orderMapper.updateByPrimaryKey(order);
        } else {
            orderMapper.insert(order);
        }
    }

    /**
     * 根据id删除订单
     *
     * @param id
     */
    @Override
    public void deleteById(long id) throws CustomException {

        if (orderMapper.selectByPrimaryKey(id) == null) {
            throw new CustomException("删除失败！未查询到要删除的数据");
        }
        orderMapper.deleteByPrimaryKey(id);

    }

    /**
     * 提交订单
     * <p>
     * 加入购物车
     *
     * @param orderVM
     */
    @Override
    public void commit(OrderVM orderVM) throws CustomException {
        /*
        给order实体类赋值
         */
        Order order = new Order();
        order.setAddressId(orderVM.getAddressId());
        order.setCustomerId(orderVM.getCustomerId());
        order.setEmployeeId(orderVM.getEmployeeId());
        order.setStatus(OrderExtend.STATUS_WFK);
        // 计算总价格
        String total = "0";
        for (OrderLine orderLine : orderVM.getOrderLines()) {
            total = new BigDecimal(total).add(new BigDecimal(orderLine.getProductPrice()).multiply(new BigDecimal(orderLine.getNumber()))).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
        }
//        order.setTotal(orderVM.getTotal());
        order.setTotal(total);

        orderMapper.insert(order);

        /*
        给orderLine赋值并插入数据库
         */
        List<OrderLine> orderLines = orderVM.getOrderLines();
        for (OrderLine orderLine : orderLines) {
            orderLine.setOrderId(order.getId());
            orderLineMapper.insert(orderLine);
        }

    }

    /**
     * 通过id查询订单详细信息
     *
     * @param id
     * @return
     */
    @Override
    public OrderExtend findById(long id) {
        List<OrderExtend> list = orderExtendMapper.query(id, null, null, null);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据订单状态查询订单信息
     *
     * @param status
     * @return
     */
    @Override
    public List<OrderExtend> findByStatus(String status) {
        List<OrderExtend> query = orderExtendMapper.query(null, status, null, null);
        Iterator it = query.iterator();

        //查询顾客下单信息，删除员工提现订单信息
        while (it.hasNext()) {
            OrderExtend orderExtend = (OrderExtend) it.next();
            if ("提现".equals(orderExtend.getStatus())) {
                it.remove();
            }
        }
        return query;
    }

    /**
     * 支付订单 => 待派单 (=>如果员工选择服务员工，则=>未服务）
     *
     * @param orderId
     */
    @Override
    public void pay(long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        order.setOrderTime(new Date().getTime());
        if (order.getEmployeeId() != null) {
            // 顾客选择员工
            order.setStatus(OrderExtend.STATUS_WFW);
        } else {
            // 顾客未选择员工，等待公司派单
            order.setStatus(OrderExtend.STATUS_DPD);
        }
        // 更新数据
        orderMapper.updateByPrimaryKey(order);

        // 支付完成，顾客余额则扣除相应价格
        User customer = userMapper.selectByPrimaryKey(order.getCustomerId());
        String balance = new BigDecimal(customer.getBalance()).subtract(new BigDecimal(order.getTotal())).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
        customer.setBalance(balance);
        userMapper.updateByPrimaryKey(customer);

        // 生成相应账户余额明细
        Account account = new Account();
        account.setMoney(order.getTotal());
        account.setDescription("购买产品");
        account.setOrderId(orderId);
        account.setTransactionTime(new Date().getTime());
        account.setUserId(order.getCustomerId());
        account.setType("支付");
        accountMapper.insert(account);
    }

    /**
     * 派单 => 未服务
     *
     * @param orderId
     * @param employeeId
     */
    @Override
    public void sendOrder(long orderId, long employeeId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        // 派单给员工
        order.setEmployeeId(employeeId);
        order.setStatus(OrderExtend.STATUS_WFW);
        // 更新数据
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 员工拒绝接单 => 待派单
     *
     * @param orderId
     */
    @Override
    public void refOrder(long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        // 更改订单状态未待派单
        order.setStatus(OrderExtend.STATUS_DPD);
        // 删除订单中原有员工id
        order.setEmployeeId(null);
        // 更新数据
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 员工服务 => 待评价
     *
     * @param orderId
     */
    @Override
    public void serviceCus(long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        // 更改订单状态
        order.setStatus(OrderExtend.STATUS_DPJ);
        // 更新数据
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 顾客评价=>服务已完成=>平台扣费(5%)=>员工收入
     *
     * @param orderId
     */
    @Override
    public void comments(long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        // 更改订单状态
        order.setStatus(OrderExtend.STATUS_WC);
        // 更新数据
        orderMapper.updateByPrimaryKey(order);

        // 找到公司账户
        UserExample userExample = new UserExample();
        userExample.createCriteria().andTelephoneEqualTo("gongSi");
        User admin = userMapper.selectByExample(userExample).get(0);

        // 找到服务员工信息
        User employee = userMapper.selectByPrimaryKey(order.getEmployeeId());

        // 平台扣除5%的费用
        String pTotal = new BigDecimal(order.getTotal()).multiply(new BigDecimal(0.05)).setScale(4, BigDecimal.ROUND_HALF_UP).toString();

        // 员工可得到的费用
        String eTotal = new BigDecimal(order.getTotal()).subtract(new BigDecimal(pTotal)).setScale(4, BigDecimal.ROUND_HALF_UP).toString();

        Account account = new Account();

        // 生成公司余额明细
        account.setType("收入");
        account.setDescription("扣除员工" + employee.getName() + "的平台费");
        account.setOrderId(orderId);
        account.setUserId(admin.getId());
        account.setMoney(pTotal);
        account.setTransactionTime(new Date().getTime());
        accountMapper.insert(account);

        // 生成员工余额明细
        account.setType("收入");
        account.setDescription("服务费");
        account.setUserId(employee.getId());
        account.setMoney(eTotal);
        accountMapper.insert(account);

        // 平台费用打入公司账户
        if (admin.getBalance() != null) {
            pTotal = new BigDecimal(pTotal).add(new BigDecimal(admin.getBalance())).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
        }
        admin.setBalance(pTotal);
        userMapper.updateByPrimaryKey(admin);

        // 员工服务费打入员工账户
        if (employee.getBalance() != null) {
            eTotal = new BigDecimal(eTotal).add(new BigDecimal(employee.getBalance())).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
        }
        employee.setBalance(eTotal);
        userMapper.updateByPrimaryKey(employee);

    }

    /**
     * 根据订单状态查询某个顾客的订单信息
     *
     * @param status
     * @param customerId
     * @return
     */
    @Override
    public List<OrderExtend> findByCus(String status, Long customerId) {
        return orderExtendMapper.query(null, status, customerId, null);
    }

    /**
     * 员工虽坏家具或是商品价格算错，可申请退款
     * 一旦服务完成，员工评价结束，则不可申请退款
     * <p>
     * msg为退款理由
     *
     * @param id
     * @param money
     * @param msg
     */
    @Override
    public void refund(long id, String money, String msg) {
        Order order = orderMapper.selectByPrimaryKey(id);

        // 生成退款明细
        Account account = new Account();
        account.setMoney(money);
        account.setOrderId(id);
        account.setUserId(order.getCustomerId());
        account.setDescription(msg);
        account.setType("退款");
        accountMapper.insert(account);
    }

    /**
     * 查询员工所有的接单信息
     *
     * @param status
     * @param employeeId
     * @return
     */
    @Override
    public List<OrderExtend> findByEmp(String status, Long employeeId) {
        return orderExtendMapper.query(null, status, null, employeeId);
    }

}
