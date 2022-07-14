package com.liuywei.housekeeping.domain.extend;

import com.liuywei.housekeeping.domain.Account;
import com.liuywei.housekeeping.domain.Order;
import com.liuywei.housekeeping.domain.User;

public class AccountExtend extends Account {

    private User user;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    private Order order;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
