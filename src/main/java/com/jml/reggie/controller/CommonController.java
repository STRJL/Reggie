package com.jml.reggie.controller;

import com.jml.reggie.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {


    @Value("${reggie.path}")
    private String basePath;


    /*
    * 上传项目
    * */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){

        String name = file.getOriginalFilename();

        log.info(name);

        String sname = name.substring(name.lastIndexOf("."));


        //使用UUID随机生成文件名
        String uname = UUID.randomUUID().toString()+sname;

        //创建一个新目录
        File desfile=new File(basePath);

        if (!desfile.exists()){
            desfile.mkdirs();
        }

        try{
            file.transferTo(new File(basePath+uname));
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.success(uname) ;
    }


    /*
    * 下载项目
    * */

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws Exception {
        FileInputStream inputStream=new FileInputStream(new File(basePath+name));

        //输出流
        ServletOutputStream outputStream = response.getOutputStream();

        response.setContentType("image/jpeg");

        int len=0;
        byte[] bye=new byte[1024];
        while ((len=inputStream.read(bye))!=-1){
            outputStream.write(bye,0,len);
            outputStream.flush();
        }

        outputStream.close();
        inputStream.close();


    }
}
