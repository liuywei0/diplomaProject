package com.liuywei.housekeeping.domain.extend;

import com.liuywei.housekeeping.domain.Comment;
import com.liuywei.housekeeping.domain.User;

import java.util.List;

public class CommentExtend extends Comment {

    private User customer;
    private User employee;
    private List<Comment> children;

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

    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }
}
