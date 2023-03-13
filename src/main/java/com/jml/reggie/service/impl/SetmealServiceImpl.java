package com.jml.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jml.reggie.common.CustomException;
import com.jml.reggie.dto.SetmealDto;
import com.jml.reggie.entity.Setmeal;
import com.jml.reggie.entity.SetmealDish;
import com.jml.reggie.mapper.SetmealMapper;
import com.jml.reggie.service.SetmealDishservice;
import com.jml.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishservice dishservice;

    @Transactional
    public void saveSetmeal(SetmealDto dto) {
        this.save(dto);

        String id=dto.getId().toString();

        List<SetmealDish> dishes=dto.getSetmealDishes();

        dishes.stream().map((item)->{
            item.setSetmealId(id);
            return item;
        }).collect(Collectors.toList());

        dishservice.saveBatch(dishes);

    }

    @Transactional
    public void updateSetmeal(SetmealDto dto) {
        this.updateById(dto);

        LambdaQueryWrapper<SetmealDish> w=new LambdaQueryWrapper<>();
        w.eq(SetmealDish::getSetmealId,dto.getId().toString());
        dishservice.remove(w);

        List<SetmealDish> setmealDishes = dto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(dto.getId().toString());
            return item;
        }).collect(Collectors.toList());

        dishservice.saveBatch(setmealDishes);
    }

    @Transactional
    public void DeleteSetmeal(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> w=new LambdaQueryWrapper<>();
        w.in(Setmeal::getId,ids);
        w.eq(Setmeal::getStatus,1);
        int count=this.count(w);
        if (count>0){
            throw new CustomException("套餐正在售卖中");
        }

        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> dish=new LambdaQueryWrapper<>();
        dish.in(SetmealDish::getSetmealId,ids);

        dishservice.remove(dish);
    }
}
