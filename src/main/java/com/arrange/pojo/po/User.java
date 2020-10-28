package com.arrange.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**主建，唯一身份标识*/
    private Integer id;
    /**用户姓名*/
    private String name;
    /**学号*/
    private String stuNumber;
    /**教务系统密码(密文存储)*/
    private String password;
    /**
     * 所属单位，一个由数字字符组成的字符串，
     * 每个数字字符都在unit表中能找到*/
    private String unit;
}
