package com.liuywei.housekeeping.dao.extend;

import com.liuywei.housekeeping.domain.Comment;
import com.liuywei.housekeeping.domain.extend.CommentExtend;

import java.util.List;

public interface CommentExtendMapper {

    CommentExtend selectByOrderId(long orderId);

    List<Comment> selectChildren(long id);
}
