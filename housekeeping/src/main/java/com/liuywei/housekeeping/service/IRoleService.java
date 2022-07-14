package com.liuywei.housekeeping.service;

import com.liuywei.housekeeping.domain.Role;
import com.liuywei.housekeeping.domain.extend.RoleExtend;
import com.liuywei.housekeeping.utils.CustomException;

import java.util.List;

public interface IRoleService {

    /**
     * 查询所有角色分类
     *
     * @return
     */
    List<Role> findAll();

    /**
     * 保存或修改
     *
     * @param role
     */
    void saveOrUpdate(Role role) throws CustomException;

    /**
     * 根据id删除角色分类
     *
     * @param id
     */
    void deleteById(long id) throws CustomException;

    /**
     * 级联用户表查询所有
     *
     * @return
     */
    List<RoleExtend> findAllWithUser();
}
