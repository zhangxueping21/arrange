package com.arrange.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 每个用户的值班时间
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResult {
    /**主建*/
    private Integer id;
    /**活动id*/
    private Integer activeId;
    /**用户id*/
    private Integer userId;
    /**用户的值班时间*/
    private String subTime;
}
