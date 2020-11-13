package com.arrange.controller;

import com.arrange.pojo.po.timetable.FirstDay;
import com.arrange.pojo.vo.Response;
import com.arrange.pojo.vo.ResponseMsg;
import com.arrange.service.FirstDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    @PutMapping("/setFirstDay")
    public Response setFirstDay(int year,int month,int day){
        FirstDay firstDay = new FirstDay(0,year,month,day);
        firstDayService.saveOrUpdate(firstDay);
        return new Response().success();
    }
    @GetMapping("/settleFirstDay")
    public Response getFirstDay(){
        Integer id = firstDayService.getMaxId();
        if(id != null){
            FirstDay firstDay = firstDayService.getById(id);
            return new Response().success(firstDay);
        }
        return new Response(ResponseMsg.NO_TARGET);
    }
}
