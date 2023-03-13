package com.jml.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jml.reggie.entity.Dish;
import com.jml.reggie.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
