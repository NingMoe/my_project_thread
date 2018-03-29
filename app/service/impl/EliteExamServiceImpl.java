package service.impl;

import com.google.inject.Inject;
import com.hht.interceptor.Page;
import mapper.ProEliteExaminationOnlineMapper;
import mapper.ProEmployeeMapper;
import mapper.ProPositionMapper;
import mapper.ProShopEliteMapper;
import models.ProEliteExaminationOnline;
import models.ProPosition;
import service.EliteExamService;
import utils.DateUtils;
import utils.StringUtils;

import java.util.*;

import static utils.StringUtils.objToString;

/**
 * Created by Administrator on 2017/7/19 0019.
 */
public class EliteExamServiceImpl implements EliteExamService {
    @Inject
    private ProEliteExaminationOnlineMapper examMapper;
    @Inject
    private ProEmployeeMapper employeeMapper;
    @Inject
    private ProShopEliteMapper shopEliteMapper;
    @Inject
    private ProPositionMapper positionMapper;



    @Override
    public List<Map<String, Object>> getExamTransitEmployees(Map<String, Object> mapParams) throws Exception {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (mapParams != null) {
            mapList = examMapper.getExamTransitEmployees(mapParams);
            if (mapList != null && mapList.size() > 0) {
                for (Map<String, Object> map : mapList) {
                    String code = StringUtils.objToString(map.get("employeeCode"));
                    int examTime = examMapper.getExamTimesCountByEmployyeCode(code);
                    String createTime = StringUtils.objToString(map.get("createTime"));
                    map.put("passDate", Long.valueOf(createTime));
                    map.put("examTime", examTime);
                    //TODO
                    int useTime = differentDays(Long.valueOf(map.get("becomeTime").toString()),Long.valueOf(createTime));
                    map.put("useTime", useTime + "天");

                }

            }

        }
        return mapList;
    }

    /**
     * 两个日期的相差天数
     * @param time1
     * @param time2
     * @return
     */
    private int differentDays(Long time1,Long time2)
    {
        Date date1 = new Date(time1);
        Date date2 = new Date(time2);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return Math.abs(timeDistance + (day2-day1)) ;
        }
        else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2-day1));
            return Math.abs(day2-day1);
        }
    }

    @Override
    public int insertExamRecord(Map<String, Object> mapParams) throws Exception {
        if (mapParams != null) {
            ProEliteExaminationOnline exam = new ProEliteExaminationOnline();
            exam.setId(StringUtils.getTableId("ProEliteExaminationOnline"));
            exam.setEmployeeId(objToString(mapParams.get("employeeId")));
            exam.setEmployeeCode(objToString(mapParams.get("employeeCode")));
            String resultScore = StringUtils.objToString(mapParams.get("resultScore"));
//            if (resultScore != null && resultScore != "") {
//                exam.setRelultScore(Integer.valueOf(resultScore));
//            }
//            String passScore = StringUtils.objToString(mapParams.get("passScore"));
//            if (passScore != null && passScore != "") {
//                exam.setRelultScore(Integer.valueOf(passScore));
//            }
            exam.setIsPass(StringUtils.objToString(mapParams.get("isPass")));
            exam.setCreatorId(StringUtils.objToString(mapParams.get("creatorId")));
            exam.setCreateTime(System.currentTimeMillis());
            exam.setDr("N");
            exam.setIsPass("Y");
            examMapper.insert(exam);
            return 1;

        }
        return 0;
    }
//--------------------------------------------------报表汇总-----------------------------------------------------
    /**
     * 门店金鹰考试通过汇总
     *
     * @return
     */
    @Override
    public Page getShopEliteExamPassCollect(Map<String, Object> mapParams) throws Exception {
        String pageSize = objToString(mapParams.get("pageSize"));
        String pageNum = objToString(mapParams.get("pageNum"));
        //设置分页参数
        Page page = new Page();
        //分页参数
        if (pageNum != null && !"".equals(pageNum)) {
            page.setPageNum(Integer.valueOf(pageNum));
        }
        if (pageSize != null && !"".equals(pageSize)) {
            page.setPageRecordCount(Integer.valueOf(pageSize));
        }
        page.setParams(mapParams);
        //所有店的金鹰人数
        List<Map<String, Object>> shopEliteList = shopEliteMapper.getShopEliteCountByListPage(page);
        if (shopEliteList.size() <= 0) {
            page.setList(new ArrayList());
            return page;
        }

        List<String> shopIdList = new ArrayList<>();

        for (Map<String, Object> map : shopEliteList) {
            shopIdList.add(map.get("shopId").toString());

        }
        //所有店的员工总数
        List<Map<String, Object>> employeeList = shopEliteMapper.getShopEmployeeCountByList(shopIdList);

        //考试通过的人数
        List<Map<String, Object>> examPassList = shopEliteMapper.getExamPassCountByList(shopIdList);
        if (examPassList.size() <= 0) {
            page.setList(new ArrayList());
            page.setList(shopIdList);//后添加的
            return page;
        }
        //考试通过人员的具体信息
        List<Map<String, Object>> examEmployeeList = shopEliteMapper.getExamPassEmployeeByList(shopIdList);

        for (Map<String, Object> shopElite : shopEliteList) {
            shopElite.put("examPassCount", "");
            shopElite.put("passScale", "");
            shopElite.put("averageDays", "");

            //门店id
            String shopId = shopElite.get("shopId").toString();
            //已有门店金鹰池人数
            int eliteCount = Integer.valueOf(shopElite.get("eliteCount").toString());
            for (Map<String, Object> employee : employeeList) {
                if (shopId.equals(employee.get("shopId").toString())) {
                    //门店总人数
                    int employeeCount = Integer.valueOf(employee.get("employeeCount").toString());
                    //金鹰比例
                    int eliteScale = (int)((double)(eliteCount * 100)/ employeeCount);
                    shopElite.put("eliteScale", eliteScale + "%");
                    break;

                }

            }
            int examPassCount = 0;
            for (Map<String, Object> examPass : examPassList) {
                if (shopId.equals(examPass.get("shopId").toString())) {
                    //通过的人数
                    examPassCount = Integer.valueOf(examPass.get("passCount").toString());
                    shopElite.put("examPassCount", examPassCount);
                    //通过占比
                    int passScale = (int)((double)(examPassCount * 100 )/ eliteCount);
                    shopElite.put("passScale", passScale +"%");
                    break;
                }
            }


            int days = 0;
            for (Map<String, Object> examEmployee : examEmployeeList) {
                if (shopId.equals(examEmployee.get("shopId").toString())) {
                    //计算每个员工通过时间
                    Long passTime = Long.valueOf(examEmployee.get("createTime").toString());
                    Long elitePoolTime = Long.valueOf(examEmployee.get("elitePoolTime").toString());
                    int day = (int) ((passTime - elitePoolTime) / (1000 * 3600 * 24));
                    days = days + day;

                }

            }
            //计算平均通过时间
            if (examPassCount != 0) {
                int averageDays = days / examPassCount;
                shopElite.put("averageDays", averageDays);
            }

        }
        page.setList(shopEliteList);
        return page;
    }


    /**
     * 门店金鹰考试通过详情
     *
     * @return
     */
    @Override
    public Page getShopEliteExamPassInfo(Map<String, Object> mapParams) throws Exception {
        String pageSize = objToString(mapParams.get("pageSize"));
        String pageNum = objToString(mapParams.get("pageNum"));
        //设置分页参数
        Page page = new Page();
        //分页参数
        if (pageNum != null && !"".equals(pageNum)) {
            page.setPageNum(Integer.valueOf(pageNum));
        }
        if (pageSize != null && !"".equals(pageSize)) {
            page.setPageRecordCount(Integer.valueOf(pageSize));
        }
        page.setParams(mapParams);
        List<Map<String, Object>> mapList = examMapper.getExamEmployeesByListPage(page);
        List<ProPosition> proPosition = positionMapper.getPositions();
        if (mapList != null && mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                String createTime = StringUtils.objToString(map.get("createTime"));
                map.put("passDate", Long.valueOf(createTime));
                map.put("examResult", "通过");
                //计算每个员工通过时间
                Long passTime = Long.valueOf(map.get("createTime").toString());
                Long elitePoolTime = Long.valueOf(map.get("elitePoolTime").toString());
                int day = (int) ((passTime - elitePoolTime) / (1000 * 3600 * 24));
                map.put("useDay",day);
                for (int i = 0; i < proPosition.size(); i++) {
                    if(proPosition.get(i).getId().equals(map.get("positionId").toString())){
                        map.put("positionName", proPosition.get(i).getPositionName());
                        continue;
                    }
                }
//                ProPosition proPosition = positionMapper.selectByPrimaryKey(objToString(map.get("positionId")));
//                if(proPosition!=null){
//                    map.put("positionName", proPosition.getPositionName());
//                }else{
//                    map.put("positionName", "");
//                }
            }
        }
        page.setList(mapList);
        return page;
    }

    /**
     * 月度考试汇总
     * @return
     */
    @Override
    public Page getExamCollectOfMonth(Map<String, Object> params) throws Exception {
        String pageSize = params.get("pageSize").toString();
        String pageNum =  params.get("pageNum").toString();

        Page page = new Page();
        page.setPageNum(Integer.valueOf(pageNum));
        page.setPageRecordCount(Integer.valueOf(pageSize));
        page.setParams(params);
        //门店的金鹰人数
        List<Map<String, Object>> shopEliteList = shopEliteMapper.getShopEliteCountByListPage(page);
        if (shopEliteList.size() <= 0) {
            page.setList(new ArrayList());
            return page;
        }
        List<String> shopIdList = new ArrayList<>();
        for (Map<String, Object> map : shopEliteList) {
            shopIdList.add(map.get("shopId").toString());
        }
        //门店员工总数
        List<Map<String, Object>> employeeList = shopEliteMapper.getShopEmployeeCountByList(shopIdList);
        //当月参加考试的人数
        if(params.get("month")==null||"".equals(params.get("month"))){
            params.put("month", DateUtils.getMonth(new Date()));
        }
        params.put("list" ,shopIdList);
        List<Map<String, Object>> examList = examMapper.getExamPassCountByList(params);
        if (examList.size() <= 0) {
            page.setList(new ArrayList());
            return page;
        }
        //当月考试通过的人数
        params.put("isPass" ,"Y");
        List<Map<String, Object>> examPassList = examMapper.getExamPassCountByList(params);
        if (examPassList.size() <= 0) {
            page.setList(new ArrayList());
            return page;
        }
        //当月考试通过人员的具体信息
        List<Map<String, Object>> examEmployeeList = shopEliteMapper.getExamPassEmployeeByList(shopIdList);

        for (Map<String, Object> shopElite : shopEliteList) {
            shopElite.put("examPassCount", "");
            shopElite.put("passScale", "");
            shopElite.put("averageDays", "");
            shopElite.put("month",params.get("month").toString());
            //门店id
            String shopId = shopElite.get("shopId").toString();
            //已有门店金鹰池人数
            int eliteCount = Integer.valueOf(shopElite.get("eliteCount").toString());
            for (Map<String, Object> employee : employeeList) {
                if (shopId.equals(employee.get("shopId").toString())) {
                    //门店总人数
                    int employeeCount = Integer.valueOf(employee.get("employeeCount").toString());
                    //金鹰比例
                    int eliteScale = (int)((double)(eliteCount * 100)/ employeeCount);
                    shopElite.put("eliteScale", eliteScale + "%");
                    break;
                }
            }

           //参加考试人数
            int examCount = 0;
            for (Map<String, Object> exam : examList) {
                if (shopId.equals(exam.get("shopId").toString())) {
                    examCount = Integer.valueOf(exam.get("count").toString());
                    shopElite.put("examCount", examCount);
                    break;
                }
            }
            int examPassCount = 0;
            for (Map<String, Object> examPass : examPassList) {
                if (shopId.equals(examPass.get("shopId").toString())) {
                    //通过的人数
                    examPassCount = Integer.valueOf(examPass.get("count").toString());
                    shopElite.put("examPassCount", examPassCount);
                    //通过占比
                    int passScale = (int)((double)(examPassCount * 100 )/ examCount);
                    shopElite.put("passScale", passScale +"%");
                    break;
                }
            }
            int days = 0;
            for (Map<String, Object> examEmployee : examEmployeeList) {
                if (shopId.equals(examEmployee.get("shopId").toString())) {
                    //计算每个员工通过时间
                    Long passTime = Long.valueOf(examEmployee.get("createTime").toString());
                    Long elitePoolTime = Long.valueOf(examEmployee.get("elitePoolTime").toString());
                    int day = (int) ((passTime - elitePoolTime) / (1000 * 3600 * 24));
                    days = days + day;
                }

            }
            //计算平均通过时间
            if (examPassCount != 0) {
                int averageDays = days / examPassCount;
                shopElite.put("averageDays", averageDays);
            }

        }
        page.setList(shopEliteList);
        return page;
    }



}
