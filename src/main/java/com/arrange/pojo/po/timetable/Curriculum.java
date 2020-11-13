package com.arrange.pojo.po.timetable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 一节课的信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curriculum {

    public static final Integer NORMAL = 0;
    public static final Integer ODD_WEEK = 1;//单周
    public static final Integer DOUBLE_WEEK  = 2;//双周

    private Integer from_week;

    private Integer to_week;

    private Integer from_section;

    private Integer to_section;

    private Integer arrange_type;

}
