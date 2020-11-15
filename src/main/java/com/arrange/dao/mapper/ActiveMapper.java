package com.arrange.dao.mapper;

import com.arrange.pojo.po.Active;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ActiveMapper extends BaseMapper<Active> {
    void settle();
    int getMaxId();
    List<Active> listByUserId(Integer userId);
}
