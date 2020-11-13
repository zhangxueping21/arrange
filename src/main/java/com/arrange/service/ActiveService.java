package com.arrange.service;

import com.arrange.pojo.po.Active;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface ActiveService extends IService<Active> {
    void settle();
    int getMaxId();
}
