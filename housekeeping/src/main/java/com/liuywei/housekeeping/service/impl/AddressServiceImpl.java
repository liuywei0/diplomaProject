package com.liuywei.housekeeping.service.impl;

import com.liuywei.housekeeping.dao.AddressMapper;
import com.liuywei.housekeeping.dao.UserMapper;
import com.liuywei.housekeeping.dao.extend.AddressExtendMapper;
import com.liuywei.housekeeping.domain.Address;
import com.liuywei.housekeeping.domain.AddressExample;
import com.liuywei.housekeeping.domain.User;
import com.liuywei.housekeeping.domain.UserExample;
import com.liuywei.housekeeping.domain.extend.AddressExtend;
import com.liuywei.housekeeping.service.IAddressService;
import com.liuywei.housekeeping.utils.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {

    @Resource
    private AddressMapper addressMapper;

    @Resource
    private AddressExtendMapper addressExtendMapper;

    @Resource
    private UserMapper userMapper;
    /**
     * 查询所有地址
     *
     * @return
     */
    @Override
    public List<Address> findAll() {
        return addressMapper.selectByExample(new AddressExample());
    }

    /**
     * 保存或修改地址
     *
     * @param address
     */
    @Override
    public void saveOrUpdate(Address address) throws CustomException {
        if (address.getId() != null) {
            addressMapper.updateByPrimaryKey(address);
        } else {
            addressMapper.insert(address);
        }
    }

    /**
     * 根据id删除地址
     *
     * @param id
     */
    @Override
    public void deleteById(long id) throws CustomException {
        if (addressMapper.selectByPrimaryKey(id) == null) {
            throw new CustomException("删除失败！未查询到要删除的数据");
        }
        addressMapper.deleteByPrimaryKey(id);
    }

    /**
     * 通过用户id查询地址信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<Address> findByUserId(long userId) {

        AddressExample addressExample = new AddressExample();
        addressExample.createCriteria().andUserIdEqualTo(userId);

        return addressMapper.selectByExample(addressExample);
    }

    /**
     * 级联查询所有地址包括user
     *
     * @return
     */
    @Override
    public List<AddressExtend> findAllWithUser() {
        return addressExtendMapper.selectAllWithUser();
    }

    /**
     * 根据用户手机号查询用户地址
     *
     * @param tel
     * @return
     */
    @Override
    public List<AddressExtend> findByTel(String tel) {
        UserExample user = new UserExample();
        user.createCriteria().andTelephoneEqualTo(tel);
        List<User> users = userMapper.selectByExample(user);

        return addressExtendMapper.selectByUserId(users.get(0).getId());
    }
}
