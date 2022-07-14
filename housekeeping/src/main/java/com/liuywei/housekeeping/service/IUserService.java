package com.liuywei.housekeeping.service;

import com.liuywei.housekeeping.domain.User;
import com.liuywei.housekeeping.domain.extend.UserExtend;
import com.liuywei.housekeeping.utils.CustomException;

import java.util.List;

public interface IUserService {

    /**
     * 查询所有用户信息
     *
     * @return
     */
    List<User> findAll();

    /**
     * 通过id删除用户
     *
     * @param id
     * @return
     */
    void deleteById(long id) throws CustomException;

    /**
     * 保存或修改yonghuxinxi
     *
     * @param user
     * @return
     */
    void saveOrUpdate(User user) throws Exception;

    /**
     * 级联role查询所有用户信息
     *
     * @return
     */
    List<UserExtend> findAllWithRole();

    /**
     * 查询所有员工信息
     *
     * @return
     */
    List<User> findAllEmployee();

    /**
     * 审核通过
     *
     * @param user
     */
    void check(User user);

    /**
     * 审核不通过
     *
     * @param user
     */
    void checkDef(User user);

    /**
     * 登录
     *
     * @param tel
     * @param psw
     * @return
     */
    UserExtend login(String tel, String psw, Long roleId) throws Exception;

    /**
     * 通过手机号查询用户信息
     *
     * @param tel
     * @return
     */
    User findByTel(String tel);

    /**
     * 通过手机号查询用户，包括其角色信息
     *
     * @param tel
     * @return
     */
    UserExtend findByTelWithRole(String tel);

    /**
     * 修改密码
     *
     * @param id
     * @param psw
     */
    void editPsw(long id, String psw, String pass) throws Exception;
}
