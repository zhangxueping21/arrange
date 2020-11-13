package com.arrange.controller;

import com.arrange.pojo.vo.Response;
import com.arrange.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邀请信息的controller
 */
@RestController
public class InvitationController {
    @Autowired
    private InvitationService invitationService;

    @PutMapping("/settleInvitation")
    public Response settle(){
        invitationService.settle();
        return new Response().success();
    }
}
