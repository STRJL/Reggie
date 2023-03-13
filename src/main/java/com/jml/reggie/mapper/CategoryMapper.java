package com.jml.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jml.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
