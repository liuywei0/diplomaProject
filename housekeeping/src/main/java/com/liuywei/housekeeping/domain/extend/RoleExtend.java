package com.liuywei.housekeeping.domain.extend;

import com.liuywei.housekeeping.domain.Role;
import com.liuywei.housekeeping.domain.User;

import java.util.List;

public class RoleExtend extends Role {

    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
