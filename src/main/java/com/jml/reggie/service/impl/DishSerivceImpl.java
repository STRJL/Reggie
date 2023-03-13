package com.jml.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jml.reggie.dto.DishDto;
import com.jml.reggie.entity.Dish;
import com.jml.reggie.entity.DishFlavor;
import com.jml.reggie.mapper.DishMapper;
import com.jml.reggie.service.DishFlavorService;
import com.jml.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishSerivceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService flavorService;

    @Transactional
    public void saveWithFileavor(DishDto dishDto){
        this.save(dishDto);

        Long id= dishDto.getId();

        List<DishFlavor> flavors=dishDto.getFlavors();
        flavors= flavors.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        flavorService.saveBatch(flavors);

    }

    @Transactional
    public void UpdateWithFileavor(DishDto dishDto) {

        this.updateById(dishDto);

        Long id=dishDto.getId();

        //直接循环获取修改并未删除数据

//        LambdaQueryWrapper<DishFlavor> flavor=new LambdaQueryWrapper<>();
//        flavor.eq(DishFlavor::getDishId,id);
//        List<DishFlavor> list = flavorService.list(flavor);
//
//        List<DishFlavor> flavors=dishDto.getFlavors();
//        List<DishFlavor> saves=new ArrayList<>();
//        flavors.stream().map((item)->{
//            if (item.getDishId()==null){
//                item.setDishId(id);
//                saves.add(item);
//            }
//            return item;
//        }).collect(Collectors.toList());
//        list= list.stream().map((item)->{
//
//          int szi=0;
//
//            for (DishFlavor f : flavors) {
//
//
//                if (item.getId().equals(f.getId())){
//                    szi++;
//                    break;
//                }
//            }
//            if (szi==0){
//                item.setIsDeleted(1);
//            }
//            return item;
//        }).collect(Collectors.toList());
//
//        flavorService.updateBatchById(list);
//        log.info(saves.toString());
//        if (saves.size()>0){
//            flavorService.saveBatch(saves);
//        }

        //先清除数据再添加

        LambdaQueryWrapper<DishFlavor> f=new LambdaQueryWrapper<>();
        f.eq(DishFlavor::getDishId,id);
        flavorService.remove(f);

        //添加
        List<DishFlavor> flavors=dishDto.getFlavors();
        flavors= flavors.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        flavorService.saveBatch(flavors);

    }


}
