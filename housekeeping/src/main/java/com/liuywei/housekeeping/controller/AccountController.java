package com.liuywei.housekeeping.controller;

import com.liuywei.housekeeping.domain.Account;
import com.liuywei.housekeeping.domain.User;
import com.liuywei.housekeeping.domain.extend.AccountExtend;
import com.liuywei.housekeeping.service.IAccountService;
import com.liuywei.housekeeping.utils.Message;
import com.liuywei.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @GetMapping("findByUserId")
    public Message findByUserId(long userId) {
        List<Account> accounts = accountService.findByUserId(userId);
        return Result.success("查询成功！", accounts);
    }

    @GetMapping("findAll")
    public Message findAll() {
        List<AccountExtend> accounts = accountService.findAll();
        return Result.success("查询成功！", accounts);
    }

    @GetMapping("findById")
    public Message findById(long id) {
        Account account = accountService.findById(id);
        return Result.success("查询成功！", account);
    }

    @PostMapping("saveOrUpdate")
    public Message saveOrUpdate(Account account) {
        accountService.saveOrUpdate(account);
        return Result.success("保存成功！");
    }

    @GetMapping("deleteByOrderId")
    public Message deleteByOrderId(long id) {
        accountService.deleteByOrderId(id);
        return Result.success("删除成功！");
    }

    //查询所有退款账户
    @GetMapping("findRef")
    public Message findRef() {
        return Result.success("查询成功！", accountService.selectWithdrawal("退款", null, null));
    }

    //退款审核
    @GetMapping("checkRef")
    public Message checkRef(long id, String msg) {
        accountService.checkRef(id, msg);
        return Result.success("审核完成！");
    }

    //提现审核
    @GetMapping("checkWithdrawal")
    public Message checkWithdrawal(long id, long userId, String msg) {
        accountService.checkWithdrawal(id, userId, msg);
        return Result.success("审核完成！");
    }

    //查询所有提现账户
    @GetMapping("selectWithdrawal")
    public Message selectWithdrawal() {
        return Result.success("查询成功！", accountService.selectWithdrawal("提现", null, null));
    }

    // 充值
    @GetMapping("recharge")
    public Message recharge(long userId, String money) {
        accountService.recharge(userId, money);
        return Result.success("查询成功！");
    }

    // 提现申请
    @GetMapping("withdrawal")
    public Message withdrawal(long userId, String money, String dec) {
        accountService.withdrawal(userId, money, dec);
        return Result.success("提现申请成功！");
    }

}
