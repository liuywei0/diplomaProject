package com.liuywei.housekeeping.service.impl;

import com.liuywei.housekeeping.dao.AccountMapper;
import com.liuywei.housekeeping.dao.OrderMapper;
import com.liuywei.housekeeping.dao.UserMapper;
import com.liuywei.housekeeping.dao.extend.AccountExtendMapper;
import com.liuywei.housekeeping.domain.Account;
import com.liuywei.housekeeping.domain.AccountExample;
import com.liuywei.housekeeping.domain.Order;
import com.liuywei.housekeeping.domain.User;
import com.liuywei.housekeeping.domain.extend.AccountExtend;
import com.liuywei.housekeeping.service.IAccountService;
import com.liuywei.housekeeping.utils.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class AccountServiceImpl implements IAccountService {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private AccountExtendMapper accountExtendMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 查询所有订单交易额信息
     *
     * @return
     */
    @Override
    public List<AccountExtend> findAll() {
        return accountExtendMapper.selectAllWithUser(null, null, null);
    }

    /**
     * 通过id查询订单交易额信息
     *
     * @param id
     * @return
     */
    @Override
    public Account findById(long id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过用户id查询用户所有交易额信息
     *
     * @param userId
     * @return List<Account>
     */
    @Override
    public List<Account> findByUserId(long userId) {
        //拼接sql语句
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andUserIdEqualTo(userId);

        return accountMapper.selectByExample(accountExample);
    }

    /**
     * 保存或修改
     *
     * @param account
     */
    @Override
    public void saveOrUpdate(Account account) throws CustomException {
        if (account.getId() != null) {
            accountMapper.updateByPrimaryKey(account);
        } else {
            accountMapper.insert(account);
        }
    }

    /**
     * 通过订单id删除
     *
     * @param id
     */
    @Override
    public void deleteByOrderId(long id) throws CustomException {

        Account account = accountMapper.selectByPrimaryKey(id);
        if (account == null) {
            throw new CustomException("删除失败！未查询到要删除的数据");
        }

        accountMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查找所有提现账户信息
     *
     * @param
     * @return
     */
    @Override
    public List<AccountExtend> selectWithdrawal(String type, Long userId, Long orderId) {
        return accountExtendMapper.selectAllWithUser(type, userId, orderId);
    }

    /**
     * 提现审核
     *
     * @param msg
     */
    @Override
    public void checkWithdrawal(long id, long userId, String msg) {
        Account account = accountMapper.selectByPrimaryKey(id);

        // 添加审核时间，证明已审核
        account.setTransactionTime(new Date().getTime());

        // 提现成功，修改用户账户余额
        if (msg.equals("同意")) {
            // 查询用户信息
            User user = userMapper.selectByPrimaryKey(userId);
            // 用bigdecimal进行减法运算
            BigDecimal subtract = new BigDecimal(user.getBalance()).subtract(new BigDecimal(account.getMoney())).setScale(4, BigDecimal.ROUND_HALF_UP);
            user.setBalance(subtract.toString());
            userMapper.updateByPrimaryKey(user);
        }
        accountMapper.updateByPrimaryKey(account);
    }

    /**
     * 退款审核
     *
     * @param id
     */
    @Override
    public void checkRef(long id, String msg) {
        Account account = accountMapper.selectByPrimaryKey(id);
        account.setTransactionTime(new Date().getTime()); // 添加审核时间，证明已审核过
        // 修改明细信息
        // 审核通过
        if (msg.equals("同意")) {
            // 修改订单价格
            Order order = orderMapper.selectByPrimaryKey(id);
            String total = new BigDecimal(order.getTotal()).subtract(new BigDecimal(account.getMoney())).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
            order.setTotal(total);
            orderMapper.updateByPrimaryKey(order);

            // 退款到顾客账户
            User customer = userMapper.selectByPrimaryKey(account.getUserId());
            String balance = new BigDecimal(customer.getBalance()).add(new BigDecimal(account.getMoney())).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
            customer.setBalance(balance);
            userMapper.updateByPrimaryKey(customer);
        }

        accountMapper.updateByPrimaryKey(account);
    }

    /**
     * 充值
     *
     * @param money
     */
    @Override
    public void recharge(long userId, String money) {
        // 生成充值明细
        Account account = new Account();
        account.setTransactionTime(new Date().getTime());
        account.setDescription("充值");
        account.setMoney(money);
        account.setType("充值");
        account.setUserId(userId);
        accountMapper.insert(account);

        // 充值到账
        User user = userMapper.selectByPrimaryKey(userId);
        String balance = new BigDecimal(money).add(new BigDecimal(user.getBalance())).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
        user.setBalance(balance);
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * 提现申请
     *
     * @param userId
     * @param money
     */
    @Override
    public void withdrawal(long userId, String money, String dec) {
        Account account = new Account();
        account.setType("提现");
        account.setDescription(dec);
        account.setUserId(userId);
        account.setMoney(money);

        accountMapper.insert(account);
    }
}
