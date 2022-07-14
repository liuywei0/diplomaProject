package com.liuywei.housekeeping.service;

import com.liuywei.housekeeping.domain.Comment;
import com.liuywei.housekeeping.domain.Order;
import com.liuywei.housekeeping.domain.extend.CommentExtend;
import com.liuywei.housekeeping.utils.CustomException;

import java.util.List;

public interface ICommentService {

    /**
     * 查询所有评论信息
     *
     * @return
     */
    List<Comment> findAll();

    /**
     * 通过订单id查询评论
     *
     * @param orderId
     * @return
     */
    CommentExtend findByOrderId(long orderId);

    /**
     * 通过评论时间查询评论
     *
     * @param starTime
     * @param endTime
     * @return
     */
    List<Comment> findByTime(Long starTime, Long endTime);

    /**
     * 根据评论id删除评论
     *
     * @param id
     */
    void deleteById(long id) throws CustomException;

    /**
     * 根据订单id删除评论
     *
     * @param orderId
     */
    void deleteByOrderId(long orderId) throws CustomException;

    /**
     * 顾客评价
     */
    void comments(long orderId, long userId, String content, int score);


    /**
     * 追评
     *
     * @param content
     * @param score
     */
    void review(long id, long userId, long orderId, String content, int score);

    /**
     * 通过商品id查找评论
     *
     * @param productId
     * @return
     */
    List<CommentExtend> findByProId(long productId);

}
