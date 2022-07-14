package com.liuywei.housekeeping.service;

import com.liuywei.housekeeping.domain.RolePrivilege;
import com.liuywei.housekeeping.utils.CustomException;

import java.util.List;

public interface IRolePrivilegeService {

    /**
     * 查询所有
     * @return
     */
    List<RolePrivilege> findAll();

    /**
     * 通过roleId查询
     * @param roleId
     * @return
     */
    List<RolePrivilege> findByRoleId(long roleId);

    /**
     * 通过privilrgrId查询
     * @param privilegeId
     * @return
     */
    List<RolePrivilege> findByPrivilegeId(long privilegeId);

    /**
     * 保存或修改
     * @param rolePrivilege
     */
    void saveOrUpdate(RolePrivilege rolePrivilege) throws CustomException;

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(long id) throws CustomException;

    /**
     * 根据privilegeId删除
     * @param privilegeId
     */
    void deleteByPrivilegeId(long privilegeId) throws CustomException;

    /**
     * 通过roleId删除
     * @param roleId
     */
    void deleteByRoleId(long roleId) throws CustomException;

}
