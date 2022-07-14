package com.liuywei.housekeeping.controller;

import com.liuywei.housekeeping.domain.OrderLine;
import com.liuywei.housekeeping.domain.extend.OrderLineExtend;
import com.liuywei.housekeeping.service.IOrderLineService;
import com.liuywei.housekeeping.utils.Message;
import com.liuywei.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orderLine")
public class OrderLineController {

    @Autowired
    private IOrderLineService orderLineService;

    @GetMapping("findAll")
    public Message findAll() {
        List<OrderLine> orderLines = orderLineService.findAll();
        return Result.success("查询成功！", orderLines);
    }

    @GetMapping("findById")
    public Message findById(long id) {
        OrderLine orderLine = orderLineService.findById(id);
        return Result.success("查询成功！", orderLine);

    }

    @PostMapping("saveOrUpdate")
    public Message saveOrUpdate(OrderLine orderLine) {
        orderLineService.saveOrUpdate(orderLine);
        return Result.success("保存成功！");

    }

    @GetMapping("deleteById")
    public Message deleteById(long id) {
        orderLineService.deleteById(id);
        return Result.success("删除成功！");

    }

    @GetMapping("findByOrderId")
    public Message findByOrderId(long orderId) {
        List<OrderLineExtend> byOrderId = orderLineService.findByOrderId(orderId);
        return Result.success("查询成功！", byOrderId);
    }

}
