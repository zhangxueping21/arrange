package com.arrange.service;

import com.arrange.pojo.po.Active;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActiveService extends IService<Active> {
    void settle();
    int getMaxId();

    List<Active> listByUserId(Integer userId);
}
