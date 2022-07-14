package com.liuywei.housekeeping.service.impl;

import com.liuywei.housekeeping.dao.RoleMapper;
import com.liuywei.housekeeping.dao.UserMapper;
import com.liuywei.housekeeping.dao.extend.UserExtendMapper;
import com.liuywei.housekeeping.domain.Role;
import com.liuywei.housekeeping.domain.RoleExample;
import com.liuywei.housekeeping.domain.User;
import com.liuywei.housekeeping.domain.UserExample;
import com.liuywei.housekeeping.domain.extend.UserExtend;
import com.liuywei.housekeeping.service.IUserService;
import com.liuywei.housekeeping.utils.CustomException;
import com.liuywei.housekeeping.utils.MD5Tool;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserExtendMapper userExtendMapper;

    @Resource
    private RoleMapper roleMapper;

    /**
     * 查询所有用户信息
     *
     * @return
     */
    @Override
    public List<User> findAll() {
        return userMapper.selectByExample(new UserExample());
    }

    /**
     * 通过id删除用户
     *
     * @param id
     * @return
     */
    @Override
    public void deleteById(long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 注册修改
     *
     * @param user
     * @return
     */
    @Override
    public void saveOrUpdate(User user) throws Exception {

        // 添加时查找是否存在该手机号
        UserExample userExample = new UserExample();
        userExample.createCriteria().andTelephoneEqualTo(user.getTelephone());
        if (userMapper.selectByExample(userExample).size() != 0){
            throw new CustomException("该手机号已注册！");
        }

        user.setPassword(MD5Tool.getMD5(user.getPassword()));
        if (user.getId() != null) {
            userMapper.updateByPrimaryKey(user);
        } else {

            user.setRegisterTime(new Date().getTime());
            user.setBalance("0");
            user.setScore(0);

            // 找到顾客的角色id
            RoleExample roleExample = new RoleExample();
            roleExample.createCriteria().andNameEqualTo("顾客");
            Role role = roleMapper.selectByExample(roleExample).get(0);

            // 将顾客的注册信息的状态改为正常
            if (user.getRoleId() == role.getId()){
                user.setStatus("正常");
            }

            userMapper.insert(user);
        }
    }

    /**
     * 级联role查询所有用户信息
     *
     * @return
     */
    @Override
    public List<UserExtend> findAllWithRole() {
        return userExtendMapper.selectAllWithRole();
    }

    /**
     * 查询所有员工信息
     *
     * @return
     */
    @Override
    public List<User> findAllEmployee() {
        // 员工的角色id为2
        return userExtendMapper.selectByRoleId((long) 2);
    }

    /**
     * 审核通过
     *
     * @param user
     */
    @Override
    public void check(User user) {
        user.setStatus("正常");
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * 审核不通过
     *
     * @param user
     */
    @Override
    public void checkDef(User user) {
        user.setStatus("禁用");
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * 登录
     *
     * @param tel
     * @param psw
     * @return
     */
    @Override
    public UserExtend login(String tel, String psw, Long roleId) throws Exception {

        UserExtend user = userExtendMapper.selectByTel(tel);

        psw = MD5Tool.getMD5(psw);
        if(user.getPassword().equals(psw) && user.getRoleId().equals(roleId)){
            if(user.getStatus().equals("禁用")){
                throw new CustomException("账号状态异常！");
            }
            return user;
        }

        return null;
    }

    /**
     * 通过手机号查询用户信息
     *
     * 注册时可以使用，
     * 下单时也可以使用
     * @param tel
     * @return
     */
    @Override
    public User findByTel(String tel)  {

        User user = userExtendMapper.selectByTel(tel);
        return user;
    }

    /**
     * 通过手机号查询用户，包括其角色信息
     *
     * @param tel
     * @return
     */
    @Override
    public UserExtend findByTelWithRole(String tel)  {
        UserExtend user = userExtendMapper.selectByTel(tel);
        return user;
    }

    /**
     * 修改密码
     *
     * @param id
     * @param psw
     * @param pass
     */
    @Override
    public void editPsw(long id, String psw, String pass) throws Exception {
        User user = userMapper.selectByPrimaryKey(id);
        // 判断原密码是否一致
        if(MD5Tool.getMD5(psw).equals(user.getPassword())){
            user.setPassword(MD5Tool.getMD5(pass));
            userMapper.updateByPrimaryKey(user);
            return;
        }
        // 密码不对
        throw new CustomException("原密码输入错误！");
    }
}
