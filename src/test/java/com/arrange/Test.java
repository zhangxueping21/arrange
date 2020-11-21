package com.arrange;

import com.arrange.utils.JwtUtill;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Test {
    @Autowired
    JwtUtill jwtUtill;
    @org.junit.jupiter.api.Test
    public void fun() {
        System.out.println(jwtUtill.createJwt(""));

    }
}
