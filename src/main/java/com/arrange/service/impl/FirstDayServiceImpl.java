package com.arrange.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.arrange.dao.mapper.FirstDayMapper;
import com.arrange.pojo.po.timetable.FirstDay;
import com.arrange.service.FirstDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirstDayServiceImpl extends ServiceImpl<FirstDayMapper, FirstDay> implements FirstDayService {
    @Autowired
    private FirstDayMapper firstDayMapper;

    @Override
    public void settle() {
        firstDayMapper.settle();
    }

    @Override
    public int getMaxId() {
        return firstDayMapper.getMaxId();
    }
}
