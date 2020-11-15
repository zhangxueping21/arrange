package com.arrange.service.impl;

import com.arrange.dao.mapper.ActiveMapper;
import com.arrange.pojo.po.Active;
import com.arrange.service.ActiveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActiveServiceImpl extends ServiceImpl<ActiveMapper, Active> implements ActiveService {
    @Autowired
    private ActiveMapper activeMapper;

    @Override
    public void settle() {
        activeMapper.settle();
    }

    @Override
    public int getMaxId() {
        return activeMapper.getMaxId();
    }

    @Override
    public List<Active> listByUserId(Integer userId) {
        return activeMapper.listByUserId(userId);
    }
}
