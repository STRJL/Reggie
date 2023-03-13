package com.jml.reggie.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
public class MyMeteObjectHandler implements MetaObjectHandler {

    @Autowired
    private HttpServletRequest request;

    @Override
    public void insertFill(MetaObject metaObject) {

        metaObject.setValue("createTime",LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());

        Long id = null;
        if (request.getSession().getAttribute("user")!=null){
            id= (Long)request.getSession().getAttribute("user");
        }
        if (request.getSession().getAttribute("employee")!=null){
            id= (Long)request.getSession().getAttribute("employee");
        }


        metaObject.setValue("createUser",id);
        metaObject.setValue("updateUser",id);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",LocalDateTime.now());
        Long id= null;
        if (request.getSession().getAttribute("user")!=null){
            id= (Long)request.getSession().getAttribute("user");
        }
        if (request.getSession().getAttribute("employee")!=null){
            id= (Long)request.getSession().getAttribute("employee");
        }
        metaObject.setValue("updateUser",id);
    }
}
