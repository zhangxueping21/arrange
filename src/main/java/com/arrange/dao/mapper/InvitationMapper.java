package com.arrange.dao.mapper;

import com.arrange.pojo.po.Invitation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface InvitationMapper extends BaseMapper<Invitation> {
}
