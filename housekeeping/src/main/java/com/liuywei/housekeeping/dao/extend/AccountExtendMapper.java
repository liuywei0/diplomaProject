package com.liuywei.housekeeping.dao.extend;

import com.liuywei.housekeeping.domain.User;
import com.liuywei.housekeeping.domain.extend.AccountExtend;

import java.util.List;

public interface AccountExtendMapper {
    List<AccountExtend> selectAllWithUser(String type, Long userId, Long orderId);
}
