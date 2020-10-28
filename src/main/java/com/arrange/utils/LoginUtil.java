package com.arrange.utils;

import com.arrange.pojo.vo.ThreadLocalClient;
import com.arrange.pojo.vo.ThreadLocalContext;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class LoginUtil {
    public static final Logger log = LoggerFactory.getLogger(com.arrange.utils.LoginUtil.class);
    public static String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36";
    public static String REFERER = "http://id.scuec.edu.cn/authserver/login";
    public static String LOGIN_PAGE = "http://id.scuec.edu.cn/authserver/login";
//    public static CloseableHttpClient client;
//    public static HttpClientContext context;
    private LoginUtil() {
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param username 用户名
     * @param password 密码
     * @return 所代表是否登录成功
     */
    public static boolean verify(String username, String password) throws IOException {

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
//        PorxyBean PorxyBean = ProxyList.getPorxyList().get((int)(Math.random()*ProxyList.getPorxyList().size()));

//        HttpHost httpHost = new HttpHost("http", PorxyBean.getIp(), PorxyBean.getPort());
        RequestConfig config = RequestConfig.custom()
                .setRedirectsEnabled(false)
//                .setProxy(httpHost)
                .setConnectTimeout(5, TimeUnit.SECONDS)
                .build();//不允许重定向

        HttpClientContext context = HttpClientContext.create();
        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build();

        Map<String,String> params = getParam(username,password,client);

        HttpPost httpPost = new HttpPost(LOGIN_PAGE);

        httpPost.setConfig(config);
        httpPost.addHeader("Referer", REFERER);
        httpPost.addHeader("User-Agent", UA);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        List <NameValuePair> nvps = new ArrayList <>();
        params.forEach((key, value) -> nvps.add(new BasicNameValuePair(key, value)));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response2 = client.execute(httpPost,context);
        ThreadLocalClient.set(client);
        ThreadLocalContext.set(context);
        if(response2 != null && response2.getCode() == 302 ){
            // context.getCookieStore().getCookies().forEach(System.out::println);
            log.info("登录成功");
            return true;
        }
        log.info("登录失败");
        return false;
    }

    public static Map<String, String> getParam(String username, String password,CloseableHttpClient client) throws IOException {
        HttpGet httpGet = new HttpGet(LOGIN_PAGE);
        httpGet.addHeader("Referer", REFERER);
        httpGet.addHeader("User-Agent", UA);
        CloseableHttpResponse inputStream = client.execute(httpGet);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream.getEntity().getContent()));

        StringBuilder page = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            page.append(line);
        }
        reader.close();
        inputStream.close();
        Map<String, String> map = new HashMap<>();
        Document document = Jsoup.parse(String.valueOf(page));
//        Elements elements = document.select("input[type=hidden] > [id]");
//        String _eventId = document.select("#_eventId").attr("value");
        String execution = document.select("#execution").attr("value");
        map.put("_eventId","submit");
        map.put("lt","");
        map.put("cllt","userNameLogin");
        map.put("execution",execution);

        String salt = document.select("#pwdEncryptSalt").attr("value");
        map.put("username", username);
        map.put("password", encryptPassword(salt, password));
        return map;
    }

    /**
     * 对密码加密
     *
     * @param salt     密钥
     * @param password 数据
     * @return String
     */
    private static String encryptPassword(String salt, String password) {

        byte[] encrypted = "".getBytes();
        try {
            //加密
            byte[] raw = salt.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //"算法/模式/补码方式"padding:CryptoJS.pad.Pkcs7
            //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec ips = new IvParameterSpec(randomStr(16).getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
            encrypted = cipher.doFinal((randomStr(64) + password).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(Base64.getEncoder().encode(encrypted));
    }

    /**
     * 获取随机字符串
     *
     * @param len
     * @return String
     */
    private static String randomStr(int len) {
        String str = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < len; i++) {
            res.append(str.charAt((int) (Math.random() * str.length())));
        }
        return res.toString();
    }
}

