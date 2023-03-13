package com.jml.reggie.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperMifig {

    @Bean
    public MybatisPlusInterceptor interceptor(){
        //定义
        MybatisPlusInterceptor interceptor=new MybatisPlusInterceptor();

        //添加具体的拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());

        return interceptor;
    }
}
