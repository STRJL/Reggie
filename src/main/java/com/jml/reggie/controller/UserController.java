package com.jml.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jml.reggie.common.BeanContex;
import com.jml.reggie.common.Result;
import com.jml.reggie.entity.Employee;
import com.jml.reggie.entity.User;
import com.jml.reggie.service.UserService;
import com.jml.reggie.utils.SMSUtils;
import com.jml.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone=user.getPhone();

        if (StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            log.info("code="+code);
            SMSUtils.sendMessage("ruiji","SMS_265475182",phone,code);

            session.setAttribute(phone,code);
            session.setMaxInactiveInterval(5*60);
            return Result.success("手机验证码发送成功");

        }

        return Result.error("短信发送失败");
    }

    @PostMapping("/loginout")
    public Result<String> loginout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpSession session, HttpServletRequest request){
        String phone=map.get("phone").toString();

        String code=map.get("code").toString();

        Object codes=session.getAttribute(phone);

        if (codes!=null&&codes.equals(code)){
            LambdaQueryWrapper<User> u=new LambdaQueryWrapper<>();
            u.eq(User::getPhone,phone);

            User one = userService.getOne(u);

            if (one==null){
                one=new User();
                one.setPhone(phone);
                one.setStatus(1);
                userService.save(one);
            }
//            session.setAttribute("user",one.getId());

            request.getSession().setAttribute("user",one.getId());

            return Result.success(one);
        }

        return Result.error("登录失败");
    }
}
