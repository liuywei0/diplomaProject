package com.liuywei.housekeeping.service;

import com.liuywei.housekeeping.domain.Privilege;
import com.liuywei.housekeeping.domain.extend.PrivilegeExtend;
import com.liuywei.housekeeping.utils.CustomException;

import java.util.List;

public interface IPrivilegeService {

    /**
     * 查询所有权限信息
     *
     * @return
     */
    List<Privilege> findAll();

    /**
     * 根据id查询权限信息
     *
     * @param id
     * @return
     */
    Privilege findById(long id);

    /**
     * 保存或修改权限信息
     *
     * @param privilege
     */
    void saveOrUpdate(Privilege privilege) throws CustomException;

    /**
     * 根据id删除权限信息
     *
     * @param id
     */
    void deleteById(long id) throws CustomException;

    /**
     * 级联查询所有包括其子类
     *
     * @return
     */
    List<PrivilegeExtend> findWithChildren();

}
