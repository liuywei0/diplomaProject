package com.liuywei.housekeeping.service.impl;

import com.liuywei.housekeeping.dao.RolePrivilegeMapper;
import com.liuywei.housekeeping.domain.RolePrivilege;
import com.liuywei.housekeeping.domain.RolePrivilegeExample;
import com.liuywei.housekeeping.service.IRolePrivilegeService;
import com.liuywei.housekeeping.utils.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RolePrivilegeServiceImpl implements IRolePrivilegeService {

    @Resource
    private RolePrivilegeMapper rolePrivilegeMapper;

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<RolePrivilege> findAll() {
        return rolePrivilegeMapper.selectByExample(new RolePrivilegeExample());
    }

    /**
     * 通过roleId查询
     *
     * @param roleId
     * @return
     */
    @Override
    public List<RolePrivilege> findByRoleId(long roleId) {

        RolePrivilegeExample rolePrivilegeExample = new RolePrivilegeExample();
        rolePrivilegeExample.createCriteria().andRoleIdEqualTo(roleId);

        return rolePrivilegeMapper.selectByExample(rolePrivilegeExample);
    }

    /**
     * 通过privilrgrId查询
     *
     * @param privilegeId
     * @return
     */
    @Override
    public List<RolePrivilege> findByPrivilegeId(long privilegeId) {

        RolePrivilegeExample rolePrivilegeExample = new RolePrivilegeExample();
        rolePrivilegeExample.createCriteria().andPrivilegeIdEqualTo(privilegeId);

        return rolePrivilegeMapper.selectByExample(rolePrivilegeExample);
    }

    /**
     * 保存或修改
     *
     * @param rolePrivilege
     */
    @Override
    public void saveOrUpdate(RolePrivilege rolePrivilege) throws CustomException {

        if (rolePrivilege.getId() != null) {
            rolePrivilegeMapper.updateByPrimaryKey(rolePrivilege);
        } else {
            rolePrivilegeMapper.insert(rolePrivilege);
        }

    }

    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void deleteById(long id) throws CustomException {

        if (rolePrivilegeMapper.selectByPrimaryKey(id) == null) {
            throw new CustomException("删除失败，未查询到要删除的数据！");
        }

        rolePrivilegeMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据privilegeId删除
     *
     * @param privilegeId
     */
    @Override
    public void deleteByPrivilegeId(long privilegeId) throws CustomException {

        RolePrivilegeExample rolePrivilegeExample = new RolePrivilegeExample();
        rolePrivilegeExample.createCriteria().andPrivilegeIdEqualTo(privilegeId);

        if (rolePrivilegeMapper.selectByExample(rolePrivilegeExample) == null) {
            throw new CustomException("删除失败，未查询到要删除的数据！");
        }

        rolePrivilegeMapper.deleteByExample(rolePrivilegeExample);

    }

    /**
     * 通过roleId删除
     *
     * @param roleId
     */
    @Override
    public void deleteByRoleId(long roleId) throws CustomException {

        RolePrivilegeExample rolePrivilegeExample = new RolePrivilegeExample();
        rolePrivilegeExample.createCriteria().andRoleIdEqualTo(roleId);

        if (rolePrivilegeMapper.selectByExample(rolePrivilegeExample) == null) {
            throw new CustomException("删除失败，未查询到要删除的数据！");
        }

        rolePrivilegeMapper.deleteByExample(rolePrivilegeExample);

    }
}
