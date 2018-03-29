package service.impl;

import com.alibaba.fastjson.JSON;
import com.google.inject.Inject;
import com.hht.utils.BeanIdCreater;
import mapper.*;
import models.*;
import models.vo.ProEmployeeVo;
import org.mybatis.guice.transactional.Transactional;
import service.ImportService;
import utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by MR.GANG on 2017/10/30.
 */
public class ImportServiceImpl implements ImportService {
    @Inject
    private ProShopEliteMapper proShopEliteMapper;
    @Inject
    private ProEliteClassMapper proEliteClassMapper;
    @Inject
    private ProEliteNoticeClassRecordMapper proEliteNoticeClassRecordMapper;
    @Inject
    private ProEliteNoticeRecordMapper proEliteNoticeRecordMapper;
    @Inject
    private ProShopMapper proShopMapper;
    @Inject
    private ProEmployeeMapper proEmployeeMapper;
    @Inject
    private ProEliteGroomMapper proEliteGroomMapper;
    @Inject
    private  ProEliteClassPersonMapper proEliteClassPersonMapper;
    @Inject
    private ProEliteClassPersonProjectMapper proEliteClassPersonProjectMapper;
    @Override
    @Transactional
    public Map<String, Object> getLobbyClassImport(Map<String, Object> params) {
        Map<String, Object> returnData = new HashMap<>();//返回值
        List<HashMap> list = JSON.parseArray(params.get("exportExcel").toString(), HashMap.class);
        //①查询所有大堂班级
        ProEliteClass proEliteClassParams = new ProEliteClass();
        proEliteClassParams.setClassType("100");
        List<ProEliteClass> proEliteClasses = proEliteClassMapper.getClassByClassType(proEliteClassParams);
        //②查询所有金鹰池人员
        List<String> paramsList = new ArrayList<>();
        List<ProShopElite> proShopEliteList = proShopEliteMapper.getElitesByCodeList(paramsList);

        //③查询条件准备
        Set<String> employeeCodes = new HashSet<>();//员工编号
        Set<String> classNames = new HashSet();//班级名称
        //④映射数据准备
        Map<String, String> classTeacherMapping = new HashMap<>();//班级-老师映射
        Map<String, String> classlecturerMapping = new HashMap<>();//班级-讲师映射
        Map<String, String> timeMapping = new HashMap<>();//班级-开班时间映射
        for (HashMap hashMap : list) {
            if (StringUtils.objToString(hashMap.get("shopName")).equals("所在门店")) {
                continue;
            }
            classTeacherMapping.put(StringUtils.objToString(hashMap.get("eliteClass")), StringUtils.objToString(hashMap.get("teacherName")));
            classlecturerMapping.put(StringUtils.objToString(hashMap.get("eliteClass")), StringUtils.objToString(hashMap.get("lecturer")));
            timeMapping.put(StringUtils.objToString(hashMap.get("eliteClass")), StringUtils.objToString(hashMap.get("startTime")));
            classNames.add(StringUtils.objToString(hashMap.get("eliteClass")).trim());
            employeeCodes.add(StringUtils.objToString(hashMap.get("employeeCode")));
        }
        //人员名称-->人员对象
        Map<String, ProEmployeeVo> employeeNameMapping = new HashMap<>();
        List<ProEmployeeVo> employees = proEmployeeMapper.queryEmployeeByEmployees(employeeCodes);
        for (ProEmployeeVo employee : employees) {
            employeeNameMapping.put(employee.getEmployeeCode(), employee);
        }
        //班级名称-->班级主键
        Map<String, String> classNameIdMapping = new HashMap<>();
        List<ProEliteClass> proEliteClass = new ArrayList<>();//添加班级
        for (String className : classNames) {
            //判断班级是否已经存在。存在则取原有班级
            boolean flag = false;
            for (ProEliteClass eliteClass : proEliteClasses) {
                if (className.equals(eliteClass.getTerm().trim())) {
                    classNameIdMapping.put(className, eliteClass.getId());
                    flag = true;
                    break;
                }
            }
            if (flag) {
                continue;
            }
            //不存在在新建班级
            String eliteClassId = StringUtils.getTableId("eliteClass");
            ProEliteClass p = new ProEliteClass();
            p.setId(eliteClassId);
            p.setClassType("100");
            p.setTerm(className);
            p.setTeacherName(classTeacherMapping.get(className));
            p.setTraineeNum(30);
//            p.setReportLocaltion("");
//            p.setBeginTime();系统导入时间
//            p.setReportTime(Long.parseLong(getTime(timeMapping.get(className)==null?"1900-01-01":timeMapping.get(className))));
            p.setAssistantTeacherName(classlecturerMapping.get(className));
            p.setCreatorId("ROOT");
            p.setDr("N");
            classNameIdMapping.put(className, eliteClassId);//插入班级名称--班级主键映射
            proEliteClass.add(p);
        }
        List<ProShopElite> proShopElites = new ArrayList<>();//门店精英添加
        List<ProEliteClassPerson> proEliteClassPerson = new ArrayList<>();//添加门店精英人员
        List<ProEliteNoticeRecord> proEliteNotices = new ArrayList<>();//员工通知
        List<ProEliteNoticeClassRecord> proEliteClassNotices = new ArrayList<>();//员工大模板通知
        StringBuffer sb = new StringBuffer();
        for (HashMap hashMap : list) {
            if (StringUtils.objToString(hashMap.get("shopName")).equals("所在门店")) {
                continue;
            }
            String shopEliteId = StringUtils.getTableId("ProShopElite");
            ProEmployeeVo proEmployeeVo = employeeNameMapping.get(StringUtils.objToString(hashMap.get("employeeCode")));
            if (proEmployeeVo == null) {
                continue;
            }
            for (ProShopElite proShopElite : proShopEliteList) {
                if (proShopElite.getEmployeeCode().equals(proEmployeeVo.getEmployeeCode())) {
                    sb.append(proShopElite.getEmployeeCode() + ":" + proEmployeeVo.getEmployeeName() + "已在金鹰池中已存在!\r\n");
                    break;
                }
            }
            ProShopElite shopElite = new ProShopElite();
            shopElite.setId(shopEliteId);
            shopElite.setEmployeeCode(proEmployeeVo.getEmployeeCode());
            shopElite.setEmployeeId(proEmployeeVo.getId());
            shopElite.setShopId(proEmployeeVo.getShopId());
            shopElite.setClassStatus("100");
            shopElite.setEliteStatus("300");
            shopElite.setIsLobby("Y");
            shopElite.setTestRecordId("ROOT");
            shopElite.setDr("N");
            shopElite.setTs(Long.parseLong("1509429523210"));
            proShopElites.add(shopElite);

            String eliteClassPersonId = StringUtils.getTableId("ProEliteClassPerson");
            ProEliteClassPerson eliteClassPerson = new ProEliteClassPerson();
            eliteClassPerson.setId(eliteClassPersonId);
            eliteClassPerson.setEmployeeCode(proEmployeeVo.getEmployeeCode());
            eliteClassPerson.setEmployeeId(proEmployeeVo.getId());
            eliteClassPerson.setCreatorId("ROOT");
            eliteClassPerson.setResult("300");
            eliteClassPerson.setRemark(StringUtils.objToString(hashMap.get("remark")));
            eliteClassPerson.setDr("N");
            eliteClassPerson.setClassId(classNameIdMapping.get(StringUtils.objToString(hashMap.get("eliteClass")).trim()));
            proEliteClassPerson.add(eliteClassPerson);

            String[] arr = {"1", "3", "5", "110"};//员工通知
            ProEliteNoticeRecord eliteNotice = null;
            for (String s : arr) {
                if (s.equals("3")) {
                    eliteNotice = new ProEliteNoticeRecord();
                    eliteNotice.setEmployeeId(proEmployeeVo.getId());
                    eliteNotice.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                    eliteNotice.setCreatorId("ROOT");
                    eliteNotice.setDr("N");
                    eliteNotice.setIsRead("N");
                    eliteNotice.setNoticeCode(s);
                    eliteNotice.setNoticeName("测评审核通过");
                    eliteNotice.setNoticeText("恭喜你进入金鹰池！但这仅仅才是开始，在这里你需要学习大量的知识和技能，为将来做好管理层而准备，加油吧！！！");
                } else if (s.equals("1")) {
                    eliteNotice = new ProEliteNoticeRecord();
                    eliteNotice.setEmployeeId(proEmployeeVo.getId());
                    eliteNotice.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                    eliteNotice.setCreatorId("ROOT");
                    eliteNotice.setDr("N");
                    eliteNotice.setIsRead("N");
                    eliteNotice.setNoticeCode(s);
                    eliteNotice.setNoticeName("推荐测评");
                    eliteNotice.setNoticeText("您好，店经理推荐你进入金鹰池，现在你需要完成咨询公司对你的人品、领导力、悟性、沟通能力及学习能力等方面的测评，店经理会参考测评结果决定你是否进入，但测评不是唯一评判标准。");
                } else if (s.equals("5")) {
                    eliteNotice = new ProEliteNoticeRecord();
                    eliteNotice.setEmployeeId(proEmployeeVo.getId());
                    eliteNotice.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                    eliteNotice.setCreatorId("ROOT");
                    eliteNotice.setDr("N");
                    eliteNotice.setIsRead("N");
                    eliteNotice.setNoticeCode(s);
                    eliteNotice.setNoticeName("线上考试通过");
                    eliteNotice.setNoticeText("你考了100分，合格分为80。恭喜你通过了线上考试，现在可以上海大啦！赶快找店经理为你安排后备大堂班培训班吧。");

                } else if (s.equals("110")) {
                    eliteNotice = new ProEliteNoticeRecord();
                    eliteNotice.setEmployeeId(proEmployeeVo.getId());
                    eliteNotice.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                    eliteNotice.setCreatorId("ROOT");
                    eliteNotice.setDr("N");
                    eliteNotice.setIsRead("N");
                    eliteNotice.setNoticeCode(s);
                    eliteNotice.setNoticeName("在线测评");
                    eliteNotice.setNoticeText("您的测评已经提交，请耐心等待店经理审核。");
                }
                proEliteNotices.add(eliteNotice);
            }
            ProEliteNoticeClassRecord proEliteNotice = new ProEliteNoticeClassRecord();
            proEliteNotice.setCreatorId("ROOT");
            proEliteNotice.setDr("N");
            proEliteNotice.setIsUse("Y");
            proEliteNotice.setNoticeClassId(proEmployeeVo.getEmployeeCode());
            proEliteNotice.setNoticeTitle("100");
            proEliteNotice.setNoticeText("<p>恭喜您成功报名后备大堂培训班！现将开班事宜通知如下，请认真阅读后务必点击下方“我已知晓并按时参加培训”进行确认。</p><p>一、基本信息</p><p>1、培训天数：20天</p><p>2、培训地点：平谷区塔洼人家度假村</p><p>3、报到地点：天通苑总办附近&nbsp;</p><p>4、报到时间：2017年10月14日08时30分&nbsp;</p><p>二、温馨提醒</p><p>1、请于2017年10月14日08时30分&nbsp;前扫描下方二维码加入培训群;</p><p>&lt;img id='scan' src='' alt=''/ &gt;</p><p>2、请根据天气情况安排好出行，并提前自行购买往返车票，学员的车费以及培训期间产生的费用门店自行承担，报销标准由门店确认；</p><p>3、因培训需要，请务必带上笔记本电脑、（黑色或白色）运动鞋、前后堂两套工服（夏季）、健康证复印件、工牌、洗漱用品及拖鞋，头花（女士），白色T恤和衬衫各一件，对讲机一部（含充电器），笔和笔记本；</p><p>4、培训期间，请各位学员根据天气情况安排好出行；</p><p>5、根据制度要求，学员在培训期间无工资，请知晓；</p><p>6、请提前做好工作交接，安排好门店工作。</p><p>7、请于3天内确认是否参加此次培训，如未按时回复，则需要重新报名。</p><p>我已知晓并按时参加培训</p><p>如不能按时参加，请提前向海大发送邮件申请，各门店不得私自调换人员。</p><p>如对以上通知有任何疑问，请致电海大：李宁， 电话：18001378890  </p>");
            proEliteClassNotices.add(proEliteNotice);
        }






        //批量插入
        if (proEliteClass != null && proEliteClass.size() != 0 && sb.length() == 0) {
            proEliteClassMapper.setProEliteClass(proEliteClass);
        }
        if (proShopElites != null && proShopElites.size() != 0 && sb.length() == 0) {
            proShopEliteMapper.insertBatch(proShopElites);
        }
        if (proEliteClassPerson != null && proEliteClassPerson.size() != 0 && sb.length() == 0) {
            proEliteClassPersonMapper.insertBatchClassPerson(proEliteClassPerson);
        }
        if (proEliteNotices != null && proEliteNotices.size() != 0 && sb.length() == 0) {
            proEliteNoticeRecordMapper.batchInsertNoticeRecord(proEliteNotices);
        }
        if (proEliteClassNotices != null && proEliteClassNotices.size() != 0 && sb.length() == 0) {
            proEliteNoticeClassRecordMapper.insertBatchClassRecord(proEliteClassNotices);
//            proEliteNoticeClassMapper
        }
        if (sb.length() == 0) {
            returnData.put("flag", "success");
            returnData.put("result", sb.toString());
        } else {
            returnData.put("flag", "fail");
            returnData.put("result", sb.toString());
        }
        return returnData;
    }

    @Override
    @Transactional
    public Map<String, Object> getLobbyAllClassImport(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        List<HashMap> list = JSON.parseArray(params.get("exportExcel").toString(), HashMap.class);
        ProEliteClass proEliteClassParams = new ProEliteClass();
        proEliteClassParams.setClassType("100");
        List<ProEliteClass> proEliteClasses = proEliteClassMapper.getClassByClassType(proEliteClassParams);
        //②查询所有金鹰池人员
        List<String> paramsList = new ArrayList<>();
        List<ProShopElite> proShopEliteList = proShopEliteMapper.getElitesByCodeList(paramsList);
        //所有已发证人员
        List<String> personCars = proEliteClassPersonMapper.selectAllCardPerson();
        //③查询条件准备
        Set<String> employeeCodes = new HashSet<>();//员工编号
        Set<String> classNames = new HashSet();//班级名称
        //④映射数据准备
        Map<String, String> classTeacherMapping = new HashMap<>();//班级-老师映射
        Map<String, String> classlecturerMapping = new HashMap<>();//班级-讲师映射
        Map<String, String> timeMapping = new HashMap<>();//班级-开班时间映射
        for (HashMap hashMap : list) {
            if (StringUtils.objToString(hashMap.get("shopName")).equals("所在门店")) {
                continue;
            }
            classTeacherMapping.put(StringUtils.objToString(hashMap.get("eliteClass")), StringUtils.objToString(hashMap.get("teacherName")));
            classlecturerMapping.put(StringUtils.objToString(hashMap.get("eliteClass")), StringUtils.objToString(hashMap.get("lecturer")));
            timeMapping.put(StringUtils.objToString(hashMap.get("eliteClass")), StringUtils.objToString(hashMap.get("startTime")));
            classNames.add(StringUtils.objToString(hashMap.get("eliteClass")).trim());

        }
//--------------------------------------数据导入-----------------------------------------
        //①查询人员大堂班是否发证  如已发证则 不导入
        List<Map<String, Object>> employeeInfos = new ArrayList<>();//所有需要导入的人员数据
        List<Map<String, Object>> deleteGroom = new ArrayList<>();
        for (HashMap hashMap : list) {
            boolean flag = true;
            for (String personCar : personCars) {
                if (StringUtils.objToString(hashMap.get("employeeCode")).equals(personCar)) {//人员已经存在发证
                    flag = false;
                    break;
                }
            }
            if (flag) {
                employeeCodes.add(StringUtils.objToString(hashMap.get("employeeCode")));
                employeeInfos.add(hashMap);


            }
            Map<String,Object> map=new HashMap<>();
            map.put("employeeCode",StringUtils.objToString(hashMap.get("employeeCode")));
            map.put("classType","100");
            map.put("dr","Y");
            deleteGroom.add(map);
        }
        //删除大堂班报名表数据
        proEliteGroomMapper.updateEliteClassGroomByEmployeeCode(deleteGroom);
        //-------------------------------------做班级人员映射-----------------------------------
        //班级名称-->班级主键
        Map<String, String> classNameIdMapping = new HashMap<>();
        List<ProEliteClass> proEliteClass = new ArrayList<>();//添加班级
        for (String className : classNames) {
            //判断班级是否已经存在。存在则取原有班级
            boolean flag = false;
            for (ProEliteClass eliteClass : proEliteClasses) {
                if (className.equals(eliteClass.getTerm().trim())) {
                    classNameIdMapping.put(className, eliteClass.getId());
                    flag = true;
                    break;
                }
            }
            if (flag) {
                continue;
            }
            //不存在在新建班级
            String eliteClassId = StringUtils.getTableId("eliteClass");
            ProEliteClass p = new ProEliteClass();
            p.setId(eliteClassId);
            p.setClassType("100");
            p.setTerm(className);
            p.setTeacherName(classTeacherMapping.get(className));
            p.setTraineeNum(30);
//            p.setReportLocaltion("");
//            p.setBeginTime();系统导入时间
//            p.setReportTime(Long.parseLong(getTime(timeMapping.get(className)==null?"1900-01-01":timeMapping.get(className))));
            p.setAssistantTeacherName(classlecturerMapping.get(className));
            p.setCreatorId("ROOT");
            p.setDr("N");
            classNameIdMapping.put(className, eliteClassId);//插入班级名称--班级主键映射
            proEliteClass.add(p);
        }
//---------------------------------门店精英数据准备------------------------------------------------
        Map<String, ProEmployeeVo> employeeNameMapping = new HashMap<>();
        List<ProEmployeeVo> employees = proEmployeeMapper.queryEmployeeByEmployees(employeeCodes);
        for (ProEmployeeVo employee : employees) {
            employeeNameMapping.put(employee.getEmployeeCode(), employee);
        }
        List<ProShopElite> proShopElites = new ArrayList<>();//门店精英添加
        List<ProEliteClassPerson> proEliteClassPerson = new ArrayList<>();//添加班级人员
        List<ProEliteNoticeRecord> proEliteNotices = new ArrayList<>();//员工通知
        List<ProEliteNoticeClassRecord> proEliteClassNotices = new ArrayList<>();//员工大模板通知
        List<ProEliteClassPerson> updateProEliteClassPerson=new ArrayList<>();//更新班级人员
        List<ProShopElite>  updateProShopElites=new ArrayList<>();//更新门店精英
        StringBuffer sb = new StringBuffer();
        //批量删除通知
        proEliteNoticeClassRecordMapper.deleteByEmployeeCodeList(employeeInfos);
//        for (HashMap hashMap : list) {
          for(Map<String,Object> hashMap:employeeInfos){
            if (StringUtils.objToString(hashMap.get("shopName")).equals("所在门店")) {
                continue;
            }
            String shopEliteId = StringUtils.getTableId("ProShopElite");
            ProEmployeeVo proEmployeeVo = employeeNameMapping.get(StringUtils.objToString(hashMap.get("employeeCode")));
             if (proEmployeeVo==null) {
                    continue;
             }
            boolean flag=true;//默认不存在
            for (ProShopElite proShopElite : proShopEliteList) {
                if (proShopElite.getEmployeeCode().equals(proEmployeeVo.getEmployeeCode())) {
                    flag=false;
                    break;
                }
            }
            if(flag){
                ProShopElite shopElite = new ProShopElite();
                shopElite.setId(shopEliteId);
                shopElite.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                shopElite.setEmployeeId(proEmployeeVo.getId());
                shopElite.setShopId(proEmployeeVo.getShopId());
                shopElite.setClassStatus("100");
                shopElite.setEliteStatus("300");
                shopElite.setIsLobby("Y");
                shopElite.setTestRecordId("ROOT");
                shopElite.setDr("N");
                shopElite.setTs(Long.parseLong("1509429523210"));
                proShopElites.add(shopElite);


                String[] arr = {"1", "3", "5", "110"};//员工通知
                ProEliteNoticeRecord eliteNotice = null;
                for (String s : arr) {
                    if (s.equals("3")) {
                        eliteNotice = new ProEliteNoticeRecord();
                        eliteNotice.setEmployeeId(proEmployeeVo.getId());
                        eliteNotice.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                        eliteNotice.setCreatorId("ROOT");
                        eliteNotice.setDr("N");
                        eliteNotice.setIsRead("N");
                        eliteNotice.setNoticeCode(s);
                        eliteNotice.setNoticeName("测评审核通过");
                        eliteNotice.setNoticeText("恭喜你进入金鹰池！但这仅仅才是开始，在这里你需要学习大量的知识和技能，为将来做好管理层而准备，加油吧！！！");
                    } else if (s.equals("1")) {
                        eliteNotice = new ProEliteNoticeRecord();
                        eliteNotice.setEmployeeId(proEmployeeVo.getId());
                        eliteNotice.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                        eliteNotice.setCreatorId("ROOT");
                        eliteNotice.setDr("N");
                        eliteNotice.setIsRead("N");
                        eliteNotice.setNoticeCode(s);
                        eliteNotice.setNoticeName("推荐测评");
                        eliteNotice.setNoticeText("您好，店经理推荐你进入金鹰池，现在你需要完成咨询公司对你的人品、领导力、悟性、沟通能力及学习能力等方面的测评，店经理会参考测评结果决定你是否进入，但测评不是唯一评判标准。");
                    } else if (s.equals("5")) {
                        eliteNotice = new ProEliteNoticeRecord();
                        eliteNotice.setEmployeeId(proEmployeeVo.getId());
                        eliteNotice.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                        eliteNotice.setCreatorId("ROOT");
                        eliteNotice.setDr("N");
                        eliteNotice.setIsRead("N");
                        eliteNotice.setNoticeCode(s);
                        eliteNotice.setNoticeName("线上考试通过");
                        eliteNotice.setNoticeText("你考了100分，合格分为80。恭喜你通过了线上考试，现在可以上海大啦！赶快找店经理为你安排后备大堂班培训班吧。");

                    } else if (s.equals("110")) {
                        eliteNotice = new ProEliteNoticeRecord();
                        eliteNotice.setEmployeeId(proEmployeeVo.getId());
                        eliteNotice.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                        eliteNotice.setCreatorId("ROOT");
                        eliteNotice.setDr("N");
                        eliteNotice.setIsRead("N");
                        eliteNotice.setNoticeCode(s);
                        eliteNotice.setNoticeName("在线测评");
                        eliteNotice.setNoticeText("您的测评已经提交，请耐心等待店经理审核。");
                    }
                    proEliteNotices.add(eliteNotice);
                }
                ProEliteNoticeClassRecord proEliteNotice = new ProEliteNoticeClassRecord();
                proEliteNotice.setCreatorId("ROOT");
                proEliteNotice.setDr("N");
                proEliteNotice.setIsUse("Y");
                proEliteNotice.setNoticeClassId(proEmployeeVo.getEmployeeCode());
                proEliteNotice.setNoticeTitle("100");
                proEliteNotice.setNoticeText("<p>恭喜您成功报名后备大堂培训班！现将开班事宜通知如下，请认真阅读后务必点击下方“我已知晓并按时参加培训”进行确认。</p><p>一、基本信息</p><p>1、培训天数：20天</p><p>2、培训地点：平谷区塔洼人家度假村</p><p>3、报到地点：天通苑总办附近&nbsp;</p><p>4、报到时间：2017年10月14日08时30分&nbsp;</p><p>二、温馨提醒</p><p>1、请于2017年10月14日08时30分&nbsp;前扫描下方二维码加入培训群;</p><p>&lt;img id='scan' src='' alt=''/ &gt;</p><p>2、请根据天气情况安排好出行，并提前自行购买往返车票，学员的车费以及培训期间产生的费用门店自行承担，报销标准由门店确认；</p><p>3、因培训需要，请务必带上笔记本电脑、（黑色或白色）运动鞋、前后堂两套工服（夏季）、健康证复印件、工牌、洗漱用品及拖鞋，头花（女士），白色T恤和衬衫各一件，对讲机一部（含充电器），笔和笔记本；</p><p>4、培训期间，请各位学员根据天气情况安排好出行；</p><p>5、根据制度要求，学员在培训期间无工资，请知晓；</p><p>6、请提前做好工作交接，安排好门店工作。</p><p>7、请于3天内确认是否参加此次培训，如未按时回复，则需要重新报名。</p><p>我已知晓并按时参加培训</p><p>如不能按时参加，请提前向海大发送邮件申请，各门店不得私自调换人员。</p><p>如对以上通知有任何疑问，请致电海大：李宁， 电话：18001378890  </p>");
                proEliteClassNotices.add(proEliteNotice);
            }else{
                ProShopElite shopElite = new ProShopElite();
                shopElite.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                shopElite.setEmployeeId(proEmployeeVo.getId());
                shopElite.setClassStatus("100");
                shopElite.setEliteStatus("300");
                shopElite.setIsLobby("Y");
                shopElite.setTs(Long.parseLong("1509429523210"));
                updateProShopElites.add(shopElite);


                String[] arr = {"1", "3", "5", "110"};//员工通知
                ProEliteNoticeRecord eliteNotice = null;
                for (String s : arr) {
                    if (s.equals("3")) {
                        eliteNotice = new ProEliteNoticeRecord();
                        eliteNotice.setEmployeeId(proEmployeeVo.getId());
                        eliteNotice.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                        eliteNotice.setCreatorId("ROOT");
                        eliteNotice.setDr("N");
                        eliteNotice.setIsRead("N");
                        eliteNotice.setNoticeCode(s);
                        eliteNotice.setNoticeName("测评审核通过");
                        eliteNotice.setNoticeText("恭喜你进入金鹰池！但这仅仅才是开始，在这里你需要学习大量的知识和技能，为将来做好管理层而准备，加油吧！！！");
                    } else if (s.equals("1")) {
                        eliteNotice = new ProEliteNoticeRecord();
                        eliteNotice.setEmployeeId(proEmployeeVo.getId());
                        eliteNotice.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                        eliteNotice.setCreatorId("ROOT");
                        eliteNotice.setDr("N");
                        eliteNotice.setIsRead("N");
                        eliteNotice.setNoticeCode(s);
                        eliteNotice.setNoticeName("推荐测评");
                        eliteNotice.setNoticeText("您好，店经理推荐你进入金鹰池，现在你需要完成咨询公司对你的人品、领导力、悟性、沟通能力及学习能力等方面的测评，店经理会参考测评结果决定你是否进入，但测评不是唯一评判标准。");
                    } else if (s.equals("5")) {
                        eliteNotice = new ProEliteNoticeRecord();
                        eliteNotice.setEmployeeId(proEmployeeVo.getId());
                        eliteNotice.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                        eliteNotice.setCreatorId("ROOT");
                        eliteNotice.setDr("N");
                        eliteNotice.setIsRead("N");
                        eliteNotice.setNoticeCode(s);
                        eliteNotice.setNoticeName("线上考试通过");
                        eliteNotice.setNoticeText("你考了100分，合格分为80。恭喜你通过了线上考试，现在可以上海大啦！赶快找店经理为你安排后备大堂班培训班吧。");

                    } else if (s.equals("110")) {
                        eliteNotice = new ProEliteNoticeRecord();
                        eliteNotice.setEmployeeId(proEmployeeVo.getId());
                        eliteNotice.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                        eliteNotice.setCreatorId("ROOT");
                        eliteNotice.setDr("N");
                        eliteNotice.setIsRead("N");
                        eliteNotice.setNoticeCode(s);
                        eliteNotice.setNoticeName("在线测评");
                        eliteNotice.setNoticeText("您的测评已经提交，请耐心等待店经理审核。");
                    }
                    proEliteNotices.add(eliteNotice);
                }
                ProEliteNoticeClassRecord proEliteNotice = new ProEliteNoticeClassRecord();
                proEliteNotice.setCreatorId("ROOT");
                proEliteNotice.setDr("N");
                proEliteNotice.setIsUse("Y");
                proEliteNotice.setNoticeClassId(proEmployeeVo.getEmployeeCode());
                proEliteNotice.setNoticeTitle("100");
                proEliteNotice.setNoticeText("<p>恭喜您成功报名后备大堂培训班！现将开班事宜通知如下，请认真阅读后务必点击下方“我已知晓并按时参加培训”进行确认。</p><p>一、基本信息</p><p>1、培训天数：20天</p><p>2、培训地点：平谷区塔洼人家度假村</p><p>3、报到地点：天通苑总办附近&nbsp;</p><p>4、报到时间：2017年10月14日08时30分&nbsp;</p><p>二、温馨提醒</p><p>1、请于2017年10月14日08时30分&nbsp;前扫描下方二维码加入培训群;</p><p>&lt;img id='scan' src='' alt=''/ &gt;</p><p>2、请根据天气情况安排好出行，并提前自行购买往返车票，学员的车费以及培训期间产生的费用门店自行承担，报销标准由门店确认；</p><p>3、因培训需要，请务必带上笔记本电脑、（黑色或白色）运动鞋、前后堂两套工服（夏季）、健康证复印件、工牌、洗漱用品及拖鞋，头花（女士），白色T恤和衬衫各一件，对讲机一部（含充电器），笔和笔记本；</p><p>4、培训期间，请各位学员根据天气情况安排好出行；</p><p>5、根据制度要求，学员在培训期间无工资，请知晓；</p><p>6、请提前做好工作交接，安排好门店工作。</p><p>7、请于3天内确认是否参加此次培训，如未按时回复，则需要重新报名。</p><p>我已知晓并按时参加培训</p><p>如不能按时参加，请提前向海大发送邮件申请，各门店不得私自调换人员。</p><p>如对以上通知有任何疑问，请致电海大：李宁， 电话：18001378890  </p>");
                proEliteClassNotices.add(proEliteNotice);
            }
              //查询所有大堂班人员
              List<String> empList=proEliteClassPersonMapper.selectAlllobPerson();
             if(empList.contains(StringUtils.objToString(hashMap.get("employeeCode")))){
                 ProEliteClassPerson eliteClassPerson = new ProEliteClassPerson();
                 eliteClassPerson.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                 eliteClassPerson.setEmployeeId(proEmployeeVo.getId());
                 eliteClassPerson.setModifierId("ROOT");
                 eliteClassPerson.setResult("300");
                 eliteClassPerson.setRemark(StringUtils.objToString(hashMap.get("remark")));
                 eliteClassPerson.setClassId(classNameIdMapping.get(StringUtils.objToString(hashMap.get("eliteClass")).trim()));
                 updateProEliteClassPerson.add(eliteClassPerson);
             }else{
                 String eliteClassPersonId = StringUtils.getTableId("ProEliteClassPerson");
                 ProEliteClassPerson eliteClassPerson = new ProEliteClassPerson();
                 eliteClassPerson.setId(eliteClassPersonId);
                 eliteClassPerson.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                 eliteClassPerson.setEmployeeId(proEmployeeVo.getId());
                 eliteClassPerson.setCreatorId("ROOT");
                 eliteClassPerson.setGroomTime(Long.parseLong("1509429523210"));
                 eliteClassPerson.setResult("300");
                 eliteClassPerson.setRemark(StringUtils.objToString(hashMap.get("remark")));
                 eliteClassPerson.setDr("N");
                 eliteClassPerson.setClassId(classNameIdMapping.get(StringUtils.objToString(hashMap.get("eliteClass")).trim()));
                 proEliteClassPerson.add(eliteClassPerson);
             }
        }
        //批量插入
        if (proEliteClass != null && proEliteClass.size() != 0) {
            proEliteClassMapper.setProEliteClass(proEliteClass);
        }
        if (proShopElites != null && proShopElites.size() != 0) {
            proShopEliteMapper.insertBatch(proShopElites);
        }
        if (proEliteClassPerson != null && proEliteClassPerson.size() != 0 ) {
            proEliteClassPersonMapper.insertBatchClassPerson(proEliteClassPerson);
        }
        if (proEliteNotices != null && proEliteNotices.size() != 0) {
            proEliteNoticeRecordMapper.batchInsertNoticeRecord(proEliteNotices);
        }
        if (proEliteClassNotices != null && proEliteClassNotices.size() != 0 ) {
            proEliteNoticeClassRecordMapper.insertBatchClassRecord(proEliteClassNotices);
        }
        if(updateProEliteClassPerson!=null&&updateProEliteClassPerson.size()!=0){
            proEliteClassPersonMapper.batchUpdateClassPersonListone(updateProEliteClassPerson);
        }
        if(updateProShopElites!=null&&updateProShopElites.size()!=0){
            proShopEliteMapper.batchUpdateEliteList(updateProShopElites);
        }
      return result;
    }
    //导入店经理发证
    @Override
    @Transactional
    public Map<String, Object> getAllClassImport(Map<String, Object> params) {
        //①得到原始数据
        List<HashMap> list = JSON.parseArray(params.get("exportExcel").toString(), HashMap.class);
        //②查询所有班级人员数据
        ProEliteClass proEliteClassParams = new ProEliteClass();
        proEliteClassParams.setClassType("200");
        List<ProEliteClass> proEliteClasses = proEliteClassMapper.getClassByClassType(proEliteClassParams);
        //③查询所有金鹰池人员
        List<String> paramsList = new ArrayList<>();
        List<ProShopElite> proShopEliteList = proShopEliteMapper.getElitesByCodeList(paramsList);
        //③查询条件准备
        Set<String> employeeCodes = new HashSet<>();//员工编号
        Set<String> classNames = new HashSet();//班级名称
        //④映射数据准备
        Map<String, String> classTeacherMapping = new HashMap<>();//班级-老师映射
        Map<String, String> classlecturerMapping = new HashMap<>();//班级-讲师映射
        Map<String, String> timeMapping = new HashMap<>();//班级-开班时间映射
        for (HashMap hashMap : list) {
            if (StringUtils.objToString(hashMap.get("shopName")).equals("门店")) {
                continue;
            }
            classTeacherMapping.put(StringUtils.objToString(hashMap.get("eliteClass")), StringUtils.objToString(hashMap.get("teacherName")));
            classlecturerMapping.put(StringUtils.objToString(hashMap.get("eliteClass")), StringUtils.objToString(hashMap.get("lecturer")));
            timeMapping.put(StringUtils.objToString(hashMap.get("eliteClass")), StringUtils.objToString(hashMap.get("startTime")));
            classNames.add(StringUtils.objToString(hashMap.get("eliteClass")).trim());
            employeeCodes.add(StringUtils.objToString(hashMap.get("employeeCode")));
        }
        //⑤人员名称-->人员对象
        Map<String, ProEmployeeVo> employeeNameMapping = new HashMap<>();
        List<ProEmployeeVo> employees = proEmployeeMapper.queryEmployeeByEmployees(employeeCodes);
        for (ProEmployeeVo employee : employees) {
            employeeNameMapping.put(employee.getEmployeeCode(), employee);
        }
        //⑥查询人员店经理班是否发证  如已发证则 不导入
        //所有已发证人员
        List<String> personCars = proEliteClassPersonMapper.selectAlllodCardPerson();
        List<Map<String, Object>> employeeInfos = new ArrayList<>();//所有需要导入的人员数据
        for (HashMap hashMap : list) {
            boolean flag = true;
//            for (String personCar : personCars) {
//                if (StringUtils.objToString(hashMap.get("employeeCode")).equals(personCar)) {//人员已经存在发证
//                    flag = false;
//                    break;
//                }
//            }
            if (flag) {
                employeeCodes.add(StringUtils.objToString(hashMap.get("employeeCode")));
                employeeInfos.add(hashMap);
            }
        }
        //⑦班级名称-->班级主键
        Map<String, String> classNameIdMapping = new HashMap<>();
        List<ProEliteClass> proEliteClass = new ArrayList<>();//添加班级
        for (String className : classNames) {
            //判断班级是否已经存在。存在则取原有班级
            boolean flag = false;
            for (ProEliteClass eliteClass : proEliteClasses) {
                if (className.equals(eliteClass.getTerm().trim())) {
                    classNameIdMapping.put(className, eliteClass.getId());
                    flag = true;
                    break;
                }
            }
            if (flag) {
                continue;
            }
            //不存在在新建班级
            String eliteClassId = StringUtils.getTableId("eliteClass");
            ProEliteClass p = new ProEliteClass();
            p.setId(eliteClassId);
            p.setClassType("200");
            p.setTerm(className);
            p.setTeacherName(classTeacherMapping.get(className));
            p.setTraineeNum(30);
//            p.setReportLocaltion("");
//            p.setBeginTime();系统导入时间
//            p.setReportTime(Long.parseLong(getTime(timeMapping.get(className)==null?"1900-01-01":timeMapping.get(className))));
            p.setAssistantTeacherName(classlecturerMapping.get(className));
            p.setCreatorId("ROOT");
            p.setDr("N");
            classNameIdMapping.put(className, eliteClassId);//插入班级名称--班级主键映射
            proEliteClass.add(p);
        }
        //⑧导入数据
        List<ProShopElite> proShopElites = new ArrayList<>();//门店精英添加
        List<ProEliteClassPerson> proEliteClassPerson = new ArrayList<>();//添加班级人员
        List<ProEliteClassPerson> updateProEliteClassPerson=new ArrayList<>();//更新班级人员
        List<ProShopElite>  updateProShopElites=new ArrayList<>();//更新门店精英
        //查询所有店经理班人员
        List<String> empList=proEliteClassPersonMapper.selectAllManyPerson();
        for(Map<String,Object> hashMap:employeeInfos) {
            if (StringUtils.objToString(hashMap.get("shopName")).equals("所在门店")) {
                continue;
            }
            String shopEliteId = StringUtils.getTableId("ProShopElite");
            ProEmployeeVo proEmployeeVo = employeeNameMapping.get(StringUtils.objToString(hashMap.get("employeeCode")));
            if (proEmployeeVo == null) {
                continue;
            }
            boolean flag = true;//默认不存在
            for (ProShopElite proShopElite : proShopEliteList) {
                if (proShopElite.getEmployeeCode().equals(proEmployeeVo.getEmployeeCode())) {
                    flag = false;
                    break;
                }
            }
            if(flag&&StringUtils.objToString(hashMap.get("isCard")).equals("发证")){
                ProShopElite shopElite = new ProShopElite();
                shopElite.setId(shopEliteId);
                shopElite.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                shopElite.setEmployeeId(proEmployeeVo.getId());
                shopElite.setShopId(proEmployeeVo.getShopId());
                shopElite.setClassStatus("300");
                shopElite.setEliteStatus("400");
                shopElite.setIsLobby("Y");
                shopElite.setTestRecordId("ROOT");
                shopElite.setDr("N");
                shopElite.setTs(Long.parseLong("1509429523210"));
                proShopElites.add(shopElite);
            }else if(flag&&StringUtils.objToString(hashMap.get("isCard")).equals("不发证")){

            }else if(!flag&&StringUtils.objToString(hashMap.get("isCard")).equals("发证")){
                ProShopElite shopElite = new ProShopElite();
                shopElite.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                shopElite.setEmployeeId(proEmployeeVo.getId());
                shopElite.setClassStatus("300");
                shopElite.setEliteStatus("400");
                shopElite.setIsLobby("Y");
                shopElite.setElitePoolTime(Long.parseLong("1509429523210"));
                shopElite.setTs(Long.parseLong("1509429523210"));
                updateProShopElites.add(shopElite);

            }else if(!flag&&StringUtils.objToString(hashMap.get("isCard")).equals("不发证")){
                ProShopElite shopElite = new ProShopElite();
                shopElite.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                shopElite.setEmployeeId(proEmployeeVo.getId());
                shopElite.setClassStatus("100");
                shopElite.setEliteStatus("300");
                shopElite.setIsLobby("Y");
                shopElite.setElitePoolTime(Long.parseLong("1509429523210"));
                shopElite.setTs(Long.parseLong("1509429523210"));
                updateProShopElites.add(shopElite);
            }

            if(empList.contains(StringUtils.objToString(hashMap.get("employeeCode")))){//如果存在则更新
                ProEliteClassPerson eliteClassPerson = new ProEliteClassPerson();
                eliteClassPerson.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                eliteClassPerson.setEmployeeId(proEmployeeVo.getId());
                eliteClassPerson.setCreatorId("ROOT");
                eliteClassPerson.setGroomTime(Long.parseLong("1509429523210"));
                if(StringUtils.objToString(hashMap.get("isCard")).equals("发证")){
                    eliteClassPerson.setResult("300");
                }else{
                    eliteClassPerson.setResult("200");
                }
                eliteClassPerson.setRemark(StringUtils.objToString(hashMap.get("remark")));
                eliteClassPerson.setDr("N");
                eliteClassPerson.setClassId(classNameIdMapping.get(StringUtils.objToString(hashMap.get("eliteClass")).trim()));
                updateProEliteClassPerson.add(eliteClassPerson);
            }else{
                ProEliteClassPerson eliteClassPerson = new ProEliteClassPerson();
                String eliteClassPersonId = StringUtils.getTableId("ProEliteClassPerson");
                eliteClassPerson.setId(eliteClassPersonId);
                eliteClassPerson.setEmployeeCode(proEmployeeVo.getEmployeeCode());
                eliteClassPerson.setEmployeeId(proEmployeeVo.getId());
                eliteClassPerson.setCreatorId("ROOT");
                eliteClassPerson.setGroomTime(Long.parseLong("1509429523210"));
                if(StringUtils.objToString(hashMap.get("isCard")).equals("发证")){
                    eliteClassPerson.setResult("300");
                }else{
                    eliteClassPerson.setResult("200");
                }
                eliteClassPerson.setRemark(StringUtils.objToString(hashMap.get("remark")));
                eliteClassPerson.setDr("N");
                eliteClassPerson.setClassId(classNameIdMapping.get(StringUtils.objToString(hashMap.get("eliteClass")).trim()));
                proEliteClassPerson.add(eliteClassPerson);
            }
        }
        //批量插入
        if (proEliteClass != null && proEliteClass.size() != 0) {
            proEliteClassMapper.setProEliteClass(proEliteClass);
        }
        if (proShopElites != null && proShopElites.size() != 0) {
            proShopEliteMapper.insertBatch(proShopElites);
        }
        if (proEliteClassPerson != null && proEliteClassPerson.size() != 0 ) {
            proEliteClassPersonMapper.insertBatchClassPerson(proEliteClassPerson);
        }
        if(updateProEliteClassPerson!=null&&updateProEliteClassPerson.size()!=0){
            proEliteClassPersonMapper.batchUpdateClassPersonList(updateProEliteClassPerson);
        }
        if(updateProShopElites!=null&&updateProShopElites.size()!=0){
            proShopEliteMapper.batchUpdateEliteList(updateProShopElites);
        }
        Map<String, Object> result = new HashMap<>();
        System.out.println("插入班级"+proEliteClass.size()+"插入金鹰池"+proShopElites.size()+"插入经理班"+proEliteClassPerson.size());
        System.out.println("跟新经理班"+updateProEliteClassPerson.size()+"更新金鹰池"+updateProShopElites.size());
        System.out.println("导入数据"+list.size());
        System.out.println("导入数据"+employeeInfos.size());
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> setExportCardPerson(Map<String, Object> params) {
        Map<String,Object> resultMap=new HashMap<>();
        //得到原始数据
        List<HashMap> list = JSON.parseArray(params.get("exportExcel").toString(), HashMap.class);
        List<Map<String,Object>> updateClassPerson=new ArrayList<>();
        List<ProEliteClassPerson> updateEliteClassPerson=new ArrayList<>();
        List<ProEliteClassPersonProject> insertProject=new ArrayList<>();
        Map<String,Object>   mapObject=new HashMap<>();
        Map<String,String>   mapCount=new HashMap<>();//总得分
        Map<String,String>   mapRemark=new HashMap<>();//评价
        Set<String>  setEmployee=new HashSet<>();//职员代码
        Set<String>  setClass=new HashSet<>();//查询班级
        Map<String,String>   mapEmployeeMapping=new HashMap<>();//人员代码跟class_person映射
        for (HashMap hashMap : list) {
            if(!StringUtils.objToString(hashMap.get("职员代码")).equals("")){
                mapObject.put(StringUtils.objToString(hashMap.get("职员代码")).trim(),hashMap);
            }
            if(!StringUtils.objToString(hashMap.get("综合得分")).equals("")){
                mapCount.put(StringUtils.objToString(hashMap.get("职员代码")).trim(),StringUtils.objToString(hashMap.get("综合得分")).trim());
            }
            if(!StringUtils.objToString(hashMap.get("备注")).equals("")){
                mapRemark.put(StringUtils.objToString(hashMap.get("职员代码")),StringUtils.objToString(hashMap.get("备注")));
            }
            setEmployee.add(StringUtils.objToString(hashMap.get("职员代码")).trim());
            setClass.add(StringUtils.objToString(hashMap.get("班级")).trim());
        }
        if(setClass.size()>1){
            resultMap.put("flag","1");
            resultMap.put("result","存在多个班级");
            return resultMap;
        }
        //通过班级，查询class_person  表主键
        List<ProEliteClassPerson>  proEliteClassPerson= proEliteClassPersonMapper.getClassPersonsList(setEmployee,setClass.toArray()[0].toString());
        if(proEliteClassPerson.size()==0){
            resultMap.put("flag","1");
            resultMap.put("result","该班级需要导入的人员数为0，请核对");
            return resultMap;
        }
        String result="";
        for (ProEliteClassPerson eliteClassPerson : proEliteClassPerson) {
            eliteClassPerson.setScore(mapCount.get(eliteClassPerson.getEmployeeCode()));
            eliteClassPerson.setRemark(mapRemark.get(eliteClassPerson.getEmployeeCode()));
            updateEliteClassPerson.add(eliteClassPerson);
            //职员代码跟员工映射
            mapEmployeeMapping.put(eliteClassPerson.getEmployeeCode(),eliteClassPerson.getId());

//            if(eliteClassPerson.getResult().equals("300")){
//                result+="编号"+eliteClassPerson.getEmployeeCode()+"已发证不可导入\r\n";
//            }
        }
        if(result.length()>0){
            resultMap.put("flag","1");
            resultMap.put("result",result);
        }
        for(HashMap hashMap : list){
            ProEliteClassPersonProject p=new ProEliteClassPersonProject();
//            String  id=StringUtils.getTableId("ProEliteClassPersonProject");
            String id=UUID.randomUUID().toString().replaceAll("-", "");
            p.setId(id);
            p.setCardproject(StringUtils.objToString(hashMap.get("发证项目")));
            p.setCardtype(StringUtils.objToString(hashMap.get("发证大类")));
            p.setRemark(StringUtils.objToString(hashMap.get("评价")));
            p.setScore(StringUtils.objToString(hashMap.get("得分")));
            p.setCreateTime(System.currentTimeMillis());
            p.setCreatorId("system");
            p.setParentId(mapEmployeeMapping.get(StringUtils.objToString(hashMap.get("职员代码"))));
            p.setDr("N");
            insertProject.add(p);
        }
//        StringUtils.objToString(hashMap.get("班级名称"));
//        StringUtils.objToString(hashMap.get("人员名称"));
//        StringUtils.objToString(hashMap.get("职员代码"));
//        StringUtils.objToString(hashMap.get("评价"));
//        StringUtils.objToString(hashMap.get("总得分"));
//        StringUtils.objToString(hashMap.get("发证项目"));
//        StringUtils.objToString(hashMap.get("发证大类"));
        //删除人员评价

        if(updateEliteClassPerson.size()>0){//更新班级人员
            proEliteClassPersonMapper.batchUpdateImportClassPerson(updateEliteClassPerson);
        }
        if(insertProject.size()>0){//插入人员评价
            proEliteClassPersonProjectMapper.batchDeleteProELiteClassPerson(updateEliteClassPerson);
            proEliteClassPersonProjectMapper.setAllProELiteClassPerson(insertProject);
        }
        resultMap.put("flag","0");
        resultMap.put("result","导入成功");
        //导入发证人员
        return resultMap;
    }


    /**
     * 将字符串转换成时间戳
     *
     * @param user_time
     * @return
     */
    private String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re_time;
    }
}
