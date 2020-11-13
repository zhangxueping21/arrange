package com.arrange.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invitation {
    /**主键*/
    private Integer id;
    /**被邀请的用户的id，和user表的id关联*/
    private Integer userId;
    /**活动id，和active表的id关联*/
    private Integer activeId;
    /**邀请状态，0表示未查看，10代表已查看并且拒绝，11代表已查看并且同意*/
    private Integer state;
    /**值班时间*/
    private String time;
    /**创建时间*/
    private LocalDateTime createTime;
    /**更新时间*/
    private LocalDateTime updateTime;
}
