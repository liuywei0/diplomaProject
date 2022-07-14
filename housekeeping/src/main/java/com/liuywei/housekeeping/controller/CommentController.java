package com.liuywei.housekeeping.controller;

import com.liuywei.housekeeping.domain.Comment;
import com.liuywei.housekeeping.domain.extend.CommentExtend;
import com.liuywei.housekeeping.service.ICommentService;
import com.liuywei.housekeeping.utils.Message;
import com.liuywei.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @GetMapping("findByTime")
    public Message findByTime(Long starTime, Long endTime) {
        List<Comment> comments = commentService.findByTime(starTime, endTime);
        return Result.success("查询成功！", comments);
    }

    @GetMapping("findAll")
    public Message findAll() {
        List<Comment> comments = commentService.findAll();
        return Result.success("查询成功！", comments);
    }

    @GetMapping("findByOrderId")
    public Message findByOrderId(Long orderId) {
        CommentExtend comment = commentService.findByOrderId(orderId);
        return Result.success("查询成功！", comment);
    }

    @GetMapping("findByProId")
    public Message findByProId(Long productId) {
        List<CommentExtend> comments = commentService.findByProId(productId);
        return Result.success("查询成功！", comments);
    }

    @GetMapping("deleteById")
    public Message deleteById(long id) {
        commentService.deleteById(id);
        return Result.success("删除成功！");
    }

    @GetMapping("deleteByOrderId")
    public Message deleteByOrderId(long orderId) {
        commentService.deleteByOrderId(orderId);
        return Result.success("删除成功！");
    }

    @PostMapping("review")
    public Message review(long id, long userId, long orderId, String content, int score) {
        // 用户追加评价
        commentService.review(id, userId, orderId, content, score);
        return Result.success("顾客已完成追加评价！");
    }

}
