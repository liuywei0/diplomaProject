package com.liuywei.housekeeping.service.impl;

import com.liuywei.housekeeping.dao.*;
import com.liuywei.housekeeping.dao.extend.CommentExtendMapper;
import com.liuywei.housekeeping.domain.*;
import com.liuywei.housekeeping.service.ICommentService;
import com.liuywei.housekeeping.utils.CustomException;
import org.springframework.stereotype.Service;
import com.liuywei.housekeeping.domain.extend.CommentExtend;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private CommentExtendMapper commentExtendMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private OrderLineMapper orderLineMapper;

    /**
     * 查询所有评论信息
     *
     * @return
     */
    @Override
    public List<Comment> findAll() {
        return commentMapper.selectByExample(new CommentExample());
    }

    /**
     * 通过订单id查询评论
     *
     * @param orderId
     * @return
     */
    @Override
    public CommentExtend findByOrderId(long orderId) {
        CommentExtend commentExtend = commentExtendMapper.selectByOrderId(orderId);
        if (commentExtend == null) {
            return null;
        }
        Order order = orderMapper.selectByPrimaryKey(commentExtend.getOrderId());
        User employee = userMapper.selectByPrimaryKey(order.getEmployeeId());
        commentExtend.setEmployee(employee);
        return commentExtend;
    }

    /**
     * 通过商品id查找评论
     *
     * @param productId
     * @return
     */
    @Override
    public List<CommentExtend> findByProId(long productId) {

        OrderLineExample orderLineExample = new OrderLineExample();
        orderLineExample.createCriteria().andProductIdEqualTo(productId);

        List<OrderLine> orderLines = orderLineMapper.selectByExample(orderLineExample);

        List<CommentExtend> list = new ArrayList<>();
        for (OrderLine orderLine : orderLines) {
            CommentExtend byOrderId = findByOrderId(orderLine.getOrderId());
            if (byOrderId != null) {
                list.add(byOrderId);
            }
        }
        return list;
    }

    /**
     * 通过评论时间查询评论
     * <p>
     * 一个时间范围
     *
     * @param starTime
     * @param endTime
     * @return
     */
    @Override
    public List<Comment> findByTime(Long starTime, Long endTime) {

        endTime = endTime == null ? new Date().getTime() : endTime;

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andCommentTimeBetween(starTime, endTime);

        return commentMapper.selectByExample(commentExample);
    }

    /**
     * 根据评论id删除评论
     *
     * @param id
     */
    @Override
    public void deleteById(long id) throws CustomException {
        if (commentMapper.selectByPrimaryKey(id) == null) {
            throw new CustomException("删除失败！未查询到要删除的数据");
        }
        commentMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据订单id删除评论
     *
     * @param orderId
     */
    @Override
    public void deleteByOrderId(long orderId) throws CustomException {

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andOrderIdEqualTo(orderId);

        if (commentMapper.selectByExample(commentExample) == null) {
            throw new CustomException("删除失败！未查询到要删除的数据");
        }
        commentMapper.deleteByExample(commentExample);

    }

    /**
     * 顾客评价
     */
    @Override
    public void comments(long orderId, long userId, String content, int score) {
        Comment comment = new Comment();
        // 添加评论数据
        comment.setCommentTime(new Date().getTime());
        comment.setContent(content);
        comment.setOrderId(orderId);
        comment.setUserId(userId);
        comment.setScore(score);

        // 若该订单已存在评价，则为追评
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andOrderIdEqualTo(orderId);
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() != 0) {
            comment.setPartentId(comments.get(0).getId());
        }

        // 插入数据
        commentMapper.insert(comment);

        // 找到评价的员工id
        Long employeeId = orderMapper.selectByPrimaryKey(orderId).getEmployeeId();
        // 找到该员工
        User employee = userMapper.selectByPrimaryKey(employeeId);
        if (employee.getComment() != null && employee.getComment().equals(0)) {
            BigDecimal sc = new BigDecimal(employee.getComment()).add(new BigDecimal(score)).divide(new BigDecimal(2));
            employee.setComment(sc.toString());
        } else
            employee.setComment(score + "");
        userMapper.updateByPrimaryKey(employee);

    }

    /**
     * 追评
     *
     * @param content
     * @param score
     */
    @Override
    public void review(long id, long userId, long orderId, String content, int score) {
        Comment comment = new Comment();
        comment.setPartentId(id);
        comment.setOrderId(orderId);
        comment.setScore(score);
        comment.setContent(content);
        comment.setCommentTime(new Date().getTime());

        commentMapper.insert(comment);
    }


}
