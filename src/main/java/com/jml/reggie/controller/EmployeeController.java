package com.jml.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jml.reggie.common.Result;
import com.jml.reggie.entity.Employee;
import com.jml.reggie.service.EmployeeService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.digester.Digester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //对前端传入的密码进行md5加密处理
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        //根据用户名查询数据
        LambdaQueryWrapper<Employee> la=new LambdaQueryWrapper<>();
        la.eq(Employee::getUsername,employee.getUsername());
        Employee em=employeeService.getOne(la);

        if(em==null){
            return Result.error("登录失败");
        }

        if (!em.getPassword().equals(password)){
            return Result.error("登录失败");
        }

        if(em.getStatus()==0){
            return Result.error("账号已禁用");
        }

        request.getSession().setAttribute("employee",em.getId());
        return Result.success(em);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    //员工数据查询
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize,String name){
        LambdaQueryWrapper<Employee> w=new LambdaQueryWrapper<>();
        w.like(StringUtils.isNotEmpty(name),Employee::getUsername,name);
        Page p=new Page(page,pageSize);
        Page ps = employeeService.page(p, w);
        return Result.success(ps);
    }

    //员工数据查询
    @GetMapping("/{id}")
    public Result<Employee> Geid(@PathVariable Long id){
        Employee one = employeeService.getById(id);
        return Result.success(one);
    }

    //员工数据添加
    @PostMapping
    public Result<String> save(HttpServletRequest request,@RequestBody Employee employee){
        System.out.println(employee);
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        Long id = (Long)request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(id);
//        employee.setUpdateUser(id);

        employeeService.save(employee);

        return Result.success("添加成功");
    }

    //员工数据添加
    @PutMapping
    public Result<String> update(HttpServletRequest request,@RequestBody Employee employee){


        log.info(employee.toString());
//        employee.setUpdateTime(LocalDateTime.now());

//        Long id = (Long)request.getSession().getAttribute("employee");
//
//        employee.setUpdateUser(id);

        employeeService.updateById(employee);

        return Result.success("修改成功");
    }
}
