package com.liuywei.housekeeping.service.impl;

import com.liuywei.housekeeping.dao.RoleMapper;
import com.liuywei.housekeeping.dao.extend.RoleExtendMapper;
import com.liuywei.housekeeping.domain.Role;
import com.liuywei.housekeeping.domain.RoleExample;
import com.liuywei.housekeeping.domain.extend.RoleExtend;
import com.liuywei.housekeeping.service.IRoleService;
import com.liuywei.housekeeping.utils.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleExtendMapper roleExtendMapper;

    /**
     * 查询所有角色分类
     *
     * @return
     */
    @Override
    public List<Role> findAll() {
        RoleExample roleExample = new RoleExample();
        return roleMapper.selectByExample(roleExample);
    }

    /**
     * 保存或修改
     *
     * @param role
     */
    @Override
    public void saveOrUpdate(Role role) throws CustomException {
        if (role.getId() != null) {
            roleMapper.updateByPrimaryKey(role);
        } else {
            roleMapper.insert(role);
        }
    }

    /**
     * 根据id删除角色分类
     *
     * @param id
     */
    @Override
    public void deleteById(long id) throws CustomException{

        if(roleMapper.selectByPrimaryKey(id) == null){
            throw new CustomException("删除失败，未查询到要删除的数据！");
        }

        roleMapper.deleteByPrimaryKey(id);
    }

    /**
     * 级联用户表查询所有
     *
     * @return
     */
    @Override
    public List<RoleExtend> findAllWithUser() {
        return roleExtendMapper.selectAllWithUser();
    }
}
