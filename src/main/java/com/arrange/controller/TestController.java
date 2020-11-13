package com.arrange.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/testCors")
    public String testCors(){
        return "helloWord!";
    }
}
