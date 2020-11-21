package com.arrange.controller;

import com.alibaba.druid.util.StringUtils;
import com.arrange.pojo.po.Active;
import com.arrange.pojo.po.FreeMsg;
import com.arrange.pojo.po.Invitation;
import com.arrange.pojo.po.User;
import com.arrange.pojo.po.timetable.Curriculum;
import com.arrange.pojo.po.timetable.Curriculums;
import com.arrange.pojo.po.timetable.FirstDay;
import com.arrange.pojo.vo.Response;
import com.arrange.pojo.vo.ResponseMsg;
import com.arrange.service.ActiveService;
import com.arrange.service.FirstDayService;
import com.arrange.service.InvitationService;
import com.arrange.service.UserService;
import com.arrange.utils.JwtUtill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

/**
 * 排班的controller
 */
@Transactional
@RestController
public class ArrangeController {
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private JwtUtill jwtUtill;
    @Autowired
    private UserService userService;
    @Autowired
    private ActiveService activeService;
    @Autowired
    FirstDayService firstDayService;
    /**
     * 一键排班并返回活动的排班结果
     * @param request
     * @param activeId
     * @return
     */
    @PostMapping("/arrange")
    public Response arrange(HttpServletRequest request,int activeId) throws JsonProcessingException {
        String stuNumber = (String) request.getAttribute("stuNumber");
        Map<String,Object> resultMap = new HashMap<>();
        if(!StringUtils.isEmpty(stuNumber)) {
            List<User> users = invitationService.getUsers(activeId);
            Active active = activeService.getById(activeId);
            if(users != null && users.size()>0) {
                Queue <FreeMsg> frees = new LinkedList<>();//记录每个人的空余时间
                Map<Integer,int[][]> freesMap = new HashMap<>();
                for(User user:users){
                    int[][] free = Packaging(user.getTimetable(),active.getStartTime(),active.getEndTime());
                    free = dealFree(free);
                    FreeMsg freeMsg = new FreeMsg(user.getId(),free);
                    frees.add(freeMsg);
                    freesMap.put(user.getId(),free);
                }
                int days = (int) (active.getEndTime().toEpochDay() - active.getStartTime().toEpochDay())+1;//计算出多少天
                int num = active.getNum();
                int[][][] groups = null;
                if(num > 0){
                    groups = new int[days][4][num];
                    int max = 0;//最多循环5次
                    Map<Integer,Integer> hasArrange = new HashMap<>();//已经被安排的学生
                    while(!check(groups)){
                        hasArrange.clear();
                        max++;
                        for(int i = 0;i<groups.length;i++){//第几天
                            for(int j = 0;j<groups[i].length;j++){//第几节
                                for(int n = 0;n<active.getNum();n++){//第几个人
                                    if(groups[i][j][n] == 0){
                                        FreeMsg freeMsg1 = null;
                                        for(FreeMsg freeMsg:frees){
                                            int id = freeMsg.getId();
                                            int[][] free1 = freeMsg.getFree();
                                            if(free1[i][j]==0 && hasArrange.get(id) == null) {//没课
                                                freeMsg1 = new FreeMsg(id,free1);
                                            }
                                        }
                                        if(freeMsg1 != null && !search(groups[i][j],freeMsg1.getId())){
                                            groups[i][j][n] = freeMsg1.getId();
                                            hasArrange.put(freeMsg1.getId(),1);
                                            frees.remove(freeMsg1);
                                            frees.add(freeMsg1);
                                        }
                                    }
                                }
                            }
                        }
                        if(max > 4*days*num)
                            break;
                    }
                }else if(num == 0){
                    groups = new int[days][4][100];
                    for(int i = 0;i<groups.length;i++){//第几天
                        for(int j = 0;j<groups[i].length;j++){//第几节
                            int num1 = 0;
                            for(int k = 0;k<users.size();k++){
                                int[][] free = freesMap.get(users.get(k).getId());
                                if(num1<100 && free[i][j] == 0){
                                    groups[i][j][num1] = users.get(k).getId();
                                    num1++;
                                }
                            }
                        }
                    }
                }
                List<String> resultList= dealGroups(groups,active);
                String token = jwtUtill.updateJwt(stuNumber);
                resultMap.put("token",token);
                resultMap.put("resultList",resultList);
                return new Response().success(resultMap);
            }
        }
        return new Response(ResponseMsg.AUTHENTICATE_FAILED);
    }

    /**
     * 查看这个用户在该时间段有没有被安排值班
     * @param users
     * @param userId
     * @return
     */
    private boolean search(int[] users, Integer userId) {
        for(int i = 0;i<users.length;i++){
            if(users[i] == userId)
                return true;
        }
        return false;
    }

    /**
     * 处理得到的排班数组，生成字符串，并修改活动和邀请结果
     * @param groups
     * @return
     */
    private List<String> dealGroups(int[][][] groups,Active active) throws JsonProcessingException {
        List<Invitation> invitations = invitationService.getByActiveId(active.getId());
        for (Invitation invitation:invitations){
            invitation.setTime("");
            invitationService.saveOrUpdate(invitation);
        }
        LocalDate formDay = active.getStartTime();
        List<String> resultList = new ArrayList<>();
        for(int i = 0;i<groups.length;i++) {//第几天
            for (int j = 0; j < groups[i].length ; j++) {//第几节

                String time = formDay.plusDays(i).toString();
                String detailTime = detailTime(j);
                time += " "+detailTime;
                String result = time;
                for (int k = 0; k < groups[i][j].length; k++) {//第几个人

                    User user = userService.getById(groups[i][j][k]);
                    if(user != null){
                        Invitation invitation = invitationService.getByActiveIdAndUserId(active.getId(),user.getId());
                        String re = invitation.getTime();
                        invitation.setTime(re+time+"\n");
                        invitationService.saveOrUpdate(invitation);
                        String name = user.getName();
                        result += " "+name;
                    }
                }
                resultList.add(result);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String resultListJson = mapper.writeValueAsString(resultList);
        active.setState(1);
        active.setResult(resultListJson);
        activeService.saveOrUpdate(active);
        return  resultList;
    }

    /**
     * 根据时间段序号获取值班的具体时间
     * @param j
     * @return
     */
    private String detailTime(int j) {
        switch (j){
            case 0: return "08:00-09:40";
            case 1: return "10:00-11:40";
            case 2: return "14:10-15:50";
            case 3: return "16:00-17:40";
        }
        return "";
    }

    /**
     * 处理记录用户空闲时间的数组，将11节课的空闲与否换算成5节课的
     * @param free
     * @return
     */
    private int[][] dealFree(int[][] free){
        int[][] newFree = new int[free.length][5];
        for(int i = 0;i<free.length;i++){
            if(free[i].length < 11)
                return null;
            if(free[i][0] >= 1 || free[i][1] >= 1)
                newFree[i][0] = 1;
            if(free[i][2] >= 1 || free[i][3] >= 1)
                newFree[i][1] = 1;
            if(free[i][4] >= 1 || free[i][5] >= 1)
                newFree[i][2] = 1;
            if(free[i][6] >= 1 || free[i][7] >= 1)
                newFree[i][3] = 1;
            newFree[i][4] = 1;//将晚上全部设为有课，不进行排班
        }
        return newFree;
    }
    /**
     * 获取没课上的数组
     * @param json
     * @return
     */
    private int[][] Packaging(String json,LocalDate startTime,LocalDate endTime) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Curriculums curriculums = objectMapper.readValue(json, Curriculums.class);
        FirstDay firstDay = firstDayService.getById(firstDayService.count());
        LocalDate firstWeek = LocalDate.of(firstDay.getYear(),firstDay.getMonth(),firstDay.getDay());
        int days = (int) (endTime.toEpochDay() - startTime.toEpochDay())+1;//计算出多少天
        int[][] free = new int[days][12];
        LocalDate now = LocalDate.parse(startTime.toString());
        int i = 0;
        while(!now.isAfter(endTime)){
            if(! now.isBefore(firstWeek)){
                Long week = 1+(now.toEpochDay()-firstWeek.toEpochDay())/7;//第几周
                Integer dayOfWeek = now.getDayOfWeek().getValue();//星期几
                List<Curriculum> curriculumOfDay = curriculums.getCurriculum().get(dayOfWeek-1);
                for(Curriculum curriculum :curriculumOfDay){
                    if(curriculum.getFrom_week()<=week && curriculum.getTo_week()>=week
                            && (curriculum.getArrange_type()==Curriculum.NORMAL
                            || (week - curriculum.getArrange_type())%2 == 0)){
                        int fromSection = curriculum.getFrom_section();
                        int toSection = curriculum.getTo_section();
                        for(int j=fromSection;j<=toSection;j++)
                            free[i][j-1]++;//有课就大于0
                    }
                }
            }
            i++;
            now = now.plusDays(1);
        }
        return free;
    }



    private boolean check(int[][][] groups) {
        for(int i = 0;i< groups.length;i++){
            for(int j = 0;j<groups[i].length;j++){
                for(int k = 0;k<groups[i][j].length;k++){
                    if(groups[i][j][k] == 0)
                        return false;
                }
            }
        }
        return true;
    }
}
