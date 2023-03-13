package com.jml.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jml.reggie.common.Result;
import com.jml.reggie.entity.Category;
import com.jml.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize){
        Page<Category> p=new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> w=new LambdaQueryWrapper<>();
        w.orderByAsc(Category::getSort);

        categoryService.page(p,w);
        return Result.success(p);
    }

    @PostMapping
    public Result<String> save(@RequestBody Category category){
        categoryService.save(category);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success("修改成功");
    }

    @DeleteMapping
    public Result<String> del(String ids){
        Long id=Long.parseLong(ids);
        categoryService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<List<Category>> list(Category category){

        LambdaQueryWrapper<Category> w=new LambdaQueryWrapper<>();
        w.eq(category.getType()!=null,Category::getType,category.getType()).orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(w);
        return Result.success(list);

    }
}
