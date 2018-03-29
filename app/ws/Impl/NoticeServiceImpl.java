package ws.Impl;

import com.google.inject.Inject;
import com.hht.utils.IDGenerater;
import common.Constant;
import models.*;
import models.vo.ProEmployeeVo;
import org.mybatis.guice.transactional.Transactional;
import play.Configuration;
import service.*;
import utils.StringUtils;
import ws.NoticeService;

import java.util.*;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/25 18:41
 */
public class NoticeServiceImpl implements NoticeService {

    @Inject
    private EmployeeService employeeService;

    @Inject
    private EliteNoticeService noticeService;

    @Inject
    private EliteTrainCostService trainCostService;

    @Inject
    private EliteNoticeRecordService noticeRecordService;

    @Inject
    private WeinxinUtilService weinxinUtilService;
    private List<Map<String, Object>> mapList = null;

    @Override
    @Transactional
    public String sendNotice(String employeeCode, String noticeCode, boolean isShopManager, ProEliteClass eliteClass) {
        try {
            List<ProEmployeeVo> employees = employeeService.queryByCode(employeeCode);
            ProEmployeeVo employee = employees.get(0);
            ProEliteTrainCost cost = trainCostService.getProEliteTrainCosts();
            List<ProEmployeeVo> employeeManager = null;
            Map<String, Object> params = new HashMap<>();
            params.put("noticeCode", noticeCode);
            //默认通知店员
            String noticeRole = "200";
            if (isShopManager) {
                noticeRole = "100";

                //根据shopId查询该店的店经理
                employeeManager = employeeService.getShopManagerByShopId(employee.getShopId());
            }
            params.put("noticeRole", noticeRole);
            String noticeText = "";
            String noticeTitle;
            ProEliteNotice notice = noticeService.queryNoticeByNoticeCode(params);
            List<ProEmployeeVo> list = new ArrayList<>();
            list.add(employee);
            noticeText = getNoticeText(notice.getNoticeContent(), list, eliteClass, cost);
            List<ProEliteNoticeRecord> records = new ArrayList<>();
            noticeTitle = notice.getNoticeTitle();
            if ("200".equals(noticeRole)) {
                ProEliteNoticeRecord noticeRecord = new ProEliteNoticeRecord();
                noticeRecord.setDr("N");
                IDGenerater idGenerater = new IDGenerater();
                String id = idGenerater.nextId();
                noticeRecord.setId(id);
                noticeRecord.setCreateTime(System.currentTimeMillis());
                noticeRecord.setCreatorId("system");
                noticeRecord.setEmployeeCode(employee.getEmployeeCode());
                noticeRecord.setEmployeeId(employee.getId());
                noticeRecord.setIsRead("N");
                noticeRecord.setNoticeCode(noticeCode);
                noticeRecord.setNoticeText(noticeText);
                noticeRecord.setNoticeName(noticeTitle);
                records.add(noticeRecord);
            } else {
                for (ProEmployeeVo employeeVo : employeeManager) {
                    ProEliteNoticeRecord noticeRecord = new ProEliteNoticeRecord();
                    noticeRecord.setDr("N");
                    IDGenerater idGenerater = new IDGenerater();
                    String id = idGenerater.nextId();
                    noticeRecord.setId(id);
                    noticeRecord.setCreateTime(System.currentTimeMillis());
                    noticeRecord.setCreatorId("system");
                    noticeRecord.setEmployeeCode(employee.getEmployeeCode());
                    noticeRecord.setEmployeeId(employee.getId());
                    noticeRecord.setIsRead("N");
                    noticeRecord.setNoticeCode(noticeCode);
                    noticeRecord.setNoticeText(noticeText);
                    noticeRecord.setNoticeName(noticeTitle);
                    noticeRecord.setEmployeeCode(employeeVo.getEmployeeNo());
                    noticeRecord.setEmployeeId(employeeVo.getId());
                    records.add(noticeRecord);
                }
            }
            noticeRecordService.batchInsertNoticeRecord(records);
//            String systemId = Configuration.root().getString("systemId");
            //TODO 调用微信通知接口
            if ("200".equals(noticeRole)) {
                weinxinUtilService.sendWX(Constant.SYSTEMID, employee.getEmployeeCode(), noticeText);
            } else {
                for (ProEmployeeVo vo : employeeManager) {
                    weinxinUtilService.sendWX(Constant.SYSTEMID, vo.getEmployeeNo(), noticeText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "FALSE";
        }
        return "SUCCESS";
    }

    @Override
    public String sendNoticeNotPass(String employeeCode, String noticeCode, boolean isShopManager, ProEliteClass eliteClass, ProEliteClassPerson proEliteClassPerson) {
        try {
            List<ProEmployeeVo> employees = employeeService.queryByCode(employeeCode);
            ProEmployeeVo employee = employees.get(0);
            ProEliteTrainCost cost = trainCostService.getProEliteTrainCosts();
            List<ProEmployeeVo> employeeManager = null;
            Map<String, Object> params = new HashMap<>();
            params.put("noticeCode", noticeCode);
            //默认通知店员
            String noticeRole = "200";
            if (isShopManager) {
                noticeRole = "100";
                    //根据shopId查询该店的店经理
                    employeeManager = employeeService.getShopManagerByShopId(employee.getShopId());
            }
            params.put("noticeRole", noticeRole);
            String noticeText = "";
            String noticeTitle;
            ProEliteNotice notice = noticeService.queryNoticeByNoticeCode(params);
            List<ProEmployeeVo> list = new ArrayList<>();
            list.add(employee);
            noticeText = getNoticeTextNotPass(notice.getNoticeContent(), list, eliteClass, cost, proEliteClassPerson);
            noticeTitle = notice.getNoticeTitle();
            List<ProEliteNoticeRecord> records = new ArrayList<>();
            if ("200".equals(noticeRole)) {
                ProEliteNoticeRecord noticeRecord = new ProEliteNoticeRecord();
                noticeRecord.setDr("N");
                IDGenerater idGenerater = new IDGenerater();
                String id = idGenerater.nextId();
                noticeRecord.setId(id);
                noticeRecord.setCreateTime(System.currentTimeMillis());
                noticeRecord.setCreatorId("system");
                noticeRecord.setEmployeeCode(employee.getEmployeeCode());
                noticeRecord.setEmployeeId(employee.getId());
                noticeRecord.setIsRead("N");
                noticeRecord.setNoticeCode(noticeCode);
                noticeRecord.setNoticeText(noticeText);
                noticeRecord.setNoticeName(noticeTitle);
                records.add(noticeRecord);
            } else {
                for (ProEmployeeVo employeeVo : employeeManager) {
                    ProEliteNoticeRecord noticeRecord = new ProEliteNoticeRecord();
                    noticeRecord.setDr("N");
                    IDGenerater idGenerater = new IDGenerater();
                    String id = idGenerater.nextId();
                    noticeRecord.setId(id);
                    noticeRecord.setCreateTime(System.currentTimeMillis());
                    noticeRecord.setCreatorId("system");
                    noticeRecord.setEmployeeCode(employee.getEmployeeCode());
                    noticeRecord.setEmployeeId(employee.getId());
                    noticeRecord.setIsRead("N");
                    noticeRecord.setNoticeCode(noticeCode);
                    noticeRecord.setNoticeText(noticeText);
                    noticeRecord.setNoticeName(noticeTitle);
                    noticeRecord.setEmployeeCode(employeeVo.getEmployeeNo());
                    noticeRecord.setEmployeeId(employeeVo.getId());
                    records.add(noticeRecord);
                }
            }
            noticeRecordService.batchInsertNoticeRecord(records);
//            String systemId = Configuration.root().getString("systemId");
            //TODO 调用微信通知接口
            if ("200".equals(noticeRole)) {
                weinxinUtilService.sendWX(Constant.SYSTEMID, employee.getEmployeeCode(), noticeText);
            } else {
                for (ProEmployeeVo vo : employeeManager) {
                    weinxinUtilService.sendWX(Constant.SYSTEMID, vo.getEmployeeNo(), noticeText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "FALSE";
        }
        return "SUCCESS";
    }


    List<Map<String, String>> strList = null;//保存员工code  name  对应店经理code
    Set<String> set = null;//保存店经理Code

    @Override
    public String sendNoticeByCodeList(List employeeCodeList, String noticeCode, boolean isShopManager, ProEliteClass eliteClass) {
        String result = "";
        return result = sendNoticeForList(employeeCodeList, noticeCode, isShopManager, eliteClass);
    }
//    public String sendNoticeByCodeList(List employeeCodeList, String noticeCode, boolean isShopManager, ProEliteClass eliteClass) {
//        sendNoticeForList
//        String result = "";
//        try {
//            List<Map<String, String>> strList = new ArrayList<>();//保存员工code  name  对应店经理code
//            Set<String> set = new HashSet();//保存店经理Code
//            for (int i = 0; i < employeeCodeList.size(); i++) {
//                Map map = new HashMap();
//                ProEmployeeVo employee = employeeService.queryByCode(employeeCodeList.get(i).toString());
//                ProEmployeeVo employeeManager = employeeService.getShopManagerByShopId(employee.getShopId());//查询店经理
//                set.add(employeeManager.getEmployeeCode());
//                map.put("employeeCode", employeeCodeList.get(i).toString());
//                map.put("employeeName", employee.getEmployeeName());
//                map.put("employeeManagerCode", employeeManager.getEmployeeCode());
//                strList.add(map);
//            }
//            for (String s : set) {
//                result = sendNotice(s, noticeCode, true, eliteClass);
//            }
//        } catch (Exception e) {
//            return result;
//        }
//        return result;
//    }

    @Override
    public String sendNoticeAu(String employeeCode, String noticeCode, boolean isShopManager, String remark) {

        try {
            List<ProEmployeeVo> employees = employeeService.queryByCode(employeeCode);
            ProEmployeeVo employee = employees.get(0);
            Map<String, Object> params = new HashMap<>();
            params.put("noticeCode", noticeCode);
            List<ProEmployeeVo> employeeManager = null;
            //默认通知店员
            String noticeRole = "200";
            if (isShopManager) {
                noticeRole = "100";

                    //根据shopId查询该店的店经理
                employeeManager = employeeService.getShopManagerByShopId(employee.getShopId());
            }
            params.put("noticeRole", noticeRole);
            String noticeText = "";
            String noticeTitle = "";
            ProEliteNotice notice = noticeService.queryNoticeByNoticeCode(params);
            noticeText = getNoticeText(notice.getNoticeContent(), employee, remark);
            noticeTitle = notice.getNoticeTitle();
            List<ProEliteNoticeRecord> records = new ArrayList<>();
            //通知店经理 店员未通过原因
            for (ProEmployeeVo employeeVo : employeeManager) {
                ProEliteNoticeRecord noticeRecord = new ProEliteNoticeRecord();
                noticeRecord.setDr("N");
                IDGenerater idGenerater = new IDGenerater();
                String id = idGenerater.nextId();
                noticeRecord.setId(id);
                noticeRecord.setCreateTime(System.currentTimeMillis());
                noticeRecord.setCreatorId("system");
                noticeRecord.setEmployeeCode(employee.getEmployeeCode());
                noticeRecord.setEmployeeId(employee.getId());
                noticeRecord.setIsRead("N");
                noticeRecord.setNoticeCode(noticeCode);
                noticeRecord.setNoticeText(noticeText);
                noticeRecord.setNoticeName(noticeTitle);
                noticeRecord.setEmployeeCode(employeeVo.getEmployeeNo());
                noticeRecord.setEmployeeId(employeeVo.getId());
                records.add(noticeRecord);
            }
            noticeRecordService.batchInsertNoticeRecord(records);
//            String systemId = Configuration.root().getString("systemId");
            //调用微信通知接口
            for (ProEmployeeVo vo : employeeManager) {
                weinxinUtilService.sendWX(Constant.SYSTEMID, vo.getEmployeeNo(), noticeText);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "FALSE";
        }
        return "SUCCESS";
    }

    private String getMoreInfo(String employeeManager) {
        String result = "";
        if (strList != null) {
            for (Map<String, String> stringStringMap : strList) {
                if (stringStringMap.get("employeeManagerCode").equals("employeeManager")) {
                    result += stringStringMap.get("employeeName");
                }
            }
        }
        return result;
    }

    private String getNoticeTextNotPass(String noticeContent, List<ProEmployeeVo> list, ProEliteClass eliteClass, ProEliteTrainCost cost, ProEliteClassPerson proEliteClassPerson) {
        if (noticeContent.contains("{#报到时间#}")) {
            noticeContent = noticeContent.replace("{#报到时间#}", StringUtils.longToDate(eliteClass.getReportTime(), "yyyy年MM月dd日"));
        }
        if (noticeContent.contains("{#报到地点#}")) {
            noticeContent = noticeContent.replace("{#报到地点#}", eliteClass.getReportLocaltion());
        }
        if (noticeContent.contains("{#天数#}")) {
            noticeContent = noticeContent.replace("{#天数#}", eliteClass.getTrainDays().toString());
        }
        if (noticeContent.contains("{#店员#}")) {
            noticeContent = noticeContent.replace("{#店员#}", list.get(0).getEmployeeName());
        }
        if (noticeContent.contains("{#培训时间#}")) {
            noticeContent = noticeContent.replace("{#培训时间#}", StringUtils.longToDate(eliteClass.getBeginTime(), "yyyy年MM月dd日"));
        }
        if (noticeContent.contains("{#培训地点#}")) {
            noticeContent = noticeContent.replace("{#培训地点#}", eliteClass.getTrainPlace());
        }
        if (noticeContent.contains("{#测评经理需缴纳费用#}")) {
            noticeContent = noticeContent.replace("{#测评经理需缴纳费用#}", cost.getEvaluatingManagerCost().toString());
        }
        if (noticeContent.contains("{#测评店员需缴纳费用#}")) {
            noticeContent = noticeContent.replace("{#测评店员需缴纳费用#}", cost.getEvaluatingEmployeeCost().toString());
        }
        if (noticeContent.contains("{#大堂班经理需缴纳费用#}")) {
            noticeContent = noticeContent.replace("{#大堂班经理需缴纳费用#}", cost.getLobbyManagerCost().toString());
        }
        if (noticeContent.contains("{#大堂班店员需缴纳费用#}")) {
            noticeContent = noticeContent.replace("{#大堂班店员需缴纳费用#}", cost.getLobbyEmployeeCost().toString());
        }
        if (noticeContent.contains("{#经理班经理需缴纳费用#}")) {
            noticeContent = noticeContent.replace("{#经理班经理需缴纳费用#}", cost.getReserveManagerCost().toString());
        }
        if (noticeContent.contains("{#经理班店员需缴纳费用#}")) {
            noticeContent = noticeContent.replace("{#经理班店员需缴纳费用#}", cost.getReserveEmployeeCost().toString());
        }
        if (noticeContent.contains("{#经理班经理罚款金额#}")) {
            noticeContent = noticeContent.replace("{#经理班经理罚款金额#}", cost.getReserveManagerAmerce().toString());
        }
        if (noticeContent.contains("{#经理班店员罚款金额#}")) {
            noticeContent = noticeContent.replace("{#经理班店员罚款金额#}", cost.getReserveEmployeeAmerce().toString());
        }
        if (noticeContent.contains("{#学期#}")) {
            noticeContent = noticeContent.replace("{#学期#}", eliteClass.getTerm());
        }
        if (noticeContent.contains("{#原因#}")) {
            noticeContent = noticeContent.replace("{#原因#}", proEliteClassPerson.getRemark());
        }
//        if (noticeContent.contains("{#学期#}")) {
//            noticeContent = noticeContent.replace("{#学期#}", eliteClass.getClassType());
//        }
        if (noticeContent.contains("{#多店员#}")) {
            String text = "";
            for (ProEmployeeVo vo : list) {
                text += vo.getEmployeeName();
            }
            noticeContent = noticeContent.replace("{#多店员#}", text);
        }
        return noticeContent;
    }

    private String getNoticeText(String noticeContent, List<ProEmployeeVo> list, ProEliteClass eliteClass, ProEliteTrainCost cost) {
        if (noticeContent.contains("{#报到时间#}")) {
            noticeContent = noticeContent.replace("{#报到时间#}", StringUtils.longToDate(eliteClass.getReportTime(), "yyyy年MM月dd日"));
        }
        if (noticeContent.contains("{#报到地点#}")) {
            noticeContent = noticeContent.replace("{#报到地点#}", eliteClass.getReportLocaltion());
        }
        if (noticeContent.contains("{#天数#}")) {
            noticeContent = noticeContent.replace("{#天数#}", eliteClass.getTrainDays().toString());
        }
        if (noticeContent.contains("{#店员#}")) {
            noticeContent = noticeContent.replace("{#店员#}", list.get(0).getEmployeeName());
        }
        if (noticeContent.contains("{#培训时间#}")) {
            noticeContent = noticeContent.replace("{#培训时间#}", StringUtils.longToDate(eliteClass.getBeginTime(), "yyyy年MM月dd日"));
        }
        if (noticeContent.contains("{#培训地点#}")) {
            noticeContent = noticeContent.replace("{#培训地点#}", eliteClass.getTrainPlace());
        }
        if (noticeContent.contains("{#测评经理需缴纳费用#}")) {
            noticeContent = noticeContent.replace("{#测评经理需缴纳费用#}", cost.getEvaluatingManagerCost().toString());
        }
        if (noticeContent.contains("{#测评店员需缴纳费用#}")) {
            noticeContent = noticeContent.replace("{#测评店员需缴纳费用#}", cost.getEvaluatingEmployeeCost().toString());
        }
        if (noticeContent.contains("{#大堂班经理需缴纳费用#}")) {
            noticeContent = noticeContent.replace("{#大堂班经理需缴纳费用#}", cost.getLobbyManagerCost().toString());
        }
        if (noticeContent.contains("{#大堂班店员需缴纳费用#}")) {
            noticeContent = noticeContent.replace("{#大堂班店员需缴纳费用#}", cost.getLobbyEmployeeCost().toString());
        }
        if (noticeContent.contains("{#经理班经理需缴纳费用#}")) {
            noticeContent = noticeContent.replace("{#经理班经理需缴纳费用#}", cost.getReserveManagerCost().toString());
        }
        if (noticeContent.contains("{#经理班店员需缴纳费用#}")) {
            noticeContent = noticeContent.replace("{#经理班店员需缴纳费用#}", cost.getReserveEmployeeCost().toString());
        }
        if (noticeContent.contains("{#经理班经理罚款金额#}")) {
            noticeContent = noticeContent.replace("{#经理班经理罚款金额#}", cost.getReserveManagerAmerce().toString());
        }
        if (noticeContent.contains("{#经理班店员罚款金额#}")) {
            noticeContent = noticeContent.replace("{#经理班店员罚款金额#}", cost.getReserveEmployeeAmerce().toString());
        }
        if (noticeContent.contains("{#学期#}")) {
            noticeContent = noticeContent.replace("{#学期#}", eliteClass.getTerm());
        }
//        if (noticeContent.contains("{#学期#}")) {
//            noticeContent = noticeContent.replace("{#学期#}", eliteClass.getClassType());
//        }
        if (noticeContent.contains("{#多店员#}")) {
            String text = "";
            for (ProEmployeeVo vo : list) {
                text += vo.getEmployeeName();
            }
            noticeContent = noticeContent.replace("{#多店员#}", text);
        }
        return noticeContent;
    }

    /**
     * 通知店经理  新店辅导班审核未通过原因
     *
     * @return
     */
    private String getNoticeText(String noticeContent, ProEmployeeVo employee, String remark) {

        if (noticeContent.contains("{#店员#}")) {
            noticeContent = noticeContent.replace("{#店员#}", employee.getEmployeeName());
        }
        if (noticeContent.contains("{#后备店经理审核不通过原因#}")) {
            noticeContent = noticeContent.replace("{#后备店经理审核不通过原因#}", remark);
        }
        return noticeContent;
    }

    public String sendNoticeForList(List<String> employeeCodeList, String noticeCode, boolean flag, ProEliteClass eliteClass) {
        try {
            List<ProEmployeeVo> list = employeeService.queryListByCodes(employeeCodeList);
            Set<String> set = new HashSet<>();
            for (ProEmployeeVo vo : list) {
                set.add(vo.getShopId());
            }
            List<ProEmployeeVo> managers = employeeService.queryListByShopIds(set);
            Map<ProEmployeeVo, List<ProEmployeeVo>> map = new HashMap<>();
            for (ProEmployeeVo manager : managers) {
                List<ProEmployeeVo> temp = new ArrayList<>();
                for (ProEmployeeVo employeeVo : list) {
                    if (manager.getShopId().equals(employeeVo.getShopId())) {
                        temp.add(employeeVo);
                    }
                }
                map.put(manager, temp);
            }

            ProEliteTrainCost cost = trainCostService.getProEliteTrainCosts();
            Map<String, Object> params = new HashMap<>();
            params.put("noticeCode", noticeCode);
            //默认通知店员
            String noticeRole = "100";
            params.put("noticeRole", noticeRole);
            String noticeText = "";
            String noticeTitle;
            ProEliteNotice notice = noticeService.queryNoticeByNoticeCode(params);
            noticeTitle = notice.getNoticeTitle();
            List<ProEliteNoticeRecord> recordList = new ArrayList<>();
            for (ProEmployeeVo employeeVo : map.keySet()) {
                ProEmployeeVo employeeManager = employeeVo;
                ProEliteNoticeRecord noticeRecord = new ProEliteNoticeRecord();
                noticeRecord.setDr("N");
                noticeText = getNoticeText(notice.getNoticeContent(), map.get(employeeVo), eliteClass, cost);
                noticeRecord.setId(StringUtils.getTableId("pro_elite_notice_record"));
                noticeRecord.setCreateTime(System.currentTimeMillis());
                noticeRecord.setCreatorId("system");
                noticeRecord.setEmployeeCode(employeeManager.getEmployeeCode());
                noticeRecord.setEmployeeId(employeeManager.getId());
                noticeRecord.setIsRead("N");
                noticeRecord.setNoticeCode(noticeCode);
                noticeRecord.setNoticeText(noticeText);
                noticeRecord.setNoticeName(noticeTitle);
                recordList.add(noticeRecord);
            }
            noticeRecordService.batchInsertNoticeRecord(recordList);
//            String systemId = Configuration.root().getString("systemId");
            //TODO 调用微信通知接口
            for (Map.Entry<ProEmployeeVo, List<ProEmployeeVo>> entry : map.entrySet()) {
                ProEmployeeVo employeeManager = entry.getKey();
                noticeText = getNoticeText(notice.getNoticeContent(), map.get(employeeManager), eliteClass, cost);
                weinxinUtilService.sendWX(Constant.SYSTEMID, employeeManager.getEmployeeCode(), noticeText);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return "FALSE";
        }
        return "SUCCESS";
    }

    public static void main(String[] args) {
        String systemId = Configuration.root().getString("systemId");
        System.out.println(systemId);
    }

    @Override
    public void sendNoticeForExam(ProEliteExaminationOnline examinationOnline, String noticeCode, boolean b) {
        try {
            List<ProEmployeeVo> employees = employeeService.queryByCode(examinationOnline.getEmployeeCode());
            ProEmployeeVo employee = employees.get(0);
            ProEliteTrainCost cost = trainCostService.getProEliteTrainCosts();
            ProEmployeeVo employeeManager = null;
            Map<String, Object> params = new HashMap<>();
            params.put("noticeCode", noticeCode);
            //默认通知店员
            String noticeRole = "200";
            params.put("noticeRole", noticeRole);
            String noticeText = "";
            String noticeTitle;
            ProEliteNotice notice = noticeService.queryNoticeByNoticeCode(params);
            List<ProEmployeeVo> list = new ArrayList<>();
            list.add(employee);
            noticeText = notice.getNoticeContent();
            noticeText = noticeText.replace("{#考试分数#}", examinationOnline.getRelultScore().toString());
            noticeText = noticeText.replace("{#合格分数#}", examinationOnline.getPassScore().toString());
            noticeTitle = notice.getNoticeTitle();
            ProEliteNoticeRecord noticeRecord = new ProEliteNoticeRecord();
            noticeRecord.setDr("N");
            noticeRecord.setId(StringUtils.getTableId("pro_elite_notice_record"));
            noticeRecord.setCreateTime(System.currentTimeMillis());
            noticeRecord.setCreatorId("system");
            noticeRecord.setEmployeeCode(employee.getEmployeeCode());
            noticeRecord.setEmployeeId(employee.getId());
            noticeRecord.setIsRead("N");
            noticeRecord.setNoticeCode(noticeCode);
            noticeRecord.setNoticeText(noticeText);
            noticeRecord.setNoticeName(noticeTitle);
            noticeRecordService.insertNoticeRecord(noticeRecord);
//            String systemId = Configuration.root().getString("systemId");
            //TODO 调用微信通知接口
            weinxinUtilService.sendWX(Constant.SYSTEMID, employee.getEmployeeCode(), noticeText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendCoachNotice(String employeeCode, String noticeCode, ProEliteClass elite) {
        try {
            ProEliteClass eliteClass = new ProEliteClass();
            //查询员工信息
            List<ProEmployeeVo> employees = employeeService.queryByCode(employeeCode);
            ProEmployeeVo employee = employees.get(0);
            //查询所有教练
            List<ProEmployeeVo> employeeVoList = employeeService.getCoach();
            //查询缴费信息
            ProEliteTrainCost cost = trainCostService.getProEliteTrainCosts();
            //查询教练信息
            ProEmployeeVo employeeCoach = null;
            Map<String, Object> params = new HashMap<>();
            params.put("noticeCode", noticeCode);
            //查询教练信息
//            employeeCoach = employeeService.getShopManagerByShopId(employee.getShopId());
            params.put("noticeRole", 100);
            String noticeText = "";
            String noticeTitle;
            ProEliteNotice notice = noticeService.queryNoticeByNoticeCode(params);
            List<ProEmployeeVo> list = new ArrayList<>();
            list.add(employee);

            List<ProEliteNoticeRecord> records = new ArrayList<>();
            for (ProEmployeeVo proEmployeeVo : employeeVoList) {
                noticeText = getNoticeText(notice.getNoticeContent(), list, eliteClass, cost);
                noticeTitle = notice.getNoticeTitle();
                ProEliteNoticeRecord noticeRecord = new ProEliteNoticeRecord();
                noticeRecord.setDr("N");
                IDGenerater idGenerater = new IDGenerater();
                String id = idGenerater.nextId();
                noticeRecord.setId(id);
                noticeRecord.setCreateTime(System.currentTimeMillis());
                noticeRecord.setCreatorId("system");
                noticeRecord.setEmployeeCode(proEmployeeVo.getEmployeeNo());
                noticeRecord.setEmployeeId(proEmployeeVo.getId());
                noticeRecord.setIsRead("N");
                noticeRecord.setNoticeCode(noticeCode);
                noticeRecord.setNoticeText(noticeText);
                noticeRecord.setNoticeName(noticeTitle);
                records.add(noticeRecord);
                //给教练发送通知
                weinxinUtilService.sendWX(Constant.SYSTEMID, proEmployeeVo.getEmployeeNo(), noticeText);
            }
            noticeRecordService.batchInsertNoticeRecord(records);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
