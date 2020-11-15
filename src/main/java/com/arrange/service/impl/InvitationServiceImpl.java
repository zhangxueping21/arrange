package com.arrange.service.impl;

import com.arrange.dao.mapper.InvitationMapper;
import com.arrange.pojo.po.Invitation;
import com.arrange.pojo.po.User;
import com.arrange.service.InvitationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationServiceImpl extends ServiceImpl<InvitationMapper, Invitation> implements InvitationService {
    @Autowired
    private InvitationMapper invitationMapper;

    @Override
    public void settle() {
        invitationMapper.settle();
    }

    @Override
    public List<User> getUsers(int activeId) {
        return invitationMapper.getUsers(activeId);
    }

    @Override
    public Invitation getByActiveIdAndUserId(Integer activeId, Integer userId) {
        List<Invitation> invitations =
                invitationMapper.getByActiveIdAndUserId(activeId,userId);
        if(invitations != null && invitations.size() > 0)
            return invitations.get(0);
        return null;
    }
}
