package com.jml.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jml.reggie.entity.SetmealDish;
import com.jml.reggie.mapper.SetmealDishMapper;
import com.jml.reggie.service.SetmealDishservice;
import com.jml.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SetmealDishImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishservice {
}
