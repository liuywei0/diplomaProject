package com.liuywei.housekeeping.dao.extend;

import com.liuywei.housekeeping.domain.User;
import com.liuywei.housekeeping.domain.extend.UserExtend;

import java.util.List;

public interface UserExtendMapper {

    List<UserExtend> selectAllWithRole();

    List<User> selectByRoleId(long id);

    UserExtend selectByTel(String tel);
}
