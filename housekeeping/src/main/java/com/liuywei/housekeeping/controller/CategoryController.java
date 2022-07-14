package com.liuywei.housekeeping.controller;

import com.liuywei.housekeeping.domain.Category;
import com.liuywei.housekeeping.domain.extend.CategoryExtend;
import com.liuywei.housekeeping.service.ICategoryService;
import com.liuywei.housekeeping.utils.Message;
import com.liuywei.housekeeping.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@Api(value = "分类栏目接口")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("findAllWithChildren")
    public Message findAllWithChildren() {
        List<CategoryExtend> categories = categoryService.findAllWithChildren();
        return Result.success("查询成功", categories);
    }

    @GetMapping("findAll")
    public Message findAll() {
        List<Category> category = categoryService.findAll();
        return Result.success("查询成功", category);
    }

    @GetMapping("deleteById")
    public Message deleteById(long id) {
        categoryService.deleteById(id);
        return Result.success("删除成功");
    }

    @PostMapping("saveOrUpdate")
    public Message saveOrUpdate(Category category) {
        categoryService.saveOrUpdate(category);
        return Result.success("保存成功");
    }

    @GetMapping("findByName")
    public Message findByName(String name) {
        List<Category> categories = categoryService.findByName(name);
        return Result.success("查询成功！", categories);
    }


}
