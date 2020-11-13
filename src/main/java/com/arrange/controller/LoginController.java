package com.arrange.controller;

import com.alibaba.druid.util.StringUtils;
import com.arrange.pojo.po.LoginUser;
import com.arrange.pojo.po.User;
import com.arrange.pojo.vo.Response;
import com.arrange.pojo.vo.ResponseMsg;
import com.arrange.service.UserService;
import com.arrange.utils.HttpUtil;
import com.arrange.utils.JwtUtill;
import com.arrange.utils.TimetableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 登录教务系统的controller
 */
@RestController
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtill jwtUtill;
    private String timetablePage = "http://ssfw.scuec.edu.cn/ssfw/xkgl/xkjgcx.do";
    /**
     * 绑定教务系统
     * @param loginUser 从前端获取用户名和密码封装成loginUser
     * @return 返回响应结果
     */
    @PostMapping("/login")
    public Response login(LoginUser loginUser) throws IOException {
        Map<String,String> map = TimetableUtil.dataProcessing(HttpUtil.crawl(loginUser,timetablePage));
        String timetableJson = map.get("curriculumsJson");
        String username = map.get("username");
        if(!StringUtils.isEmpty(timetableJson)) {
            List<User> users = userService.getByStuNumber(loginUser.getStuNumber());
            int id = 0;
            User user;
            if(users != null && users.size()>0){
                id = users.get(0).getId();
            }
            user = new User(id,username,loginUser.getStuNumber(),loginUser.getUnit(),timetableJson, LocalDateTime.now(),LocalDateTime.now());
            userService.saveOrUpdate(user);
            String token = jwtUtill.createJwt(loginUser.getStuNumber());
            return new Response().success(token);
        }
        return new Response(ResponseMsg.PASSWORD_WRONG);
    }
}
