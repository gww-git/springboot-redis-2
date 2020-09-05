package com.sz.springbootredis2;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
@ConfigurationProperties("spring.redis")
public class Test {

    String port="";
    String host="";

    @Autowired
    private RedisTemplate redisTemplate;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @org.junit.Test
    public void test1(){
        Jedis jedis=new Jedis(host,Integer.parseInt(port));
        jedis.set("name", "xxx");
        System.out.println(jedis.get("name"));
//        System.out.println(jedis.get);

    }

    @org.junit.Test
    public void testString(){
        //1.值类型； String
        redisTemplate.boundValueOps("username").set("我叫Redis");
        redisTemplate.boundValueOps("username").append("Hello!");
        String username = (String) redisTemplate.boundValueOps("username").get();
        System.out.println("username = " + username);
        redisTemplate.boundValueOps("code").set("123456",3,TimeUnit.SECONDS); //3秒后过期；
    }

    @org.junit.Test
    public void testList(){
        //插入数据；
        redisTemplate.boundListOps("list1").leftPushAll("aaaa","bbbb","cccc");
        List list = redisTemplate.boundListOps("list1").range(0,-1);
        list.forEach(s->{    //lamda表达式，仅支持jdk1.8 +
            System.out.println(s);
        });

        System.out.println("===================================");
        List list2 = new ArrayList();
        list2.add("aa");
        list2.add("bb");
        list2.add("cc");
        list2.add("dd");
        redisTemplate.boundListOps("list2").leftPush(list2);
        List list3 =  redisTemplate.boundListOps("list2").range(0,-1);
        System.out.println(list3.toString());
    }

    @org.junit.Test
    public void testMap(){
        redisTemplate.boundHashOps("m1").put("name","admin");
        redisTemplate.boundHashOps("m1").put("age","30");
        redisTemplate.boundHashOps("m1").put("sex","男");
        Set keys = redisTemplate.boundHashOps("m1").keys();
        System.out.println("-------------------------------------");
        redisTemplate.boundHashOps("m1").delete("age","sex");
        for (Object key : keys) {
            System.out.println(key.toString());
        }
        List values = redisTemplate.boundHashOps("m1").values();
        for (Object value : values) {
            System.out.println(value.toString());
        }
    }


    @org.junit.Test
    public void deletes(){
        Boolean username = redisTemplate.delete("username");
        System.out.println(username);
        Boolean list1 = redisTemplate.delete("list1");
        System.out.println(list1);
        Boolean list2 = redisTemplate.delete("list2");
        System.out.println(list2);
        Boolean m1 = redisTemplate.delete("m1");
        System.out.println(m1);
    }

    @org.junit.Test
    public void testSet(){
        redisTemplate.boundSetOps("s1").add("abc","def","ghi","jkl","mno");
        Set set1 = redisTemplate.boundSetOps("s1").members();
        for (Object o : set1) {
            System.out.println(o.toString());
        }
    }

    @org.junit.Test
    public void testZSet(){
        redisTemplate.boundZSetOps("z1").add("tom",30);
        redisTemplate.boundZSetOps("z1").add("jack",99);
        redisTemplate.boundZSetOps("z1").add("rose",70);
        redisTemplate.boundZSetOps("z1").add("lucy",80);
        redisTemplate.boundZSetOps("z1").add("jerry",60);
        Set z1 = redisTemplate.boundZSetOps("z1").range(0, -1);//默认升序排序！
        System.out.println(z1);
    }



}
