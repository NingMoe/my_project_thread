package service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.hht.interceptor.Page;
import mapper.*;
import models.*;
import org.mybatis.guice.transactional.Transactional;
import service.EliteClassPersonService;
import service.EliteTrainCostService;
import utils.StringUtils;
import vo.ProEliteClassPersonVo;
import ws.NoticeClassSendService;
import ws.NoticeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static utils.StringUtils.objToString;

/**
 * Created by Administrator on 2017/7/15 0015.
 */
public class EliteClassPersonServiceImpl implements EliteClassPersonService {

    @Inject
    private ProEliteClassPersonMapper proEliteClassPersonMapper;

    @Inject
    private ProEliteGroomMapper eliteGroomMapper;
    @Inject
    private ProShopEliteMapper shopEliteMapper;
    @Inject
    private NoticeService noticeService;
    @Inject
    private ProEliteClassMapper classMapper;
    @Inject
    private NoticeClassSendService noticeClassSendService;

    @Inject
    private ProEliteNoticeClassRecordMapper classRecordMapper;

    @Inject
    private EliteTrainCostService costService;

    @Inject
    private  ProEliteClassPersonProjectMapper proEliteClassPersonProjectMapper;

    /**
     * 删除班级学员
     *
     * @param mapParams
     * @return
     */

    @Override
    @Transactional
    public int deleteEliteClassPerson(Map<String, Object> mapParams) throws Exception {
        //删除班级成员
        ProEliteClassPerson classPerson = new ProEliteClassPerson();
        classPerson.setId(objToString(mapParams.get("id")));
        classPerson.setDr("Y");
        proEliteClassPersonMapper.updateByPrimaryKeySelective(classPerson);
        //更新门店金鹰
        String employeeId = objToString(mapParams.get("employeeId"));
        ProShopElite shopElite = new ProShopElite();
        shopElite.setEmployeeId(employeeId);
        shopElite.setClassStatus("300");
        shopEliteMapper.updateByBean(shopElite);
        //更新报名表
        eliteGroomMapper.updateGroomEmployeeByCreateTime(employeeId);
        //删除组班通知
        classRecordMapper.deleteByEmployeeCode(mapParams.get("employeeCode").toString());

        return 1;

    }


    /**
     * 组班
     *
     * @param mapParams
     * @return
     */
    @Override
    @Transactional
    public int addEliteClassPerson(Map<String, Object> mapParams) throws Exception {
        //  if (mapParams != null) {
        List<ProEliteClassPerson> list = JSON.parseArray(mapParams.get("list").toString(), ProEliteClassPerson.class);
        ProEliteTrainCost cost = costService.getProEliteTrainCosts() ;
        //校验学员是否审核通过
        for (ProEliteClassPerson classPerson : list) {
            ProEliteClass eliteClass = classMapper.selectByPrimaryKey(objToString(mapParams.get("classId")));

            classPerson.setClassId(objToString(mapParams.get("classId")));
            classPerson.setCreatorId(objToString(mapParams.get("createId")));
            classPerson.setId(StringUtils.getTableId("ProEliteClassPerson"));
            classPerson.setDr("N");
            classPerson.setResult("100");
            classPerson.setCreateTime(System.currentTimeMillis());


            if (mapParams.get("lobbyRemark")!=null){
                classPerson.setLobbyRemark(objToString(mapParams.get("lobbyRemark")));
            }
            if (mapParams.get("lobbyType")!=null && mapParams.get("lobbyType")!=""){
                classPerson.setLobbyType(objToString(mapParams.get("lobbyType")));
            }
            if (mapParams.get("shopId")!=null){
                classPerson.setShopId(objToString(mapParams.get("shopId")));
            }

            if ("100".equals(eliteClass.getClassType())){
                classPerson.setManagerCost(cost.getLobbyManagerCost());
                classPerson.setEmployeeCost(cost.getLobbyEmployeeCost());
            }else {
                classPerson.setEmployeeCost(cost.getReserveEmployeeCost());
                classPerson.setManagerCost(cost.getReserveManagerCost());
                classPerson.setEmployeeAmerce(cost.getReserveEmployeeAmerce());
                classPerson.setManagerAmerce(cost.getReserveManagerAmerce());
            }
            proEliteClassPersonMapper.insert(classPerson);
            ProEliteGroom groom = new ProEliteGroom();
            groom.setEmployeeId(classPerson.getEmployeeId());
            eliteGroomMapper.updateEliteClassGroomByEmployeeId(groom);
            //TODO
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (eliteClass != null) {
                                if ("100".equals(eliteClass.getClassType())) {
                                    noticeClassSendService.sendNotice(classPerson.getEmployeeCode(), "100", eliteClass);
                                } else {
                                    noticeClassSendService.sendNotice(classPerson.getEmployeeCode(), "200", eliteClass);
                                }

                            }
                        }
                    }
            ).run();

        }
        return 1;
    }
    // return 0;
    //}

    /**
     * 查询某学期班级下的全部学员(分页)
     *
     * @param mapParams
     * @return
     */
    @Override
    public Page getEliteClassPersonByPage(Map<String, Object> mapParams) throws Exception {

        Page page = new Page();
        Map<String, Object> map = Maps.newHashMap();
        String classId = objToString(mapParams.get("classId"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(classId)) {
            map.put("classId", classId);
        }
        String classType = objToString(mapParams.get("classType"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(classType)) {
            map.put("classType", classType);
        }
        //分页参数
        String pageNum = objToString(mapParams.get("pageNum"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(pageNum)) {
            page.setPageNum(Integer.valueOf(pageNum));
        } else {
            page.setPageNum(1);
        }
        String pageSize = objToString(mapParams.get("pageSize"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(pageSize)) {
            page.setPageRecordCount(Integer.valueOf(pageSize));
        } else {
            page.setPageRecordCount(10);
        }
        List<Map<String, Object>> persons;
        //处理状态
        String result = objToString(mapParams.get("result"));
        if ("100".equals(result)) {
            map.put("result", "100");
            page.setParams(map);
            persons = proEliteClassPersonMapper.getEliteClassPersonByListPage(page);

        } else if ("200".equals(result)) {
            page.setParams(map);
            persons = proEliteClassPersonMapper.getClassPersonListPage(page);
        } else {
            page.setParams(map);
            persons = proEliteClassPersonMapper.getEliteClassPersonByListPage(page);
        }
//        persons = this.getClassPersonInfo(persons, classId);
        persons = this.getClassPerson(persons, mapParams);
        page.setList(persons);
        return page;
    }
    //by 王刚   查询人员参加大堂班次数详情
    private List<Map<String, Object>> getClassPerson(List<Map<String, Object>> persons,Map<String, Object>  ClassType) {
        List<Map<String,Object>> mapList= proEliteClassPersonMapper.getEmployeesAllTimesByType(ClassType);
//        ProEliteClass eliteClass = classMapper.selectByPrimaryKey(classId);
//        int count = 0;
//        if (eliteClass != null) {
//            ProEliteGroom eliteGroom = new ProEliteGroom();
//            eliteGroom.setEmployeeId(person.get("employeeId").toString());
//            eliteGroom.setClassType(eliteClass.getClassType());
//            count = eliteGroomMapper.selectToClassCountByEmPloyeeId(eliteGroom);
//        }
        if (persons != null && persons.size() > 0) {
            for (Map<String, Object> person : persons) {
                //性别
                if ("0".equals(person.get("gender"))) {
                    person.put("gender", "女");

                } else if ("1".equals(person.get("gender"))) {
                    person.put("gender", "男");
                }
                //年龄
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                Calendar c1 = Calendar.getInstance();
                Integer age = null;
                String birthday = "";
                if (person.get("birthday") != null) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                    Date date = null;
                    if (person.get("birthday") != null && !"".equals(person.get("birthday"))) {
                        try {
                            date = format.parse(person.get("birthday").toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if (date != null) {
                        c1.setTime(date);
                        age = c.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
                        birthday = format.format(date);
                    }

                }
//                if ("300".equals(person.get("classStatus"))) {
//                    person.put("isOk", "否");
//                } else {
//                    person.put("isOk", "是");
//                }
                person.put("isOk", "是");
                String lastEntryTime = "";
                if (person.get("lastEntryTime") != null && !"".equals(person.get("lastEntryTime"))) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Long time = new Long(person.get("lastEntryTime").toString());
                    String str = format.format(time);
                    try {
                        lastEntryTime = format.format(format.parse(str)).toString();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                //婚姻状况
                if("1".equals(objToString(person.get("isMarry")))){
                    person.put("isMarry","未婚");
                }else if("2".equals(objToString(person.get("isMarry")))){
                    person.put("isMarry","已婚");
                }else if("3".equals(objToString(person.get("isMarry")))){
                    person.put("isMarry","离异");
                }else {
                    person.put("isMarry","未知");
                }
                int count=0;

                for (Map<String, Object> objectMap : mapList) {
                    if(objectMap.get("employeeCode")!=null&&person.get("employeeCode")!=null){
                        if(objectMap.get("employeeCode").toString().equals(person.get("employeeCode").toString())){
                            count=Integer.parseInt(objectMap.get("count").toString());
                            break;
                        }
                    }
                }
                person.put("classTimes", count);
                person.put("lastEntryTime", lastEntryTime);
                person.put("age", age);
                person.put("birthday", birthday);
            }
        }
        return persons;
    }

    //班级成员详细信息
    private List<Map<String, Object>> getClassPersonInfo(List<Map<String, Object>> persons, String classId) {
        if (persons != null && persons.size() > 0) {
            for (Map<String, Object> person : persons) {
                //性别
                if ("0".equals(person.get("gender"))) {
                    person.put("gender", "女");

                } else if ("1".equals(person.get("gender"))) {
                    person.put("gender", "男");
                }
                //年龄
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                Calendar c1 = Calendar.getInstance();
                Integer age = null;
                String birthday = "";
                if (person.get("birthday") != null) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                    Date date = null;
                    if (person.get("birthday") != null && !"".equals(person.get("birthday"))) {
                        try {
                            date = format.parse(person.get("birthday").toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if (date != null) {
                        c1.setTime(date);
                        age = c.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
                        birthday = format.format(date);
                    }

                }
                if ("300".equals(person.get("classStatus"))) {
                    person.put("isOk", "否");
                } else {
                    person.put("isOk", "是");
                }

                String lastEntryTime = "";
                if (person.get("lastEntryTime") != null && !"".equals(person.get("lastEntryTime"))) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Long time = new Long(person.get("lastEntryTime").toString());
                    String str = format.format(time);
                    try {
                        lastEntryTime = format.format(format.parse(str)).toString();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                ProEliteClass eliteClass = classMapper.selectByPrimaryKey(classId);
                int count = 0;
                if (eliteClass != null) {
                    ProEliteGroom eliteGroom = new ProEliteGroom();
                    eliteGroom.setEmployeeId(person.get("employeeId").toString());
                    eliteGroom.setClassType(eliteClass.getClassType());
                    count = eliteGroomMapper.selectToClassCountByEmPloyeeId(eliteGroom);
                }
                //婚姻状况
                if("1".equals(objToString(person.get("isMarry")))){
                    person.put("isMarry","未婚");
                }else if("2".equals(objToString(person.get("isMarry")))){
                    person.put("isMarry","已婚");
                }else if("3".equals(objToString(person.get("isMarry")))){
                    person.put("isMarry","离异");
                }else {
                    person.put("isMarry","未知");
                }
                person.put("classTimes", count);
                person.put("lastEntryTime", lastEntryTime);
                person.put("age", age);
                person.put("birthday", birthday);
            }
        }
        return persons;
    }

    /**
     * 发证
     *
     * @param mapParams
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> updateClassCrtificate(Map<String, Object> mapParams) throws Exception {
        Map<String, Object> stringObjectMap = new HashMap<>();
        //   if (mapParams != null) {


        List<ProEliteClassPersonVo> list = JSON.parseArray(mapParams.get("list").toString(), ProEliteClassPersonVo.class);
        if (list != null && list.size() > 0) {
            List<String> employeeCodeList = new ArrayList<>();
            String classType = null;
            String code = null;
            List<ProEliteClassPerson> classPersonList = new ArrayList<>();
            List<ProShopElite> eliteList = new ArrayList<>();
            for (ProEliteClassPersonVo classPersonVo : list) {
                classPersonVo.setResult("300");
                code = classPersonVo.getEmployeeCode();
                employeeCodeList.add(code);

                ProEliteClassPerson person = new ProEliteClassPerson();
                person.setId(classPersonVo.getId());
                person.setResult("300");
                person.setRemark(classPersonVo.getRemark());
                person.setModifyTime(System.currentTimeMillis());
                person.setTrainEnd(System.currentTimeMillis());
                classPersonList.add(person);
//                    proEliteClassPersonMapper.updateByPrimaryKeySelective(person);
                classType = classPersonVo.getClassType();
                ProShopElite shopElite = new ProShopElite();
                shopElite.setEmployeeId(classPersonVo.getEmployeeId());
                shopElite.setClassStatus("100");
                if ("100".equals(classType)) {
                    shopElite.setEliteStatus("300");
                    shopElite.setIsLobby("Y");
//                        shopEliteMapper.updateStatusByEmployeeId(shopElite);

                }
                if ("200".equals(classType)) {
                    shopElite.setEliteStatus("400");
                    shopElite.setIsManager("Y");
//                        shopEliteMapper.updateStatusByEmployeeId(shopElite);
                }
                eliteList.add(shopElite);
            }
            proEliteClassPersonMapper.batchUpdateClassPerson(classPersonList);
            shopEliteMapper.batchUpdateElite(eliteList);
            stringObjectMap.put("employeeCode", code);
            stringObjectMap.put("classType", classType);
            stringObjectMap.put("employeeCodeList", employeeCodeList);
            stringObjectMap.put("list", list);
        }
        //   }
        return stringObjectMap;
    }

    /**
     * 未通过（发证操作）
     *
     * @param mapParams
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> updateClassNotPass(Map<String, Object> mapParams) throws Exception {
        Map<String, Object> stringObjectMap = new HashMap<>();
        //   if (mapParams != null) {
        ProEliteClassPerson person = new ProEliteClassPerson();
        String id = StringUtils.objToString(mapParams.get("id"));
        person.setId(id);
        person.setResult("200");
        person.setRemark(StringUtils.objToString(mapParams.get("remark")));
        person.setModifyTime(System.currentTimeMillis());
        person.setTrainEnd(System.currentTimeMillis());
        proEliteClassPersonMapper.updateByPrimaryKeySelective(person);
        String employeeId = StringUtils.objToString(mapParams.get("employeeId"));
        ProShopElite shopElite = new ProShopElite();
        shopElite.setEmployeeId(employeeId);
        shopElite.setClassStatus("100");
        shopEliteMapper.updateByBean(shopElite);
        stringObjectMap.put("id", id);
        stringObjectMap.put("employeeId", employeeId);

        //   }
        return stringObjectMap;
    }

    @Override
    public List<Map<String, Object>> getPassNotInLobby(Map<String, Object> mapParams) throws Exception {
        //获大堂证未在大堂岗
        mapParams.put("classType", "100");
        List<Map<String, Object>> mapList = proEliteClassPersonMapper.getPassNotInLobby(mapParams);
        //获店经理证未在大堂岗
        mapParams.put("classType", "200");
        mapParams.put("isManager", "Y");
        List<Map<String, Object>> managerList = proEliteClassPersonMapper.getPassNotInLobby(mapParams);
        mapList.addAll(managerList);
        return mapList;
    }

    @Override
    @Transactional
    public void sendNotice(Map<String, Object> result) {

        ProEliteClass proEliteClass = classMapper.getTermByEmployeeCode(result);
        String classType1 = proEliteClass.getClassType();
        List<String> employeeCodeList = (List<String>) result.get("employeeCodeList");
        List<ProEliteClassPersonVo> list = (List<ProEliteClassPersonVo>) result.get("list");

        //发送通知
        if ("100".equals(classType1)) {
            //大堂班获证通知
            noticeService.sendNoticeByCodeList(employeeCodeList, "11", true, proEliteClass);

        } else if ("200".equals(classType1)) {
            //店经理班获证通知
            noticeService.sendNoticeByCodeList(employeeCodeList, "12", true, proEliteClass);
        }
    }

    @Override
    @Transactional
    public void sendNotPassNotice(Map<String, Object> result) {

        String employeeId = result.get("employeeId").toString();
        String id = result.get("id").toString();
        //判断现在人员的报班状态
        ProShopElite shopElite = shopEliteMapper.selectShopEliteByEmployeeId(employeeId);
        if (shopElite != null) {

            //未通过发通知
            ProEliteClassPerson classPerson = proEliteClassPersonMapper.selectByPrimaryKey(id);

            if (classPerson != null) {
                ProEliteClass eliteClass = classMapper.selectByPrimaryKey(classPerson.getClassId());
                if ("200".equals(shopElite.getEliteStatus())) {

                    noticeService.sendNoticeNotPass(classPerson.getEmployeeCode(), "14", true, eliteClass,classPerson);
                    noticeService.sendNoticeNotPass(classPerson.getEmployeeCode(), "22", false, eliteClass,classPerson);
//                    noticeService.sendNotice(classPerson.getEmployeeCode(), "14", true, eliteClass);
//                    noticeService.sendNotice(classPerson.getEmployeeCode(), "22", false, eliteClass);
                } else if ("300".equals(shopElite.getEliteStatus())) {
                    noticeService.sendNoticeNotPass(classPerson.getEmployeeCode(), "13", true, eliteClass,classPerson);
                    noticeService.sendNoticeNotPass(classPerson.getEmployeeCode(), "23", false, eliteClass,classPerson);
//                    noticeService.sendNotice(classPerson.getEmployeeCode(), "13", true, eliteClass);
//                    noticeService.sendNotice(classPerson.getEmployeeCode(), "23", false, eliteClass);
                }

            }
        }
    }

    @Override
    public List<ProEliteClassPerson> getElitePersionList(List<ProEliteClassPersonVo> personVoList) {
        return proEliteClassPersonMapper.getElitePersionList(personVoList);
    }
    /**
     * 查询员工是否已经组班
     * @param mapParams
     * @return
     */
    @Override
    public List<ProEliteClassPerson> getClassPersons(Map<String, Object> mapParams)throws Exception {
        return proEliteClassPersonMapper.getClassPersons(mapParams);
    }
    /**
     * 查询员工报班次数
     * @param groom
     * @return
     */
    @Override
    public int getEmployeesClassTimesByType(Map<String, Object> groom) throws Exception {
        return proEliteClassPersonMapper.getEmployeesClassTimesByType(groom);
    }

    @Override
    public List<Map<String,Object>> getEmployeesAllTimesByType(Map<String, Object> groom) throws Exception {
        return proEliteClassPersonMapper.getEmployeesAllTimesByType(groom);
    }

    /**
     * 班级学员详情
     * @return
     */
    @Override
    public Page getClassPersonInfo(Map<String, Object> params)throws Exception {
        Page page = this.page(params);
        page.setParams(params);
        List<Map<String, Object>> persons = proEliteClassPersonMapper.getClassPersonByListPage(page);
        if(persons.size()>0){
            for (Map<String, Object> person : persons) {
                //培训结束时间
                Long endTime = null;
                    if(person.get("days")!=null){
//                        int days = Integer.valueOf(person.get("days").toString());
//                        Long beginTime = Long.valueOf(person.get("beginTime").toString());
//                         endTime  = beginTime + days * 3600 * 24 * 1000;
                    }

                    //是否取证
                    String result =  person.get("result").toString();
                    if("100".equals(result)){
                        person.put("certificateResult","待审");
                    }else if("200".equals(result)){
                        person.put("certificateResult","否");
                    }else if("300".equals(result)){
                        if("100".equals(person.get("classType").toString())){
                            person.put("certificateType","大堂班");
                        }
                        if("200".equals(person.get("classType").toString())){
                            person.put("certificateType","实习店经理班");
                        }
                        person.put("certificateTime",person.get("trainEndTime"));
                        person.put("certificateResult","是");
                    }
                    person.put("endTime",endTime);
                }

            }
        page.setList(persons);
        return page;
    }

    @Override
    public int batchUpdate(List<ProEliteClassPersonVo> list) {

       int i= proEliteClassPersonMapper.batchUpdate(list);
        return i;
    }

    @Override
    public Map<String,Object> getHistroy(Map<String, Object> mapParams) {
        List<ProEliteClassPersonVo> list = JSON.parseArray(mapParams.get("list").toString(), ProEliteClassPersonVo.class);
        List<ProEliteClassPersonVo> listresult=proEliteClassPersonMapper.getHistroy(list);
        StringBuffer sb=new StringBuffer();
        for (ProEliteClassPersonVo proEliteClassPersonVo : listresult) {
            sb.append("员工"+proEliteClassPersonVo.getEmployeeCode()+"在"+proEliteClassPersonVo.getTeacherName()+"下已经被淘汰过请重新分配、人\r\n");
        }
        Map<String,Object> objectMap=new HashMap<>();
        if(listresult.size()>0){
            objectMap.put("flag","1");
            objectMap.put("result",sb.toString());

        }else{
            objectMap.put("flag","0");
            objectMap.put("result",sb.toString());
        }
        return objectMap;
    }

    /**
     * 删除
     * @param mapParams
     * @return
     */
    @Override
    public int deleteEliteClassPersonProjectById(Map<String, Object> mapParams) throws Exception{
        ProEliteClassPersonProject projects= new  ProEliteClassPersonProject();
        String id = mapParams.get("id").toString();
        projects.setId(id);
        projects.setDr("Y");

      int  delete= proEliteClassPersonProjectMapper.updateEliteClassPersonProjectById(projects);

        return delete;
    }

    /**
     * 展示项目信息
     *
     * @param mapParams
     * @return
     * @throws Exception
     */
    @Override
    public List<ProEliteClassPersonProject> getEliteClassPersonProjectList(Map<String, Object> mapParams) throws Exception {

        return proEliteClassPersonProjectMapper.getEliteClassPersonProjectList(mapParams);
    }

    @Override
    public List<Map<String, Object>> getExportCard(Map<String, Object> mapParams) throws Exception {
        return proEliteClassPersonProjectMapper.selectExport(mapParams);
    }


    /***
     * 分页通用
     * @param mapParams
     * @return
     */
    private Page page(Map<String, Object> mapParams) {
        Page page = new Page();
        //分页参数
        String pageNum = objToString(mapParams.get("pageNum"));
        String pageSize = objToString(mapParams.get("pageSize"));
        page.setPageNum(Integer.valueOf(pageNum));
        page.setPageRecordCount(Integer.valueOf(pageSize));
        return page;
    }
}

