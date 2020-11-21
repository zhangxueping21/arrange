package com.arrange.controller;

import com.alibaba.druid.util.StringUtils;
import com.arrange.pojo.po.Active;
import com.arrange.pojo.po.ActiveMsg;
import com.arrange.pojo.po.Invitation;
import com.arrange.pojo.po.User;
import com.arrange.pojo.vo.Response;
import com.arrange.pojo.vo.ResponseMsg;
import com.arrange.service.ActiveService;
import com.arrange.service.InvitationService;
import com.arrange.service.UserService;
import com.arrange.utils.JwtUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邀请信息的controller
 */
@RestController
@Transactional
public class InvitationController {
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private JwtUtill jwtUtill;
    @Autowired
    private UserService userService;
    @Autowired
    private ActiveService activeService;


    /**
     * 获取被邀请的活动的信息，弹窗显示时用
     * @param request
     * @return
     */
    @GetMapping("/getInvitedMsg")
    public Response getInvitedMsg(HttpServletRequest request)  {
        String stuNumber = (String) request.getAttribute("stuNumber");
        Map<String,Object> resultMap = new HashMap<>();
        List<User> users = userService.getByStuNumber(stuNumber);
        List<ActiveMsg> activesMsg = new ArrayList<>();
        if(!StringUtils.isEmpty(stuNumber)){
            if(users != null && users.size()>0) {
                List<Invitation> invitations = invitationService.listByUserId(users.get(0).getId());
                for(Invitation invitation:invitations){
                    if(invitation.getState() == 0){
                        Active active = activeService.getById(invitation.getActiveId());
                        String startTime = active.getStartTime().format( DateTimeFormatter.ofPattern("yyyy-M-d"));
                        String endTime = active.getEndTime().format( DateTimeFormatter.ofPattern("yyyy-M-d"));
                        ActiveMsg activeMsg = new ActiveMsg(active.getId()
                                ,active.getCreateUser(),active.getName(),active.getUnit()
                                ,startTime,endTime,active.getNum(),active.getPosition()
                                ,active.getRemarks(),active.getState(),active.getResult());
                        activesMsg.add(activeMsg);
                        invitation.setState(1);
                        invitationService.saveOrUpdate(invitation);
                    }
                }
                String token = jwtUtill.updateJwt(stuNumber);
                resultMap.put("token",token);
                resultMap.put("activesMsg",activesMsg);
                return new Response().success(resultMap);
            }
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }

    /**
     * 响应邀请，接受或拒绝
     * @param request
     * @param state
     * @param activeId
     * @return
     */
    @PostMapping("/responseInvitation")
    public Response responseInvitation(HttpServletRequest request,int state,int activeId){
        String stuNumber = (String) request.getAttribute("stuNumber");
        List<User> users = userService.getByStuNumber(stuNumber);
        if(!StringUtils.isEmpty(stuNumber)){
            if(users != null && users.size()>0) {
                Invitation invitation = invitationService.getByActiveIdAndUserId(activeId,users.get(0).getId());
                invitation.setState(state);
                invitationService.saveOrUpdate(invitation);
                String token = jwtUtill.updateJwt(stuNumber);
                return new Response().success(token);
            }
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }
}
