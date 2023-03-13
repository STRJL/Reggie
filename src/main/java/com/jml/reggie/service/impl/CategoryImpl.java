package com.jml.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jml.reggie.entity.Category;
import com.jml.reggie.mapper.CategoryMapper;
import com.jml.reggie.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
