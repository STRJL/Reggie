package com.jml.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jml.reggie.entity.OrderDetail;
import com.jml.reggie.mapper.OrderDetailMapper;
import com.jml.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
