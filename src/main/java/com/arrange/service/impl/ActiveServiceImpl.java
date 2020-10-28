package com.arrange.service.impl;

import com.arrange.dao.mapper.ActiveMapper;
import com.arrange.pojo.po.Active;
import com.arrange.service.ActiveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActiveServiceImpl extends ServiceImpl<ActiveMapper, Active> implements ActiveService {
    @Autowired
    private ActiveMapper activeMapper;
}
