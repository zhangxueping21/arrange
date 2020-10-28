package com.arrange.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器，和Jwt配套使用完成用户鉴权
 */
public class JwtInterceptor implements HandlerInterceptor {
//    @Autowired
//    JwtUtill jwtUtill;
//    @Autowired
//    UserService userService;
//    @Autowired
//    WeixinUtil weixinUtil;
//    @Autowired
//    WeixinService weixinService;
//    public static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    /**
     * 进入到控制器方法之前执行的内容
     * @param request
     * @param response
     * @param handler
     * @return 如果返回false则被拦截，反正放行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        //根据openId到数据库查找
//        String userOpenId = WeixinUtil.getMessageMap(request, response).get("FromUserName");
//        User user = userService.getOne(userOpenId);
//        request.setAttribute("userOpenId", userOpenId);
//        if (user != null) {//数据库有数据，之前绑定了
//            request.setAttribute("userOpenId", user.getId().toString());
//            return true;
//        } else {//没有绑定过，发送绑定连接
//            log.info("没有绑定过");
//            log.info(request.getRequestURL() + "被拦截");
//            return false;
//        }
        return true;
    }

}
