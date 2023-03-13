package com.jml.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jml.reggie.common.Result;
import com.jml.reggie.entity.ShoppingCart;
import com.jml.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        Long id=null;
        if (request.getSession().getAttribute("user")!=null){
            id= (Long)request.getSession().getAttribute("user");
        }

        LambdaQueryWrapper<ShoppingCart> s=new LambdaQueryWrapper<>();
        s.eq(ShoppingCart::getUserId,id);
//        s.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = cartService.list(s);
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart cart){
        log.info(cart.toString());
        Long id=null;
        if (request.getSession().getAttribute("user")!=null){
            id= (Long)request.getSession().getAttribute("user");
        }

        cart.setUserId(id);
        cart.setCreateTime(LocalDateTime.now());
        Long dishid=cart.getDishId();

        LambdaQueryWrapper<ShoppingCart> s=new LambdaQueryWrapper<>();
        s.eq(ShoppingCart::getUserId,id);

        if (dishid!=null){
            s.eq(ShoppingCart::getDishId,dishid);
        }
        else {
            s.eq(ShoppingCart::getSetmealId,cart.getSetmealId());
        }
//        s.eq(ShoppingCart::getDishFlavor,cart.getDishFlavor());

        ShoppingCart one = cartService.getOne(s);

        if (one!=null){
            one.setNumber(one.getNumber()+1);
            cartService.updateById(one);
        }
        else{
            cart.setNumber(1);
            cartService.save(cart);
            one=cart;
        }
        return Result.success(one);
    }

    @PostMapping("/sub")
    public Result<ShoppingCart> sub(@RequestBody ShoppingCart cart){
        Long id=null;
        if (request.getSession().getAttribute("user")!=null){
            id= (Long)request.getSession().getAttribute("user");
        }


        Long dishid=cart.getDishId();

        LambdaQueryWrapper<ShoppingCart> s=new LambdaQueryWrapper<>();
        s.eq(ShoppingCart::getUserId,id);

        if (dishid!=null){
            s.eq(ShoppingCart::getDishId,dishid);
        }
        else {
            s.eq(ShoppingCart::getSetmealId,cart.getSetmealId());
        }
//        s.eq(ShoppingCart::getDishFlavor,cart.getDishFlavor());

        ShoppingCart one = cartService.getOne(s);
        one.setCreateTime(LocalDateTime.now());
        if (one.getNumber()>1){
            one.setNumber(one.getNumber()-1);
            cartService.updateById(one);
        }
        else{
            cartService.removeById(one.getId());
            one=cart;
        }
        return Result.success(one);
    }


    @DeleteMapping("/clean")
    public Result<ShoppingCart> clean(){
        Long id=null;
        if (request.getSession().getAttribute("user")!=null){
            id= (Long)request.getSession().getAttribute("user");
        }


        LambdaQueryWrapper<ShoppingCart> s=new LambdaQueryWrapper<>();
        s.eq(ShoppingCart::getUserId,id);

        ShoppingCart one = cartService.getOne(s);

        cartService.removeById(one.getId());
        return Result.success(one);
    }
}
