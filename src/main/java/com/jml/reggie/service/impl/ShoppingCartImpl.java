package com.jml.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jml.reggie.entity.ShoppingCart;
import com.jml.reggie.mapper.ShoppingCartMapper;
import com.jml.reggie.service.ShoppingCartService;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
