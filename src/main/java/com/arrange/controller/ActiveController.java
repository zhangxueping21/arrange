package com.arrange.controller;

import com.alibaba.druid.util.StringUtils;
import com.arrange.pojo.po.*;
import com.arrange.pojo.vo.Response;
import com.arrange.pojo.vo.ResponseMsg;
import com.arrange.service.ActiveService;
import com.arrange.service.InvitationService;
import com.arrange.service.UserService;
import com.arrange.utils.JwtUtill;
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
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }

    private void sendInvitation(Active active){
        int id = activeService.getMaxId();
        List<User> users = userService.getByUnit(Integer.toString(active.getUnit()));
        users = filtration(users,active.getUnit());
        if(users != null && users.size()>0){
            String[] names = new String[users.size()];
            for (int i = 0;i<users.size();i++) {
                Invitation invitation = new Invitation(0,users.get(i).getId(),id,0,"",LocalDateTime.now(),LocalDateTime.now());
                invitationService.saveOrUpdate(invitation);
                names[i] = users.get(i).getName();
            }
        }
    }
    private List<User> filtration(List<User> users,int unit){
        List<User> users1 = new ArrayList<>();
        for(User user:users){
            String[] units = user.getUnit().split(" ");
            for(int i = 0;i<units.length;i++){
                if(Integer.parseInt(units[i])==unit){
                    users1.add(user);
                    break;
                }
            }
        }
        return users1;
    }

    /**
     * 获取用户发布的，已排班的活动
     * @param request
     * @return
     */
    @GetMapping("/getPublishedActives1")
    public Response getPublishedActives1(HttpServletRequest request)  {
        String stuNumber = (String) request.getAttribute("stuNumber");
        Map<String,Object> resultMap = new HashMap<>();

        if(!StringUtils.isEmpty(stuNumber)) {
            List<User> users = userService.getByStuNumber(stuNumber);
            if(users != null && users.size()>0) {
                Integer userId = users.get(0).getId();
                List<Active> actives = activeService.listByUserId(userId);
                List<ActiveMsg> activesMsg = new ArrayList<>();
                for(Active active:actives){
                    if(active.getState() == 1){
                        String startTime = active.getStartTime().format( DateTimeFormatter.ofPattern("yyyy-M-d"));
                        String endTime = active.getEndTime().format( DateTimeFormatter.ofPattern("yyyy-M-d"));
                        ActiveMsg activeMsg = new ActiveMsg(active.getId()
                                ,active.getCreateUser(),active.getName(),active.getUnit()
                                ,startTime,endTime,active.getNum(),active.getPosition()
                                ,active.getRemarks(),active.getState(),active.getResult());
                        activesMsg.add(activeMsg);
                    }
                }
                resultMap.put("actives1",activesMsg);
                String token = jwtUtill.updateJwt(stuNumber);
                resultMap.put("token",token);
                return new Response().success(resultMap);
            }
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }

    /**
     * 获取用户发布的，未排班的活动，包括响应情况
     * @param request
     * @return
     */
    @GetMapping("/getPublishedActives0")
    public Response getPublishedActives0(HttpServletRequest request) {
        String stuNumber = (String) request.getAttribute("stuNumber");
        Map<String,Object> resultMap = new HashMap<>();
        if(!StringUtils.isEmpty(stuNumber)) {
            List<User> users = userService.getByStuNumber(stuNumber);
            if(users != null && users.size()>0) {
                Integer userId = users.get(0).getId();
                List<Active> actives = activeService.listByUserId(userId);
                List<ActiveMsg> activesMsg = new ArrayList<>();

                String result ;
                for(Active active:actives){
                    if(active.getState() == 0){
                        List<String> agreeName = new ArrayList<>();
                        List<String> rejectName = new ArrayList<>();
                        List<String> unSee = new ArrayList<>();

                        String startTime = active.getStartTime().format( DateTimeFormatter.ofPattern("yyyy-M-d"));
                        String endTime = active.getEndTime().format( DateTimeFormatter.ofPattern("yyyy-M-d"));
                        ActiveMsg activeMsg = new ActiveMsg(active.getId()
                                ,active.getCreateUser(),active.getName(),active.getUnit()
                                ,startTime,endTime,active.getNum(),active.getPosition()
                                ,active.getRemarks(),active.getState(),active.getResult());
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
                        result = "";
                        for(String name:agreeName)
                            result += name+" ";
                        result += agreeName.size()+"人已同意\n";
                        for(String name:unSee)
                            result += name+" ";
                        result += unSee.size()+"人未查看\n";
                        for(String name:rejectName)
                            result += name+" ";
                        result += rejectName.size()+"人已拒绝";
                        activeMsg.setResult(result);
                        activesMsg.add(activeMsg);
                    }
                }
                resultMap.put("activesMsg",activesMsg);
                String token = jwtUtill.updateJwt(stuNumber);
                resultMap.put("token",token);
                return new Response().success(resultMap);
            }
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }

    /**
     * 获取用户参加的活动
     * @param request
     * @return
     */
    @GetMapping("/getJoinActive")
    public Response getJoinActive(HttpServletRequest request) {
        String stuNumber = (String) request.getAttribute("stuNumber");
        Map<String,Object> resultMap = new HashMap<>();
        if(!StringUtils.isEmpty(stuNumber)){
            List<User> users = userService.getByStuNumber(stuNumber);
            if(users != null && users.size()>0) {
                Integer userId = users.get(0).getId();
                List<Invitation> invitations = invitationService.listByUserId(userId);
                List<ActiveMsg> activesMsg = new ArrayList<>();
                for(Invitation invitation:invitations){
                    Active active = activeService.getById(invitation.getActiveId());
                    String startTime = active.getStartTime().format( DateTimeFormatter.ofPattern("yyyy-M-d"));
                    String endTime = active.getEndTime().format( DateTimeFormatter.ofPattern("yyyy-M-d"));
                    int state = invitation.getState();
                    ActiveMsg activeMsg = new ActiveMsg(active.getId()
                            ,active.getCreateUser(),active.getName(),active.getUnit()
                            ,startTime,endTime,active.getNum(),active.getPosition()
                            ,active.getRemarks(),state,active.getResult());

                    int activeState = active.getState();
                    if(activeState == 1)
                        activeMsg.setResult(invitation.getTime());
                    activesMsg.add(activeMsg);
                }
                String token = jwtUtill.updateJwt(stuNumber);
                resultMap.put("token",token);
                resultMap.put("activeMsg",activesMsg);
                return new Response().success(resultMap);
            }
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
