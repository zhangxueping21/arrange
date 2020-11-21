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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
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
    public Response getUnit(HttpServletRequest request)  {
        String stuNumber = (String) request.getAttribute("stuNumber");
        Map<String,Object> resultMap = new HashMap<>();
        if(!StringUtils.isEmpty(stuNumber)) {
            List<User> users = userService.getByStuNumber(stuNumber);
            String unit = users.get(0).getUnit();
            String[] units = unit.split(" ");
            Map<Integer,String> unitMap = new HashMap<>();
            for(int i = 0;i<units.length;i++){
                int id = Integer.parseInt(units[i]);
                Unit aUnit = unitService.getById(id);
                if(aUnit != null)
                    unitMap.put(id,aUnit.getUnitName());
            }
            String token = jwtUtill.updateJwt(stuNumber);
            resultMap.put("units",unitMap);
            resultMap.put("token",token);
            return new Response().success(resultMap);
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }

    /**
     * 获取全部单位
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/getAllUnits")
    public Response getAllUnit(HttpServletRequest request)  {
        String stuNumber = (String) request.getAttribute("stuNumber");
        Map<String,Object> resultMap = new HashMap<>();
        if(!StringUtils.isEmpty(stuNumber)) {
            List<Unit> units = unitService.list();
            Map<Integer,String> unitMap = new HashMap<>();
            for(Unit unit:units){
                unitMap.put(unit.getId(),unit.getUnitName());
            }
            String token = jwtUtill.updateJwt(stuNumber);
            resultMap.put("units",unitMap);
            resultMap.put("token",token);
            return new Response().success(resultMap);
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
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
                unit = user.getUnit()+" "+unit;
                user.setUnit(unit);
                userService.saveOrUpdate(user);
                String token = jwtUtill.updateJwt(stuNumber);
                return new Response().success(token);
            }
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }
}
