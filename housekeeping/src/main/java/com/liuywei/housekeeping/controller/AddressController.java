package com.liuywei.housekeeping.controller;

import com.liuywei.housekeeping.domain.Address;
import com.liuywei.housekeeping.domain.extend.AddressExtend;
import com.liuywei.housekeeping.service.IAddressService;
import com.liuywei.housekeeping.utils.Message;
import com.liuywei.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private IAddressService addressService;

    @GetMapping("findAll")
    public Message findAll() {
        List<Address> addresses = addressService.findAll();
        return Result.success("查询成功！", addresses);
    }

    @GetMapping("findAllWithUser")
    public Message findAllWithUser() {
        List<AddressExtend> addresses = addressService.findAllWithUser();
        return Result.success("查询成功！", addresses);
    }

    @PostMapping("saveOrUpdate")
    public Message saveOrUpdate(Address address) {
        addressService.saveOrUpdate(address);
        return Result.success("保存成功！");
    }

    @GetMapping("deleteById")
    public Message deleteById(long id) {
        addressService.deleteById(id);
        return Result.success("删除成功！");
    }

    @GetMapping("findByUserId")
    public Message findByUserId(Long userId) {
        List<Address> addresses = addressService.findByUserId(userId);
        return Result.success("查询成功", addresses);
    }

    @GetMapping("findByTel")
    public Message findByTel(String tel) {
        List<AddressExtend> addresses = addressService.findByTel(tel);
        return Result.success("查询成功", addresses);
    }

}
