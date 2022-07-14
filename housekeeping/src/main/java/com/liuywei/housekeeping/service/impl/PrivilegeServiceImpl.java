package com.liuywei.housekeeping.service.impl;

import com.liuywei.housekeeping.dao.PrivilegeMapper;
import com.liuywei.housekeeping.dao.extend.PrivilegeExtendMapper;
import com.liuywei.housekeeping.domain.Privilege;
import com.liuywei.housekeeping.domain.PrivilegeExample;
import com.liuywei.housekeeping.domain.extend.PrivilegeExtend;
import com.liuywei.housekeeping.service.IPrivilegeService;
import com.liuywei.housekeeping.utils.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PrivilegeServiceImpl implements IPrivilegeService {

    @Resource
    private PrivilegeMapper privilegeMapper;

    @Resource
    private PrivilegeExtendMapper privilegeExtendMapper;

    /**
     * 查询所有权限信息
     *
     * @return
     */
    @Override
    public List<Privilege> findAll() {
        return privilegeMapper.selectByExample(new PrivilegeExample());
    }

    /**
     * 根据id查询权限信息
     *
     * @param id
     * @return
     */
    @Override
    public Privilege findById(long id) {
        return privilegeMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存或修改权限信息
     *
     * @param privilege
     */
    @Override
    public void saveOrUpdate(Privilege privilege) throws CustomException {
        if (privilege.getId() == null) {
            privilegeMapper.updateByPrimaryKey(privilege);
        } else {
            privilegeMapper.insert(privilege);
        }
    }

    /**
     * 根据id删除权限信息
     *
     * @param id
     */
    @Override
    public void deleteById(long id) throws CustomException {
        if (privilegeMapper.selectByPrimaryKey(id) == null) {
            throw new CustomException("删除失败！未查询到要删除的数据");
        }
        privilegeMapper.deleteByPrimaryKey(id);
    }

    /**
     * 级联查询所有包括其子类
     *
     * @return
     */
    @Override
    public List<PrivilegeExtend> findWithChildren() {
        return privilegeExtendMapper.selectWithChildren();
    }
}
