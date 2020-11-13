package com.arrange.service;

import com.arrange.pojo.po.Invitation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface InvitationService extends IService<Invitation> {
    void settle();
}
