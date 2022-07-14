package com.liuywei.housekeeping.controller;

import com.liuywei.housekeeping.domain.Comment;
import com.liuywei.housekeeping.domain.Order;
import com.liuywei.housekeeping.domain.extend.OrderExtend;
import com.liuywei.housekeeping.service.ICommentService;
import com.liuywei.housekeeping.service.IOrderService;
import com.liuywei.housekeeping.utils.Message;
import com.liuywei.housekeeping.utils.Result;
import com.liuywei.housekeeping.vm.OrderVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICommentService commentService;

    @GetMapping("findAll")
    public Message findAll() {
        List<OrderExtend> orders = orderService.findAll();
        return Result.success("查询成功！", orders);
    }

    @GetMapping("findByStatus")
    public Message findByStatus(String status) {
        List<OrderExtend> orders = orderService.findByStatus(status);
        return Result.success("查询成功！", orders);
    }

    @GetMapping("findByCus")
    public Message findByCus(String status, Long customerId) {
        List<OrderExtend> orders = orderService.findByCus(status, customerId);
        return Result.success("查询成功！", orders);
    }

    @GetMapping("findByEmp")
    public Message findByEmp(String status, Long employeeId) {
        List<OrderExtend> orders = orderService.findByEmp(status, employeeId);
        return Result.success("查询成功！", orders);
    }

    @GetMapping("findByCustomerId")
    public Message findByCustomerId(long customerId) {
        List<OrderExtend> orders = orderService.findByCustomerId(customerId);
        return Result.success("查询成功！", orders);
    }

    @GetMapping("findByTime")
    public Message findByTime(Long starTime, Long endTime) {
        List<OrderExtend> orders = orderService.findByTime(starTime, endTime);
        return Result.success("查询成功！", orders);
    }

    @PostMapping("saveOrUpdate")
    public Message saveOrUpdate(Order order) {
        orderService.saveOrUpdate(order);
        return Result.success("保存成功！");
    }

    /**
     * 退款
     *
     * @param id
     * @param money
     * @return
     */
    @GetMapping("refund")
    public Message refund(long id, String money, String msg) {
        orderService.refund(id, money, msg);
        return Result.success("保存成功！");
    }

    @GetMapping("deleteById")
    public Message deleteById(long id) {
        orderService.deleteById(id);
        return Result.success("删除成功！");
    }

    @PostMapping("commit")
    public Message commit(@RequestBody OrderVM orderVM) {
        orderService.commit(orderVM);
        return Result.success("提交成功！");
    }

    @GetMapping("findById")
    public Message findById(long id) {
        OrderExtend orderExtend = orderService.findById(id);
        return Result.success("查询成功！", orderExtend);
    }

    @GetMapping("pay")
    public Message pay(long id) {
        orderService.pay(id);
        return Result.success("支付成功！");
    }

    @GetMapping("sendOrder")
    public Message sendOrder(long id, long employeeId) {
        orderService.sendOrder(id, employeeId);
        return Result.success("派单成功！");
    }

    @GetMapping("refOrder")
    public Message refOrder(long id) {
        orderService.refOrder(id);
        return Result.success("员工拒绝服务！");
    }

    @GetMapping("serviceCus")
    public Message serviceCus(long id) {
        orderService.serviceCus(id);
        return Result.success("已完成服务！");
    }

    @PostMapping("comments")
    public Message comments(long id, long userId, String content, int score) {
        // 更改订单状态
        orderService.comments(id);
        // 用户评价
        commentService.comments(id, userId, content, score);
        return Result.success("顾客已评价！");
    }

}
