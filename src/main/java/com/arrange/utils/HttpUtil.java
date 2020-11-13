package com.arrange.utils;

import com.arrange.pojo.po.LoginUser;
import com.arrange.pojo.po.User;
import com.arrange.pojo.vo.ThreadLocalClient;
import com.arrange.pojo.vo.ThreadLocalContext;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {
    private static final String REFERER = "http://ssfw.scuec.edu.cn/ssfw/index.do";
    private static String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36";
    public static final Logger log = LoggerFactory.getLogger(com.arrange.utils.HttpUtil.class);
    /**
     * 给微信发送请求，携带数据
     * @param url 地址
     * @param data json串
     * @return
     */
    public static String send(String url, String data){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL urlObj = new URL(url);
            URLConnection connection = urlObj.openConnection();
            connection.setDoOutput(true);
            outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());
            inputStream = connection.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            StringBuilder stringBuilder = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1)
                stringBuilder.append(new String(bytes,0,len));
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 到教务系统爬取数据
     * @return
     */
    public static String crawl(LoginUser loginUser, String page) throws IOException {

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        RequestConfig config = RequestConfig.custom().setRedirectsEnabled(true).build();//不允许重定向

        if(LoginUtil.verify(loginUser.getStuNumber(),loginUser.getPassword())){

            HttpGet httpGet = new HttpGet("http://ssfw.scuec.edu.cn/ssfw/cas_index.jsp");
            httpGet.setConfig(config);
            httpGet.addHeader("User-Agent", UA);
            httpGet.addHeader("Referer", REFERER);

            CloseableHttpClient client = ThreadLocalClient.get();
            HttpClientContext context = ThreadLocalContext.get();
            client.execute(httpGet,context);

            httpGet.setPath(page);

            CloseableHttpResponse inputStream = client.execute(httpGet,context);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream.getEntity().getContent()));
            StringBuilder page1 = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                page1.append(line);
            }
            reader.close();
            inputStream.close();
            return page1.toString();
        }
        return null;
    }
}
