package com.jml.reggie;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class Tesdd {

    public static void main(String[] args) {
//        Product product = new Product("101", "wahhah", 323.0);
//    1、连接Redis（导入jar包）
        Jedis jedis = new Jedis("127.0.0.1", 6379);
//    2、将对象存储在redis中
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",1);
        jsonObject.put("name","的的");
//        String set = jedis.set("jml:user:2",jsonObject.toJSONString());
        Map<String, String> stringStringMap = jedis.hgetAll("jml:user:3");
        stringStringMap.forEach((item,k)->{
            System.out.printf("%s:%s\n",item,k);
        });
//        关闭redis
        jedis.close();


    }


}
