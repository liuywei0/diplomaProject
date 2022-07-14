package com.liuywei.housekeeping.service;

import com.liuywei.housekeeping.domain.Address;
import com.liuywei.housekeeping.domain.extend.AddressExtend;
import com.liuywei.housekeeping.utils.CustomException;

import java.util.List;

public interface IAddressService {

    /**
     * 查询所有地址
     *
     * @return
     */
    List<Address> findAll();

    /**
     * 保存或修改地址
     *
     * @param address
     */
    void saveOrUpdate(Address address) throws CustomException;

    /**
     * 根据id删除地址
     *
     * @param id
     */
    void deleteById(long id) throws CustomException;

    /**
     * 通过用户id查询地址信息
     *
     * @param userId
     * @return
     */
    List<Address> findByUserId(long userId);

    /**
     * 级联查询所有地址包括user
     *
     * @return
     */
    List<AddressExtend> findAllWithUser();

    /**
     * 根据用户手机号查询用户地址
     *
     * @param tel
     * @return
     */
    List<AddressExtend> findByTel(String tel);

}
