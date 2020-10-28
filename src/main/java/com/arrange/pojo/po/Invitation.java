package com.arrange.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    /**值班时间，以字符串表示*/
    private String time;
}
