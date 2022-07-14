package com.liuywei.housekeeping.domain.extend;

import com.liuywei.housekeeping.domain.Address;
import com.liuywei.housekeeping.domain.User;

public class AddressExtend extends Address {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
