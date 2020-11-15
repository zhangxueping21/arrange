package com.arrange.service;

import com.arrange.pojo.po.Invitation;
import com.arrange.pojo.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvitationService extends IService<Invitation> {
    void settle();

    List<User> getUsers(int activeId);

    Invitation getByActiveIdAndUserId(Integer activeId, Integer userId);
}
