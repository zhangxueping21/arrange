package com.arrange.service;

import com.arrange.pojo.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends IService<User> {
    List<User> getByStuNumber(String stuNumber);

    List<User> getByUnit(String unit);
    void settle();
}
