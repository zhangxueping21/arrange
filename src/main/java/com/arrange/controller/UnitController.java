package com.arrange.controller;

import com.arrange.pojo.vo.Response;
import com.arrange.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户/活动单位的controller
 */
@RestController
public class UnitController {
    @Autowired
    private UnitService unitService;

    @PutMapping("/settleUnit")
    public Response settle(){
        unitService.settle();
        return new Response().success();
    }
}
