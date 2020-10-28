package com.arrange.service.impl;

import com.arrange.dao.mapper.UserResultMapper;
import com.arrange.pojo.po.UserResult;
import com.arrange.service.UserResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserResultServiceImpl extends ServiceImpl<UserResultMapper, UserResult> implements UserResultService {
    @Autowired
    private UserResultMapper userResultMapper;
}
