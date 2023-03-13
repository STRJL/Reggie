package com.jml.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jml.reggie.common.Result;
import com.jml.reggie.dto.SetmealDto;
import com.jml.reggie.entity.Category;
import com.jml.reggie.entity.Dish;
import com.jml.reggie.entity.Setmeal;
import com.jml.reggie.entity.SetmealDish;
import com.jml.reggie.service.CategoryService;
import com.jml.reggie.service.DishService;
import com.jml.reggie.service.SetmealDishservice;
import com.jml.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishservice dishservice;

    @Autowired
    private DishService services;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public Result<Page> page(int page,int pageSize,String name){
        Page<Setmeal> setmealPage=new Page<>();
        Page<SetmealDto> dtoPage=new Page<>();
        LambdaQueryWrapper<Setmeal> w=new LambdaQueryWrapper<>();
        w.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);

        setmealService.page(setmealPage,w);

        BeanUtils.copyProperties(setmealPage,dtoPage,"records");

        List<Setmeal> records=setmealPage.getRecords();


        List<SetmealDto> dtos=records.stream().map((item)->{
            SetmealDto dto=new SetmealDto();
            BeanUtils.copyProperties(item,dto);

            Long id=item.getCategoryId();

            Category byId = categoryService.getById(id);

            if (byId.getName()!=null){
                dto.setCategoryName(byId.getName());
            }

            return dto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(dtos);

        return Result.success(dtoPage);

    }

    @PostMapping("/status/{status}")
    public Result<String> update(@PathVariable Integer status, String ids){

        log.info(status.toString());
        List<Long> split = new ArrayList<>(Arrays.asList(ids.split(","))).stream().map(Long::parseLong).collect(Collectors.toList());
        List<Setmeal> dishes = setmealService.listByIds(split);
        for (Setmeal dish : dishes) {
            dish.setStatus(status);
        }
        setmealService.updateBatchById(dishes);
        return Result.success("修改成功");

    }

    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids){
//        List<Long> split = new ArrayList<>(Arrays.asList(ids.split(","))).stream().map(Long::parseLong).collect(Collectors.toList());

        setmealService.DeleteSetmeal(ids);
        return Result.success("删除成功");
    }

    @PostMapping
    public Result<String> save(@RequestBody SetmealDto dto){
        setmealService.saveSetmeal(dto);
        return Result.success("添加成功");
    }

    @GetMapping("/list")
    public Result<List<SetmealDto>> GetSetm(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> s=new LambdaQueryWrapper<>();
        s.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId()).eq(Setmeal::getStatus,1);
        List<Setmeal> list = setmealService.list(s);

        List<SetmealDto> setmealDis=list.stream().map((item)->{
            SetmealDto dto=new SetmealDto();

            BeanUtils.copyProperties(item,dto);

            Long id=item.getCategoryId();

            Category byId = categoryService.getById(id);

            if (byId.getName()!=null){
                dto.setCategoryName(byId.getName());
            }

            Long ids=item.getId();
            LambdaQueryWrapper<SetmealDish> d=new LambdaQueryWrapper<>();
            d.eq(SetmealDish::getSetmealId,ids);
            List<SetmealDish> list1 = dishservice.list(d);

            dto.setSetmealDishes(list1);

            return dto;

        }).collect(Collectors.toList());

        return Result.success(setmealDis);
    }

    @GetMapping("/dish/{id}")
    public Result<List<SetmealDish>> DishLsit(@PathVariable String id){

        LambdaQueryWrapper<SetmealDish> d=new LambdaQueryWrapper<>();
        d.eq(StringUtils.isNotEmpty(id),SetmealDish::getSetmealId,id);
        List<SetmealDish> list1 = dishservice.list(d);

//        List<Dish> dishes=list1.stream().map((item)->{
//            String f=item.getDishId();
//            Dish dish = services.getById(f);
//
//            return dish;
//
//        }).collect(Collectors.toList());

        return Result.success(list1);


    }

    @GetMapping("/{id}")
    public Result<SetmealDto> GetOne(@PathVariable String id){
        LambdaQueryWrapper<Setmeal> w=new LambdaQueryWrapper<>();
        w.eq(Setmeal::getId,Long.parseLong(id));
        Setmeal one = setmealService.getOne(w);
        SetmealDto setmealDto=new SetmealDto();

        BeanUtils.copyProperties(one,setmealDto);

        LambdaQueryWrapper<SetmealDish> dishs=new LambdaQueryWrapper<>();
        dishs.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = dishservice.list(dishs);

        if (list.size()>0){
            setmealDto.setSetmealDishes(list);
        }
        return Result.success(setmealDto);
    }

    @PutMapping
    public Result<String> update(@RequestBody SetmealDto dto){
        setmealService.updateSetmeal(dto);
        return Result.success("修改成功");
    }
}
