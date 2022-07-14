package com.liuywei.housekeeping.controller;

import com.liuywei.housekeeping.domain.Role;
import com.liuywei.housekeeping.domain.extend.RoleExtend;
import com.liuywei.housekeeping.service.IRoleService;
import com.liuywei.housekeeping.utils.Message;
import com.liuywei.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping("findAll")
    public Message findAll() {
        List<Role> roles = roleService.findAll();
        return Result.success("查询成功！", roles);
    }

    @PostMapping("saveOrUpdate")
    public Message saveOrUpdate(Role role) {
        roleService.saveOrUpdate(role);
        return Result.success("保存成功！");
    }

    @GetMapping("deleteById")
    public Message deleteById(long id) {
        roleService.deleteById(id);
        return Result.success("删除成功！");
    }

    @GetMapping("findAllWithUser")
    public Message findAllWithUser() {
        List<RoleExtend> roleExtends = roleService.findAllWithUser();
        return Result.success("查询成功！", roleExtends);
    }
}
