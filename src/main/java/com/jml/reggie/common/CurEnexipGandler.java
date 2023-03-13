package com.jml.reggie.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class CurEnexipGandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exipHandler(SQLIntegrityConstraintViolationException ex){
        log.info(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            if (ex.getMessage().contains("category")){
                return Result.error("您输入的分类名称已存在");
            }
            if (ex.getMessage().contains("employee")){
                return Result.error("您输入的账号已存在");
            }

        }

        return Result.error("系统出现异常");
    }
}
