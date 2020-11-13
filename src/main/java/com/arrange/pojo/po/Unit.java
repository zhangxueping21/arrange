package com.arrange.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Unit {
    /**主键*/
    private Integer id;
    /**单位名称*/
    private String unitTime;
    /**创建时间*/
    private LocalDateTime createTime;
    /**更新时间*/
    private LocalDateTime updateTime;
}
