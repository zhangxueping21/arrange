package com.arrange.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.arrange.pojo.po.timetable.FirstDay;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FirstDayMapper extends BaseMapper<FirstDay> {
    void settle();
}
