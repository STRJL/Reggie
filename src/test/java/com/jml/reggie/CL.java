package com.jml.reggie;

import com.jml.reggie.common.JedisConnectionFacotry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class CL {

    private Jedis jedis;

    @BeforeEach
    public void   sl(){
//        jedis= new Jedis("127.0.0.1", 6379);
        jedis=JedisConnectionFacotry.getJedis();
        jedis.auth("123456");

        jedis.select(0);
    }

    @Test
    void textstring(){
         String kll = jedis.set("kll", "6666");
        System.out.println(kll);

        String klll = jedis.get("kll");
        System.out.println(klll);
    }

    @Test
    void HashSEt(){
         jedis.hset("jml:user:4", "id", "4");
         jedis.hset("jml:user:4", "name", "您好");


         Map<String, String> stringStringMap = jedis.hgetAll("jml:user:4");
        System.out.println(stringStringMap);

    }

    @AfterEach
    void dertera(){
        if (jedis!=null){
            jedis.close();
        }
    }
}
