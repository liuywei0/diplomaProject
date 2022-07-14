package com.liuywei.housekeeping.dao.extend;

import com.liuywei.housekeeping.domain.Privilege;
import com.liuywei.housekeeping.domain.extend.PrivilegeExtend;

import java.util.List;

public interface PrivilegeExtendMapper {

    /**
     * 级联查询所有包括其子类
     *
     * @return
     */
    List<PrivilegeExtend> selectWithChildren();

    /**
     * 查询子类
     *
     * @return
     */
    List<Privilege> selectChildren(long id);

}
