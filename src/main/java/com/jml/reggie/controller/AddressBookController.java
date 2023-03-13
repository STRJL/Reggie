package com.jml.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.jml.reggie.common.BeanContex;
import com.jml.reggie.common.Result;
import com.jml.reggie.dto.DishDto;
import com.jml.reggie.entity.AddressBook;
import com.jml.reggie.service.AddressBookService;
import com.jml.reggie.service.impl.AddressBookImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBook;

    @Autowired
    private HttpServletRequest request;


    @PostMapping
    private Result<String> save(@RequestBody AddressBook addressList){
        Long id=null;
        if (request.getSession().getAttribute("user")!=null){
            id= (Long)request.getSession().getAttribute("user");
        }
        addressList.setUserId(id);
        addressBook.save(addressList);
        return Result.success("添加成功");
    }

    @GetMapping("/list")
    private Result<List<AddressBook>> list(){
        Long id=null;
        if (request.getSession().getAttribute("user")!=null){
            id= (Long)request.getSession().getAttribute("user");
        }

        LambdaQueryWrapper<AddressBook> a=new LambdaQueryWrapper<>();
        a.eq(AddressBook::getUserId,id);

        List<AddressBook> list = addressBook.list(a);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    private Result<AddressBook> page(@PathVariable String id){
        AddressBook byId = addressBook.getById(id);
        return Result.success(byId);
    }

    /*
    * 修改地址
    * */
    @PutMapping
    public Result<String> updates(@RequestBody AddressBook adder){
        addressBook.updateById(adder);
        return Result.success("修改成功");
    }

    /*
    * 设置默认地址
    * */
    @PutMapping("/default")
    private  Result<String> update(@RequestBody AddressBook addressList){
        Long ids=null;
        if (request.getSession().getAttribute("user")!=null){
            ids= (Long)request.getSession().getAttribute("user");
        }
        LambdaUpdateWrapper<AddressBook> queryWrapper=new LambdaUpdateWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,ids);
        queryWrapper.set(AddressBook::getIsDefault,0);

        addressBook.update(queryWrapper);
        AddressBook byId = addressBook.getById(addressList.getId());
        byId.setIsDefault(1);
        addressBook.updateById(byId);

        return Result.success("修改成功");
    }


/*
* 查询默认地址
* */
    @GetMapping("/default")
    private Result<AddressBook> getDeault(){
        Long ids=null;
        if (request.getSession().getAttribute("user")!=null){
            ids= (Long)request.getSession().getAttribute("user");
        }
        LambdaQueryWrapper<AddressBook> d=new LambdaQueryWrapper<>();
        d.eq(AddressBook::getUserId,ids);
        d.eq(AddressBook::getIsDefault,1);

        AddressBook one = addressBook.getOne(d);
        if (one==null){
            return Result.error("你未设置收货地点");
        }
        else{
            return Result.success(one);
        }
    }
}
