package com.liuywei.housekeeping.vm;

import com.liuywei.housekeeping.domain.Order;
import com.liuywei.housekeeping.domain.OrderLine;

import java.util.List;

public class OrderVM extends Order {

    private List<OrderLine> orderLines;

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
}
