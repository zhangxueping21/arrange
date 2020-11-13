package com.arrange.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Active {
    /**主键，唯一身份标识*/
    private Integer id;
    /**活动的发起人，和user表的id关联*/
    private Integer createUser;
    /**活动名称*/
    private String name;
    /**所属单位，和unit表关联*/
    private Integer unit;
    /**开始时间*/
    private LocalDate startTime;
    /**结束时间*/
    private LocalDate endTime;
    /**值班的人数，有1,2,3,4,5人，0表示不限*/
    private Integer num;
    /**活动地点*/
    private String position;
    /**排班结果*/
    private String result;
    /**活动备注*/
    private String remarks;
    /**创建时间*/
    private LocalDateTime createTime;
    /**更新时间*/
    private LocalDateTime updateTime;
}
