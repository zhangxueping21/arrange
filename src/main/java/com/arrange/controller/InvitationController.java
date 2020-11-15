package com.arrange.controller;

import com.arrange.service.ActiveService;
import com.arrange.service.InvitationService;
import com.arrange.service.UserService;
import com.arrange.utils.JwtUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邀请信息的controller
 */
@RestController
public class InvitationController {
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private JwtUtill jwtUtill;
    @Autowired
    private UserService userService;
    @Autowired
    private ActiveService activeService;

    //获取被邀请的新信息

    //响应邀请，接受或拒绝
}
