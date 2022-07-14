package com.liuywei.housekeeping.controller;

import com.liuywei.housekeeping.domain.RolePrivilege;
import com.liuywei.housekeeping.service.IRolePrivilegeService;
import com.liuywei.housekeeping.utils.Message;
import com.liuywei.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rolePrivilege")
public class RolePrivilegeController {

    @Autowired
    private IRolePrivilegeService rolePrivilegeService;

    @GetMapping("findAll")
    public Message findAll() {
        List<RolePrivilege> rolePrivilegeList = rolePrivilegeService.findAll();
        return Result.success("查询成功！", rolePrivilegeList);
    }

    @GetMapping("findByRoleId")
    public Message findByRoleId(long roleId) {
        List<RolePrivilege> privilegeList = rolePrivilegeService.findByRoleId(roleId);
        return Result.success("查询成功！", privilegeList);
    }

    @GetMapping("findByPrivilegeId")
    public Message findByPrivilegeId(long privilegeId) {
        List<RolePrivilege> privilegeList = rolePrivilegeService.findByPrivilegeId(privilegeId);
        return Result.success("查询成功！", privilegeList);
    }

    @PostMapping("saveOrUpdate")
    public Message saveOrUpdate(RolePrivilege rolePrivilege) {
        rolePrivilegeService.saveOrUpdate(rolePrivilege);
        return Result.success("保存成功！");
    }

    @GetMapping("deleteById")
    public Message deleteById(long id) {
        rolePrivilegeService.deleteById(id);
        return Result.success("删除成功！");
    }

    @GetMapping("deleteByRoleId")
    public Message deleteByRoleId(long roleId) {
        rolePrivilegeService.deleteByRoleId(roleId);
        return Result.success("删除成功！");
    }

}
