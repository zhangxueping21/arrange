package com.arrange.controller;

import com.alibaba.druid.util.StringUtils;
import com.arrange.pojo.po.*;
import com.arrange.pojo.vo.Response;
import com.arrange.pojo.vo.ResponseMsg;
import com.arrange.service.ActiveService;
import com.arrange.service.InvitationService;
import com.arrange.service.UserService;
import com.arrange.utils.JwtUtill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动的controller
 */
@Transactional
@RestController
public class ActiveController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActiveService activeService;
    @Autowired
    private JwtUtill jwtUtill;
    @Autowired
    private InvitationService invitationService;

    @PostMapping("/publishActive")
    public Response publishActive(HttpServletRequest request, CreateActive createActive){
        String stuNumber = (String) request.getAttribute("stuNumber");
        if(!StringUtils.isEmpty(stuNumber)){
            List<User> users = userService.getByStuNumber(stuNumber);
            if(users != null && users.size() >0){
                LocalDate startTime = LocalDate.parse(createActive.getStartTime(), DateTimeFormatter.ofPattern("yyyy-M-d"));
                LocalDate endTime = LocalDate.parse(createActive.getEndTime(), DateTimeFormatter.ofPattern("yyyy-M-d"));
                Active active = new Active(0,users.get(0).getId()
                        ,createActive.getName(),createActive.getUnit()
                        ,startTime,endTime
                        ,createActive.getNum(),createActive.getPosition()
                        ,createActive.getRemarks(),0,""
                        ,LocalDateTime.now(), LocalDateTime.now());
                String token = jwtUtill.updateJwt(stuNumber);
                activeService.saveOrUpdate(active);
                active.setId(activeService.count());
                sendInvitation(active);
                return new Response().success(token);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }

    private void sendInvitation(Active active){
        int id = activeService.getMaxId();
        List<User> users = userService.getByUnit(Integer.toString(active.getUnit()));
        if(users != null && users.size()>0){
            String[] names = new String[users.size()];
            for (int i = 0;i<users.size();i++) {
                Invitation invitation = new Invitation(0,users.get(i).getId(),id,0,"",LocalDateTime.now(),LocalDateTime.now());
                invitationService.saveOrUpdate(invitation);
                names[i] = users.get(i).getName();
            }
        }
    }
    /**
     * 获取用户发布的，已排班的活动
     * @param request
     * @return
     */
    @GetMapping("/getPublishedActives1")
    public Response getPublishedActives1(HttpServletRequest request) throws JsonProcessingException {
        String stuNumber = (String) request.getAttribute("stuNumber");
        Map<String,Object> resultMap = new HashMap<>();
        if(!StringUtils.isEmpty(stuNumber)) {
            List<User> users = userService.getByStuNumber(stuNumber);
            if(users != null && users.size()>0) {
                Integer userId = users.get(0).getId();
                List<Active> actives = activeService.listByUserId(userId);
                List<Active> actives1 = new ArrayList<>();
                for(Active active:actives){
                    if(active.getState() == 1)
                        actives1.add(active);
                }
                resultMap.put("actives1",actives1);
                String token = jwtUtill.updateJwt(stuNumber);
                resultMap.put("token",token);
                ObjectMapper mapper = new ObjectMapper();
                String responseJson = mapper.writeValueAsString(resultMap);
                return new Response().success(responseJson);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }

    /**
     * 获取用户发布的，未排班的活动，包括响应情况
     * @param request
     * @return
     */
    @GetMapping("/getPublishedActives0")
    public Response getPublishedActives0(HttpServletRequest request) throws JsonProcessingException {
        String stuNumber = (String) request.getAttribute("stuNumber");
        Map<String,Object> resultMap = new HashMap<>();
        if(!StringUtils.isEmpty(stuNumber)) {
            List<User> users = userService.getByStuNumber(stuNumber);
            if(users != null && users.size()>0) {
                Integer userId = users.get(0).getId();
                List<Active> actives = activeService.listByUserId(userId);
                List<Active> actives0 = new ArrayList<>();
                List<String> agreeName = new ArrayList<>();
                List<String> rejectName = new ArrayList<>();
                List<String> unSee = new ArrayList<>();
                for(Active active:actives){
                    if(active.getState() == 0){
                        List<Invitation> invitations = invitationService.getByActiveId(active.getId());
                        for(Invitation invitation :invitations){
                            int state = invitation.getState();
                            if(state == 0 || state == 1){
                                unSee.add(userService.getById(invitation.getUserId()).getName());
                            }else if(state == 10){
                                rejectName.add(userService.getById(invitation.getUserId()).getName());
                            }else if(state == 11){
                                agreeName.add(userService.getById(invitation.getUserId()).getName());
                            }
                        }
                        String result = "";
                        for(String name:agreeName)
                            result += name+" ";
                        result += agreeName.size()+"人已同意\n";
                        for(String name:unSee)
                            result += name+" ";
                        result += unSee.size()+"人未查看\n";
                        for(String name:rejectName)
                            result += name+" ";
                        result += rejectName.size()+"人已拒绝";
                        active.setResult(result);
                        actives0.add(active);
                    }
                }
                resultMap.put("actives0",actives0);
                String token = jwtUtill.updateJwt(stuNumber);
                resultMap.put("token",token);
                ObjectMapper mapper = new ObjectMapper();
                String responseJson = mapper.writeValueAsString(resultMap);
                return new Response().success(responseJson);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }

    /**
     * 获取用户参加的活动
     * @param request
     * @return
     */
    @GetMapping("/getJoinActive")
    public Response getJoinActive(HttpServletRequest request) throws JsonProcessingException {
        String stuNumber = (String) request.getAttribute("stuNumber");
        Map<String,Object> resultMap = new HashMap<>();
        if(!StringUtils.isEmpty(stuNumber)){
            List<User> users = userService.getByStuNumber(stuNumber);
            if(users != null && users.size()>0) {
                List<Active> actives = new ArrayList<>();
                Integer userId = users.get(0).getId();
                List<Invitation> invitations = invitationService.listByUserId(userId);
                for(Invitation invitation:invitations){
                    Active active = activeService.getById(invitation.getActiveId());
                    int state = active.getState();
                    if(state == 1)
                        active.setResult(invitation.getTime());
                    actives.add(active);
                }
                String token = jwtUtill.updateJwt(stuNumber);
                resultMap.put("token",token);
                resultMap.put("actives",actives);
                ObjectMapper mapper = new ObjectMapper();
                String responseJson = mapper.writeValueAsString(resultMap);
                return new Response().success(responseJson);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }

    @DeleteMapping("/deleteActive")
    public Response deleteActive(HttpServletRequest request,Integer id){
        String stuNumber = (String) request.getAttribute("stuNumber");
        if(!StringUtils.isEmpty(stuNumber)){
            invitationService.removeByActiveId(id);
            activeService.removeById(id);
            String token = jwtUtill.createJwt(stuNumber);
            return new Response().success(token);
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }
}
