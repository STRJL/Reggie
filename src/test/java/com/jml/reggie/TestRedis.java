package com.jml.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    void testString() {
        // 写入一条String数据
        redisTemplate.opsForValue().set("ss", "是的给我");
        // 获取string数据
        Object name = redisTemplate.opsForValue().get("ss");
        System.out.println("name = " + name);
    }

    @Test
    void testList() {
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPushAll("jml:mylist:1", "3", "2", "1");
        listOperations.rightPushAll("jml:mylist:1", "4", "5", "6");

        List<String> range = listOperations.range("jml:mylist:1", 0, 5);
        for (String s : range) {
            System.out.println(s);
        }
    }

    @Test
    void testSet() {
        SetOperations setOperations = redisTemplate.opsForSet();

        setOperations.add("jml:mylist:2", "c", "a", "b");

        Set<String> members = setOperations.members("jml:mylist:2");
        for (String member : members) {
            System.out.println(member);
        }
        setOperations.add("jml:mylist:3", "2", "a", "b");
        Set<String> intersect = setOperations.intersect("jml:mylist:2", "jml:mylist:3");
        for (String member : intersect) {
            System.out.println(member);
        }

    }
}
