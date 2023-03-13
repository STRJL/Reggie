package com.jml.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jml.reggie.entity.AddressBook;
import com.jml.reggie.entity.OrderDetail;
import com.jml.reggie.entity.Orders;
import com.jml.reggie.entity.ShoppingCart;
import com.jml.reggie.mapper.OrderDetailMapper;
import com.jml.reggie.mapper.OrderMapper;
import com.jml.reggie.service.AddressBookService;
import com.jml.reggie.service.OrderDetailService;
import com.jml.reggie.service.OrdersService;
import com.jml.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrdersService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService detailService;

    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private AddressBookService addressBook;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Override
    public void submit(Orders orders) {
        Long id = null;
        if (request.getSession().getAttribute("user") != null) {
            id = (Long) request.getSession().getAttribute("user");
        }

        orders.setUserId(id);

        LambdaQueryWrapper<ShoppingCart> sj = new LambdaQueryWrapper<>();
        sj.eq(ShoppingCart::getUserId, id);
        List<ShoppingCart> list = cartService.list(sj);

        double d = 0;
        for (ShoppingCart l : list) {
            double v = l.getAmount().doubleValue() * l.getNumber();
            d += v;
        }
        long id1 = IdWorker.getId();

        List<OrderDetail> od=list.stream().map((item)->{
            OrderDetail ode=new OrderDetail();
            ode.setDishId(item.getDishId());
            ode.setAmount(item.getAmount());
            ode.setNumber(item.getNumber());
            ode.setOrderId(id1);
            ode.setName(item.getName());
            ode.setImage(item.getImage());
            ode.setDishFlavor(item.getDishFlavor());

            return ode;
        }).collect(Collectors.toList());
        detailService.saveBatch(od);
        orders.setAmount(new BigDecimal(d));

        LambdaQueryWrapper<AddressBook> ad = new LambdaQueryWrapper<>();
        ad.eq(AddressBook::getId, orders.getAddressBookId());
        ad.eq(AddressBook::getIsDefault, 1);
        AddressBook one = addressBook.getOne(ad);
        orders.setStatus(1);
        orders.setAddress(one.getDetail());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setNumber(String.valueOf(id1));
        ordersService.save(orders);

        shoppingCartService.remove(sj);


    }
}
