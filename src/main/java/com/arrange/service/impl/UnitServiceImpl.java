package com.arrange.service.impl;

import com.arrange.dao.mapper.UnitMapper;
import com.arrange.pojo.po.Unit;
import com.arrange.service.UnitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements UnitService {
    @Autowired
    private UnitMapper unitMapper;

    @Override
    public void settle() {
        unitMapper.settle();
    }
}
