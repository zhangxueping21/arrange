package com.arrange.controller;

import com.alibaba.druid.util.StringUtils;
import com.arrange.pojo.po.Unit;
import com.arrange.pojo.po.User;
import com.arrange.pojo.vo.Response;
import com.arrange.pojo.vo.ResponseMsg;
import com.arrange.service.UnitService;
import com.arrange.service.UserService;
import com.arrange.utils.JwtUtill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户/活动单位的controller
 */
@RestController
@Transactional
public class UnitController {
    @Autowired
    private UnitService unitService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtill jwtUtill;
    @GetMapping("/getUnit")
    public Response getUnit(HttpServletRequest request) throws JsonProcessingException {
        String stuNumber = (String) request.getAttribute("stuNumber");
        Map<String,Object> resultMap = new HashMap<>();
        if(!StringUtils.isEmpty(stuNumber)) {
            List<User> users = userService.getByStuNumber(stuNumber);
            String unit = users.get(0).getUnit();
            char[] units = unit.toCharArray();
            List<Unit> unitList = new ArrayList<>();
            for(int i = 0;i<units.length;i++){
                int id = units[i]-'0';
                Unit aUnit = unitService.getById(id);
                unitList.add(aUnit);
            }
            String token = jwtUtill.updateJwt(stuNumber);
            resultMap.put("units",unitList);
            resultMap.put("token",token);
            ObjectMapper mapper = new ObjectMapper();
            String responseJson = mapper.writeValueAsString(resultMap);
            return new Response().success(responseJson);
        }
        return new Response(ResponseMsg.NO_TARGET);
    }
    @PutMapping("/setUnit")
    public Response setUnit(HttpServletRequest request,String unit){
        String stuNumber = (String) request.getAttribute("stuNumber");
        if(!StringUtils.isEmpty(stuNumber)) {
            List<User> users = userService.getByStuNumber(stuNumber);
            if(users != null && users.size()>0){
                User user = users.get(0);
                user.setUnit(unit);
                userService.saveOrUpdate(user);
                String token = jwtUtill.updateJwt(stuNumber);
                return new Response().success(token);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }
    @PutMapping("/addUnit")
    public Response addUnit(HttpServletRequest request,String unit){
        String stuNumber = (String) request.getAttribute("stuNumber");
        if(!StringUtils.isEmpty(stuNumber)) {
            List<User> users = userService.getByStuNumber(stuNumber);
            if(users != null && users.size()>0){
                User user = users.get(0);
                user.setUnit(user.getUnit()+unit);
                userService.saveOrUpdate(user);
                String token = jwtUtill.updateJwt(stuNumber);
                return new Response().success(token);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }

}
