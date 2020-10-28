package com.arrange.pojo.po.timetable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * 每个学期第一天的信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirstDay {
    /** 每个学期的第一周的周一的日期 */
    @Id
    private Integer id ;
    private Integer year;
    private Integer month ;
    private Integer day ;
}
