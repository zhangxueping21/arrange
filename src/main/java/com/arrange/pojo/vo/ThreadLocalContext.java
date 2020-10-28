package com.arrange.pojo.vo;

import org.apache.hc.client5.http.protocol.HttpClientContext;

public class ThreadLocalContext {
    /**
     * 不同业务设置不同的业务场景，如：业务A设置值为1，业务B设置值为2...
     */
    private static ThreadLocal<HttpClientContext> contextThreadLocal = new ThreadLocal<>();


    public static HttpClientContext get() {
        return contextThreadLocal.get();
    }

    public static void set(HttpClientContext context) {
//        if (ThreadLocalContext.contextThreadLocal == null) {
//            ThreadLocalContext.contextThreadLocal = new ThreadLocal<>();
//        }
        com.arrange.pojo.vo.ThreadLocalContext.contextThreadLocal.set(context);
    }

    public static void clearClient() {
        set(null);
    }
}
