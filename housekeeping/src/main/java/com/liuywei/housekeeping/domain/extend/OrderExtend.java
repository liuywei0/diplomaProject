package com.liuywei.housekeeping.domain.extend;

import com.liuywei.housekeeping.domain.Address;
import com.liuywei.housekeeping.domain.Order;
import com.liuywei.housekeeping.domain.OrderLine;
import com.liuywei.housekeeping.domain.User;

import java.util.List;

public class OrderExtend extends Order {

    public static final String STATUS_WFK="未付款";
    public static final String STATUS_DPD="待派单";
    public static final String STATUS_WFW="未服务";
    public static final String STATUS_DPJ="待评价";
    public static final String STATUS_WC="已完成";

    private User customer;
    private User employee;
    private Address address;
    private List<OrderLine> orderLines;

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
