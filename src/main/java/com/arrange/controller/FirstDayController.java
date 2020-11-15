package com.arrange.controller;

import com.alibaba.druid.util.StringUtils;
import com.arrange.pojo.po.timetable.FirstDay;
import com.arrange.pojo.vo.Response;
import com.arrange.pojo.vo.ResponseMsg;
import com.arrange.service.FirstDayService;
import com.arrange.utils.JwtUtill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@Transactional
public class FirstDayController {
    @Autowired
    private FirstDayService firstDayService;
    @Autowired
    private JwtUtill jwtUtill;

    @PutMapping("/setFirstDay")
    public Response setFirstDay(HttpServletRequest request,int year, int month, int day){
        String stuNumber = (String) request.getAttribute("stuNumber");
        if(!StringUtils.isEmpty(stuNumber)){
            FirstDay firstDay = new FirstDay(0,year,month,day);
            firstDayService.saveOrUpdate(firstDay);
            String token = jwtUtill.createJwt(stuNumber);
            return new Response().success(token);
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }
    @GetMapping("/getFirstDay")
    public Response getFirstDay(HttpServletRequest request) throws JsonProcessingException {
        String stuNumber = (String) request.getAttribute("stuNumber");
        if(!StringUtils.isEmpty(stuNumber)){
            Integer id = firstDayService.getMaxId();
            if(id != null){
                FirstDay firstDay = firstDayService.getById(id);
                String token = jwtUtill.createJwt(stuNumber);
                Map<String,Object> resultMap = new HashMap<>();
                resultMap.put("firstDay",firstDay);
                resultMap.put("token",token);
                ObjectMapper mapper = new ObjectMapper();
                String responseJson = mapper.writeValueAsString(resultMap);
                return new Response().success(responseJson);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }
}
