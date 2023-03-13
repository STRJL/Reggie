package com.jml.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jml.reggie.common.Result;
import com.jml.reggie.dto.DishDto;
import com.jml.reggie.entity.Category;
import com.jml.reggie.entity.Dish;
import com.jml.reggie.entity.DishFlavor;
import com.jml.reggie.service.CategoryService;
import com.jml.reggie.service.DishFlavorService;
import com.jml.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService flavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> save(@RequestBody DishDto dish){
        dishService.saveWithFileavor(dish);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result<String> update(@RequestBody DishDto dish){
        dishService.UpdateWithFileavor(dish);
        return Result.success("修改成功");
    }

    @GetMapping("/{id}")
    public Result<DishDto> GetOne(@PathVariable String id){

        Dish ds=new Dish();
        DishDto dishDto=new DishDto();
        LambdaQueryWrapper<Dish> w=new LambdaQueryWrapper<>();
        w.eq(Dish::getId,Long.parseLong(id));
        ds= dishService.getOne(w);
        BeanUtils.copyProperties(ds,dishDto);

        Category byId = categoryService.getById(ds.getCategoryId());

        if (byId.getName()!=null){
            dishDto.setCategoryName(byId.getName());
        }

        LambdaQueryWrapper<DishFlavor> f=new LambdaQueryWrapper<>();
        f.eq(DishFlavor::getDishId,Long.parseLong(id)).eq(DishFlavor::getIsDeleted,0);
        List<DishFlavor> flavors=flavorService.list(f);

        System.out.println(flavors.size());
        if (flavors.size()>0){
            dishDto.setFlavors(flavors);
        }

        return Result.success(dishDto);

    }

    @GetMapping("/list")
    public Result<List<DishDto>> list(Dish dish){

        LambdaQueryWrapper<Dish> w=new LambdaQueryWrapper<>();
        w.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        w.like(StringUtils.isNotEmpty(dish.getName()),Dish::getName,dish.getName());
        w.eq(Dish::getStatus,1);
        w.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(w);
        List<DishDto> dtoList=list.stream().map((item)->{
            DishDto dishDto=new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();

            Category byId = categoryService.getById(categoryId);

            if (byId.getName()!=null){
                dishDto.setCategoryName(byId.getName());
            }

            Long id=item.getId();
            LambdaQueryWrapper<DishFlavor> flavor=new LambdaQueryWrapper<>();
            flavor.eq(DishFlavor::getDishId,id);
            List<DishFlavor> list1 = flavorService.list(flavor);
            dishDto.setFlavors(list1);

            return dishDto;

        }).collect(Collectors.toList());


        return Result.success(dtoList);

    }

    @GetMapping("/page")
    public Result<Page> page(int page,int pageSize,String name){
        Page<Dish> p=new Page<>(page,pageSize);
        Page<DishDto> dtoPage=new Page<>();
        LambdaQueryWrapper<Dish> w=new LambdaQueryWrapper<>();
        w.like(StringUtils.isNotEmpty(name),Dish::getName,name).orderByAsc(Dish::getSort);
        dishService.page(p, w);

        //对象拷贝
        BeanUtils.copyProperties(p,dtoPage,"records");

        List<Dish> records=p.getRecords();

        List<DishDto> list=records.stream().map((item)->{
            DishDto dishDto=new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();

            Category byId = categoryService.getById(categoryId);

            if (byId.getName()!=null){
                dishDto.setCategoryName(byId.getName());
            }


            return dishDto;

        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return Result.success(dtoPage);

    }




    @PostMapping ("/status/{status}")
    public Result<String> update(@PathVariable Integer status, String ids){

        log.info(status.toString());
        List<Long> split = new ArrayList<>(Arrays.asList(ids.split(","))).stream().map(Long::parseLong).collect(Collectors.toList());
        List<Dish> dishes = dishService.listByIds(split);
        for (Dish dish : dishes) {
            dish.setStatus(status);
        }
//        LambdaQueryWrapper<Dish> w=new LambdaQueryWrapper<>();
//        w.eq(Dish::getId,split);
        dishService.updateBatchById(dishes);
        return Result.success("修改成功");

    }

    @DeleteMapping
    public Result<String> delete(String ids){
        List<Long> split = new ArrayList<>(Arrays.asList(ids.split(","))).stream().map(Long::parseLong).collect(Collectors.toList());
        log.info(String.valueOf(split));
        dishService.removeByIds(split);
        return Result.success("删除成功");

    }
}
