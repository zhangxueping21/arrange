package com.arrange.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class TestController {
    @GetMapping("/testCors")
    public String testCors(){
        return "helloWord!";
    }
}
