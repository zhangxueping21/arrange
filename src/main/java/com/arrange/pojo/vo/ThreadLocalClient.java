package com.arrange.pojo.vo;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

public class ThreadLocalClient {
    /**
     * 不同业务设置不同的业务场景，如：业务A设置值为1，业务B设置值为2...
     */
    private static ThreadLocal<CloseableHttpClient> clientThreadLocal = new ThreadLocal<>();


    public static CloseableHttpClient get() {
        return clientThreadLocal.get();
    }

    public static void set(CloseableHttpClient client) {
//        if (ThreadLocalClient.clientThreadLocal == null) {
//            ThreadLocalClient.clientThreadLocal = new ThreadLocal<>();
//        }
        com.arrange.pojo.vo.ThreadLocalClient.clientThreadLocal.set(client);
    }

    public static void clearClient() {
        set(null);
    }
}
