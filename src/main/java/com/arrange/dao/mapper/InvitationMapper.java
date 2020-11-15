package com.arrange.dao.mapper;

import com.arrange.pojo.po.Invitation;
import com.arrange.pojo.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InvitationMapper extends BaseMapper<Invitation> {
    void settle();
    List<User> getUsers(int activeId);
    List<Invitation> getByActiveIdAndUserId(Integer activeId, Integer userId);
}
