package com.arrange.utils;

import com.arrange.pojo.po.timetable.Curriculum;
import com.arrange.pojo.po.timetable.Curriculums;
import com.arrange.pojo.vo.ThreadLocalClient;
import com.arrange.pojo.vo.ThreadLocalContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TimetableUtil {
    private static final String REFERER = "http://ssfw.scuec.edu.cn/ssfw/index.do";

    private static String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36";

    public static final Logger log = LoggerFactory.getLogger(com.arrange.utils.TimetableUtil.class);


    private TimetableUtil(){
    }


    /**
     * 对爬到的页面进行处理,包装成对象，然后转成json字符串
     * @param page
     * @return
     */
    public static Map<String,String> dataProcessing (String page) throws IOException, ParseException {
        String username = getName();
        Curriculums curriculums = new Curriculums();
        List<List<Curriculum>> curriculumWeek = new ArrayList<>(7);//每个周的课，一周有7天
        for(int i = 0;i<7;i++)
            curriculumWeek.add(new ArrayList<>());

        if(page != null && !"".equals(page)){
            Document document = Jsoup.parse(page);
            Elements inputArray = document.getElementsByTag("tr");//对应的元素数组
            String[] str = document.getElementsByTag("strong").text().split("【");
            String endString = "课程类别";
            try{
                for (int i=1;! inputArray.get(i).text().contains(endString);i++){
                    Element elementTr = inputArray.get(i);
                    Elements elementTds = elementTr.getElementsByTag("td");
                    setCurriculum(elementTds,curriculumWeek);
                }
            }catch (IndexOutOfBoundsException e){
                log.info("html表格下标越界");
                e.printStackTrace();
            }
        }else{
            return null;
        }
        for(int i = 0;i<7;i++)
            Collections.sort(curriculumWeek.get(i), new com.arrange.utils.SortByAge());
        curriculums.setCurriculum(curriculumWeek);
        ObjectMapper mapper = new ObjectMapper();
        String curriculumsJson = mapper.writeValueAsString(curriculums);
        Map<String,String> map = new HashMap<>();
        map.put("curriculumsJson",curriculumsJson);
        map.put("username",username);
        return map;
    }

    private static String getName() throws IOException, ParseException {

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        RequestConfig config = RequestConfig.custom().setRedirectsEnabled(true).build();//不允许重定向

        HttpGet httpGet = new HttpGet("http://eol.scuec.edu.cn/meol/homepage/common/sso_login.jsp");
        httpGet.setConfig(config);
        httpGet.addHeader("User-Agent", UA);
        httpGet.addHeader("Referer", REFERER);

        CloseableHttpClient client = ThreadLocalClient.get();
        HttpClientContext context = ThreadLocalContext.get();
//            client.execute(httpGet,context);
//
//            httpGet.setPath("http://eol.scuec.edu.cn/meol/personal.do?menuId=0");
        CloseableHttpResponse inputStream = client.execute(httpGet,context);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream.getEntity().getContent()));
        HttpEntity entity = inputStream.getEntity();
        String page = EntityUtils.toString(entity, "utf-8");
        String pattern = "(?<=&from=welcomepage\" class=\"info chgright\" target=\"main\">).*?(?=</a></div>)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(page);
        if (m.find( )) {
            return m.group(0);
        } else {
            return "";
        }
    }

    private static void setCurriculum(Elements elementTds,List<List<Curriculum>> curriculumWeek ){
        Integer from_week = null;
        Integer to_week = null;
        Integer from_section = null;
        Integer to_section = null;
        Integer arrange_type = null;
        Integer week = 0;
        Integer rows = 0;
        String[] curriculmMsg = elementTds.get(5).text().split(" ");
        for(int i = 0;i<curriculmMsg.length;i++){
            String msg = curriculmMsg[i];
            if(msg.contains("周")){
                from_week = Integer.parseInt(msg.substring(0,msg.indexOf("-")));
                to_week = Integer.parseInt(msg.substring(msg.indexOf("-")+1,msg.indexOf("周")));
                if(msg.length() == msg.indexOf("周")+1)
                    arrange_type = Curriculum.NORMAL;
                if(msg.length()>msg.indexOf("周")+2 && msg.charAt(msg.indexOf("周")+2) == '单')
                    arrange_type = Curriculum.ODD_WEEK;
                else if(msg.length()>msg.indexOf("周")+2 && msg.charAt(msg.indexOf("周")+2) == '双')
                    arrange_type = Curriculum.DOUBLE_WEEK;
            }else if(msg.contains("星期")){
                week = null;
                switch (msg.charAt(2)){
                    case '一' :week = 1;break;
                    case '二' :week = 2;break;
                    case '三' :week = 3;break;
                    case '四' :week = 4;break;
                    case '五' :week = 5;break;
                    case '六' :week = 6;break;
                    case '七' :week = 7;break;
                    case '日' :week = 7;break;
                    case '天' :week = 7;break;
                }
            }else if(msg.contains("节")){
                rows ++;
                from_section = Integer.parseInt(msg.substring(0,msg.indexOf("-")));
                to_section = Integer.parseInt(msg.substring(msg.indexOf("-")+1,msg.indexOf("节")));
                Curriculum curriculum = new Curriculum(from_week,to_week,from_section,to_section,arrange_type);
                curriculumWeek.get(week-1).add(curriculum);
            }else{
                Curriculum curriculum = new Curriculum(from_week,to_week,from_section,to_section,arrange_type);
            }
        }
    }

}

class SortByAge implements Comparator<Curriculum> {
    @Override
    public int compare(Curriculum o1, Curriculum o2) {
        int i = o1.getFrom_section().compareTo(o2.getFrom_section());
        if(i == 0) {
            return o1.getTo_section().compareTo(o2.getTo_section());
        }else return i;
    }
}