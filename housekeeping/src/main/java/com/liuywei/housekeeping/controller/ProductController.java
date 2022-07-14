package com.liuywei.housekeeping.controller;

import com.liuywei.housekeeping.domain.Product;
import com.liuywei.housekeeping.domain.extend.ProductExtend;
import com.liuywei.housekeeping.service.IProductService;
import com.liuywei.housekeeping.utils.Message;
import com.liuywei.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("findAll")
    public Message findAll(){
        List<Product> products = productService.findAll();
        return Result.success("查询成功！", products);
    }

    @GetMapping("findAllWithCategory")
    public Message findAllWithCategory() {
        List<ProductExtend> productExtends = productService.findAllWithCategory();
        return Result.success("查询成功！", productExtends);
    }

    @GetMapping("deleteById")
    public Message deleteById(Long id) {
        System.out.println(id);
        productService.deleteById(id);
        return Result.success("删除成功！");
    }

    @PostMapping("saveOrUpdate")
    public Message saveOrUpdate(Product product) {
        productService.saveOrUpdate(product);
        return Result.success("保存成功！");
    }

}
