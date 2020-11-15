package com.arrange;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ArrangeApplicationTests {

    @Test
    void contextLoads() {
        String string = "1234";
        System.out.println(string.getBytes());
    }

}
