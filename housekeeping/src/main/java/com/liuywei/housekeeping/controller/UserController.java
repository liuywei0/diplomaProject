package com.liuywei.housekeeping.controller;

import com.liuywei.housekeeping.domain.User;
import com.liuywei.housekeeping.domain.extend.UserExtend;
import com.liuywei.housekeeping.service.IUserService;
import com.liuywei.housekeeping.utils.CustomException;
import com.liuywei.housekeeping.utils.Message;
import com.liuywei.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("findAll")
    public Message findAll() {
        return Result.success("查询成功！", userService.findAll());
    }

    @PostMapping("saveOrUpdate")
    public Message saveOrUpdate(User user) throws Exception {
        userService.saveOrUpdate(user);
        return Result.success("保存成功！");
    }

    @GetMapping("deleteById")
    public Message deleteById(long id) {
        userService.deleteById(id);
        return Result.success("删除成功！");
    }

    @GetMapping("findAllWithRole")
    public Message findAllWithRole() {
        return Result.success("查询成功！", userService.findAllWithRole());
    }

    @GetMapping("findAllEmployee")
    public Message findAllEmployee() {
        return Result.success("查询成功！", userService.findAllEmployee());
    }

    @GetMapping("check")
    public Message check(User user) {
        userService.check(user);
        return Result.success("审核通过！");
    }

    @GetMapping("checkDef")
    public Message checkDef(User user) {
        userService.checkDef(user);
        return Result.success("审核不通过！");
    }

    @GetMapping("findByTel")
    public Message findByTel(String tel) {
        UserExtend user = userService.findByTelWithRole(tel);
        return Result.success("查询成功！", user);
    }

    //login
    @CrossOrigin
    @PostMapping("login")
    public Message login(@RequestBody User user) throws Exception {
        System.out.println(user.getRoleId());
        UserExtend userLogin = userService.login(user.getUsername(), user.getPassword(), user.getRoleId());
        if (userLogin != null) {
            Map<String, String> data = new HashMap<>();
            data.put("token", userLogin.getTelephone());
            data.put("roles", userLogin.getRole().getName());
            data.put("name", userLogin.getTelephone());
            Message success = Result.success("登录成功！");
            success.setData(data);
            return success;
        } else return Result.error("登录失败");
    }


    //info
    @CrossOrigin
    @GetMapping("info")
    public Message getInfo(@RequestParam("token") String token) {
        System.out.println("getInfo" + token);
        UserExtend user = userService.findByTelWithRole(token);
        Map<String, String> data = new HashMap<>();
        data.put("role", user.getRole().getName());
        data.put("name", user.getName());
        data.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        Message success = Result.success("ok");
        success.setData(data);
        System.out.println(data);
        return success;
    }

    @GetMapping("logout")
    public Message logout(HttpSession httpSession) {
        httpSession.invalidate();
        return Result.success("退出登录成功！");
    }

    @GetMapping("editPsw")
    public Message editPsw(long id, String psw, String pass) throws Exception {
        userService.editPsw(id, psw, pass);
        return Result.success("密码修改成功！");
    }
}
