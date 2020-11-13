package com.arrange.controller;

import com.arrange.pojo.vo.Response;
import com.arrange.service.FirstDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstDayController {
    @Autowired
    private FirstDayService firstDayService;


    @PutMapping("/settleFirstDay")
    public Response settle(){
        firstDayService.settle();
        return new Response().success();
    }
}
