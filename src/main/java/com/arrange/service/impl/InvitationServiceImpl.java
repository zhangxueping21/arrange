package com.arrange.service.impl;

import com.arrange.dao.mapper.InvitationMapper;
import com.arrange.pojo.po.Invitation;
import com.arrange.service.InvitationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationServiceImpl extends ServiceImpl<InvitationMapper, Invitation> implements InvitationService {
    @Autowired
    private InvitationMapper invitationMapper;

    @Override
    public void settle() {
        invitationMapper.settle();
    }
}
