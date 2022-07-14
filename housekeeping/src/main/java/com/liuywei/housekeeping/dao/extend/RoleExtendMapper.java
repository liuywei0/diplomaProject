package com.liuywei.housekeeping.dao.extend;

import com.liuywei.housekeeping.domain.Role;
import com.liuywei.housekeeping.domain.extend.RoleExtend;

import java.util.List;

public interface RoleExtendMapper {

    List<Role> selectById(long id);

    List<RoleExtend> selectAllWithUser();

}
