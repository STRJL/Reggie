package com.jml.reggie.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jml.reggie.common.Result;
import com.jml.reggie.dto.OrdersDto;
import com.jml.reggie.entity.*;
import com.jml.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService detailService;

    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService bookService;

    @Autowired
    private HttpServletRequest request;


    @GetMapping("/page")
    public Result<Page> page(int page,int pageSize,String number, String beginTime, String endTime){
        log.info(beginTime);
        log.info(endTime);
        Page<Orders> o=new Page<>(page,pageSize);
        Page<OrdersDto> os=new Page<>();

        LambdaQueryWrapper<Orders> w=new LambdaQueryWrapper<>();
        w.like(StringUtils.isNotEmpty(number),Orders::getNumber,number);
        w.between(StringUtils.isNotEmpty(beginTime),Orders::getOrderTime,beginTime,endTime);

        ordersService.page(o,w);

        BeanUtils.copyProperties(o,os,"records");

        List<Orders> records= o.getRecords();
        List<OrdersDto> list= records.stream().map((item)->{
            OrdersDto dto=new OrdersDto();

            BeanUtils.copyProperties(item,dto);

            Long userid=item.getUserId();

            User user = userService.getById(userid);

            if (user!=null){
                dto.setUserName(user.getName());
            }

            Long addid=item.getAddressBookId();

            AddressBook addrss = bookService.getById(addid);
            if (addrss!=null){
                String Provin=addrss.getProvinceName()==null?"":addrss.getProvinceName();
                String City=addrss.getCityName()==null?"":addrss.getCityName();
                String District=addrss.getDistrictName()==null?"":addrss.getDistrictName();
                dto.setAddress(Provin+City+District+addrss.getDetail());
                if (addrss.getPhone()!=null){
                    dto.setPhone(addrss.getPhone());
                }
                if (addrss.getConsignee()!=null){
                    dto.setConsignee(addrss.getConsignee());
                }
            }

            return dto;
        }).collect(Collectors.toList());

        os.setRecords(list);
        return Result.success(os);
    }

    @PutMapping
    public Result<String> UpdateState(@RequestBody Orders orders){
        Orders byId = ordersService.getById(orders.getId());
        byId.setStatus(orders.getStatus());
        ordersService.updateById(byId);
        return Result.success("修改成功");
    }

    @GetMapping("/userPage")
    public Result<Page> userPage(int page,int pageSize){
        Page<Orders> o=new Page<>(page,pageSize);
        Page<OrdersDto> os=new Page<>();


        Long id = null;
        if (request.getSession().getAttribute("user") != null) {
            id = (Long) request.getSession().getAttribute("user");
        }

        LambdaQueryWrapper<Orders> w=new LambdaQueryWrapper<>();
        w.eq(Orders::getUserId,id);
        w.orderByDesc(Orders::getOrderTime);

        ordersService.page(o,w);

        BeanUtils.copyProperties(o,os,"records");

        List<Orders> records= o.getRecords();
        List<OrdersDto> list= records.stream().map((item)->{
            OrdersDto dto=new OrdersDto();

            BeanUtils.copyProperties(item,dto);

            Long userid=item.getUserId();

            User user = userService.getById(userid);

            if (user!=null){
                dto.setUserName(user.getName());
            }

            String orderid=item.getNumber();

            LambdaQueryWrapper<OrderDetail> od=new LambdaQueryWrapper<>();
            od.eq(OrderDetail::getOrderId,orderid);

             List<OrderDetail> list1 = detailService.list(od);

             dto.setOrderDetails(list1);

            return dto;
        }).collect(Collectors.toList());

        os.setRecords(list);
        return Result.success(os);
    }

    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders dto){
        ordersService.submit(dto);
        return Result.success("添加成功");
    }

    @PostMapping("/again")
    public Result<String> again(@RequestBody OrdersDto dto  ){

        log.info(dto.toString());

        LambdaQueryWrapper<Orders> od=new LambdaQueryWrapper<>();
        od.eq(Orders::getId,dto.getId());

         Orders one = ordersService.getOne(od);

         LambdaQueryWrapper<OrderDetail> ode=new LambdaQueryWrapper<>();
         ode.eq(OrderDetail::getOrderId,one.getNumber());
         List<OrderDetail> list = detailService.list(ode);

        List<ShoppingCart> shoppingCartList=list.stream().map((item)->{
             ShoppingCart cart=new ShoppingCart();
             cart.setCreateTime(LocalDateTime.now());
             cart.setNumber(item.getNumber());
             cart.setUserId(one.getUserId());
             cart.setAmount(item.getAmount());
             cart.setDishFlavor(item.getDishFlavor());
             cart.setDishId(item.getDishId());
             cart.setName(item.getName());
             cart.setImage(item.getImage());

             return cart;
         }).collect(Collectors.toList());

        cartService.saveBatch(shoppingCartList);

        return Result.success("添加成功");
    }
}
