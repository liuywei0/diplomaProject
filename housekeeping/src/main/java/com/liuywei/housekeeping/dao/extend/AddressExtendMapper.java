package com.liuywei.housekeeping.dao.extend;

import com.liuywei.housekeeping.domain.extend.AddressExtend;

import java.util.List;

public interface AddressExtendMapper {
    List<AddressExtend> selectAllWithUser();

    List<AddressExtend> selectByUserId(Long userId);
}
