package com.liuywei.housekeeping.controller;

import com.liuywei.housekeeping.domain.Privilege;
import com.liuywei.housekeeping.domain.extend.PrivilegeExtend;
import com.liuywei.housekeeping.service.IPrivilegeService;
import com.liuywei.housekeeping.utils.Message;
import com.liuywei.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/privilege")
public class PrivilegeController {

    @Autowired
    private IPrivilegeService privilegeService;

    @GetMapping("findWithChildren")
    public Message findWithChildren() {
        List<PrivilegeExtend> privileges = privilegeService.findWithChildren();
        return Result.success("查询成功！", privileges);
    }

    @GetMapping("findAll")
    public Message findAll() {
        List<Privilege> privileges = privilegeService.findAll();
        return Result.success("查询成功！", privileges);
    }

    @GetMapping("findById")
    public Message findById(long id) {
        Privilege privilege = privilegeService.findById(id);
        return Result.success("查询成功！", privilege);
    }

    @GetMapping("deleteById")
    public Message deleteById(long id) {
        privilegeService.deleteById(id);
        return Result.success("删除成功！");
    }

    @PostMapping("saveOrUpdate")
    public Message saveOrUpdate(Privilege privilege) {
        privilegeService.saveOrUpdate(privilege);
        return Result.success("保存成功！");
    }


}
