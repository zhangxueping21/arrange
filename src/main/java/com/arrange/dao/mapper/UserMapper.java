package com.arrange.dao.mapper;

import com.arrange.pojo.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
    List<User> getByStuNumber(String stuNumber);

    List<User> getByUnit(String unit);

    void settle();
}
