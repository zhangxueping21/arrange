package com.arrange.controller;

import com.alibaba.druid.util.StringUtils;
import com.arrange.pojo.po.LoginUser;
import com.arrange.pojo.po.User;
import com.arrange.pojo.po.UserMsg;
import com.arrange.pojo.vo.Response;
import com.arrange.pojo.vo.ResponseMsg;
import com.arrange.service.UserService;
import com.arrange.utils.HttpUtil;
import com.arrange.utils.JwtUtill;
import com.arrange.utils.TimetableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录教务系统的controller
 */
@RestController
@Transactional
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtill jwtUtill;
    private String timetablePage = "http://ssfw.scuec.edu.cn/ssfw/xkgl/xkjgcx.do";
    /**
     * 验证教务系统
     * @param loginUser 从前端获取用户名和密码封装成loginUser
     * @return 返回响应结果
     */
    @PostMapping("/login")
    public Response login(LoginUser loginUser) throws IOException {
        Map<String,String> map = TimetableUtil.dataProcessing(HttpUtil.crawl(loginUser,timetablePage));
        if(map != null){
            String timetableJson = map.get("curriculumsJson");
            String username = map.get("username");
            Map<String,String> resultMap = new HashMap<>();
            if(!StringUtils.isEmpty(timetableJson)) {
                List<User> users = userService.getByStuNumber(loginUser.getStuNumber());
                int id = 0;
                User user;
                String unit = "";
                resultMap.put("firstLogin","true");
                if(users != null && users.size()>0){
                    id = users.get(0).getId();
                    unit = users.get(0).getUnit();
                    resultMap.put("firstLogin","false");
                }
                user = new User(id,username,loginUser.getStuNumber(),unit,timetableJson, LocalDateTime.now(),LocalDateTime.now());
                userService.saveOrUpdate(user);
                String token = jwtUtill.createJwt(loginUser.getStuNumber());
                resultMap.put("token",token);
                return new Response().success(resultMap);
            }
        }
        return new Response(ResponseMsg.PASSWORD_WRONG);
    }

    @GetMapping("/getUserMsg")
    public Response getUserMsg(HttpServletRequest request) {
        String stuNumber = (String) request.getAttribute("stuNumber");
        if(!StringUtils.isEmpty(stuNumber)){
            List<User> users = userService.getByStuNumber(stuNumber);
            if(users != null && users.size()>0) {
                User user = users.get(0);
                String token = jwtUtill.updateJwt(stuNumber);
                Map<String,Object> resultMap = new HashMap<>();
                UserMsg userMsg = new UserMsg(user.getId(),user.getName(),user.getStuNumber(),user.getUnit());
                resultMap.put("userMsg",userMsg);
                resultMap.put("token",token);
                return new Response().success(resultMap);
            }
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }
}
