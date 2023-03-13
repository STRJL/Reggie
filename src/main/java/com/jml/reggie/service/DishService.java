package com.jml.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jml.reggie.dto.DishDto;
import com.jml.reggie.entity.Dish;
import org.springframework.beans.factory.annotation.Autowired;

public interface DishService extends IService<Dish> {

    public void saveWithFileavor(DishDto dishDto);

    public void UpdateWithFileavor(DishDto dishDto);
}
