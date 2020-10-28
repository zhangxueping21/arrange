package com.arrange.service.impl;

import com.arrange.dao.mapper.UserMapper;
import com.arrange.pojo.po.User;
import com.arrange.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    @Autowired
    private UserMapper userMapper;
}
