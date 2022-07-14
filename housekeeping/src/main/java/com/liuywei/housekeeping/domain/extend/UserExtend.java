package com.liuywei.housekeeping.domain.extend;

import com.liuywei.housekeeping.domain.Role;
import com.liuywei.housekeeping.domain.User;

public class UserExtend extends User {

    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
