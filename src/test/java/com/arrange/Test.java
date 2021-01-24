package com.arrange;

import com.arrange.utils.JwtUtill;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;

//@SpringBootTest
public class Test {
//    @Autowired
//    JwtUtill jwtUtill;
    @org.junit.jupiter.api.Test
    public void fun() throws UnsupportedEncodingException {
        String s  = "�����ѧ�ۺ�ƽ̨-���������ѧ";
        System.out.println(new String(s.getBytes("gbk")));
    }
}
