package com.arrange.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveMsg {
    /**主键，唯一身份标识*/
    private Integer id;
    /**活动的发起人，和user表的id关联*/
    private Integer createUser;
    /**活动名称*/
    private String name;
    /**所属单位，和unit表关联*/
    private Integer unit;
    /**开始时间*/
    private String startTime;
    /**结束时间*/
    private String endTime;
    /**值班的人数，有1,2,3,4,5人，0表示不限*/
    private Integer num;
    /**活动地点*/
    private String position;
    /**活动备注*/
    private String remarks;
    /**活动的状态，已排班为1，未排班为0*/
    private Integer state;
    /**排班结果*/
    private String result;
}
