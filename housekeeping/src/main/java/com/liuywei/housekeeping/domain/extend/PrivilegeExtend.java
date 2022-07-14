package com.liuywei.housekeeping.domain.extend;

import com.liuywei.housekeeping.domain.Privilege;

import java.util.List;

public class PrivilegeExtend extends Privilege {

    private List<Privilege> privileges;

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }
}
