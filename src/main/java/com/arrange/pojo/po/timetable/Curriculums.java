package com.arrange.pojo.po.timetable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 将该类转化成json串，对应数据库中timetable表的timetable字段
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Curriculums {
    private List<List<Curriculum>> curriculum = new ArrayList<>();
}
