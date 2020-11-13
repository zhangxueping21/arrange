package com.arrange.pojo.po;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateActive {
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
}
