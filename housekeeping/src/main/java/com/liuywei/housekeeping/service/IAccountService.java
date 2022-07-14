package com.liuywei.housekeeping.service;

import com.liuywei.housekeeping.domain.Account;
import com.liuywei.housekeeping.domain.User;
import com.liuywei.housekeeping.domain.extend.AccountExtend;
import com.liuywei.housekeeping.utils.CustomException;

import java.util.List;

public interface IAccountService {

    /**
     * 查询所有订单交易额信息
     *
     * @return
     */
    List<AccountExtend> findAll();

    /**
     * 通过id查询订单交易额信息
     *
     * @param id
     * @return
     */
    Account findById(long id);

    /**
     * 通过用户id查询用户所有交易额信息
     *
     * @param userId
     * @return List<Account>
     */
    List<Account> findByUserId(long userId);

    /**
     * 保存或修改
     *
     * @param account
     */
    void saveOrUpdate(Account account) throws CustomException;

    /**
     * 通过订单id删除
     *
     * @param id
     */
    void deleteByOrderId(long id) throws CustomException;

    /**
     * 查找所有提现账户信息
     *
     * @param type
     * @return
     */
    List<AccountExtend> selectWithdrawal(String type, Long userId, Long orderId);

    /**
     * 提现审核
     *
     * @param msg
     */
    void checkWithdrawal(long id, long userId, String msg);

    /**
     * 退款审核
     */
    void checkRef(long id, String msg);

    /**
     * 充值
     */
    void recharge(long userId, String money);

    /**
     * 提现
     * @param userId
     * @param money
     * @param dec
     */
    void withdrawal(long userId, String money, String dec);

}
