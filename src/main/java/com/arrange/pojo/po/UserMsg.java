package com.arrange.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMsg {
    /**主建，唯一身份标识*/
    private Integer id;
    /**用户姓名*/
    private String name;
    /**学号*/
    private String stuNumber;
    /**
     * 所属单位，一个由数字字符组成的字符串，
     * 每个数字字符都在unit表中能找到*/
    private String unit;
}
