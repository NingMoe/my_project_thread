package ws.call;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.inject.Inject;
import com.hht.utils.SignVerifyUtil;
import com.hht.view.ResultView;
import common.Constant;
import mapper.ElitePresentationBodyMapper;
import mapper.ElitePresentationHeadMapper;
import models.ElitePresentationBody;
import models.ElitePresentationHead;
import models.ProEliteExaminationOnline;
import models.ProEliteTestRecord;
import play.Configuration;
import play.Logger;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteTestRecordService;
import service.ExaminationOnlineService;
import service.ShopEliteService;
import utils.StringUtils;
import utils.ValidBatchUtils;
import ws.NoticeService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/19 10:15
 */
public class EvaluationController extends Controller {

    @Inject
    private FormFactory formFactory;

    @Inject
    private EliteTestRecordService testRecordService;

    @Inject
    private ShopEliteService eliteService;

    @Inject
    private ExaminationOnlineService onlineService;

    @Inject
    private NoticeService noticeService;

    @Inject
    private ElitePresentationHeadMapper elitePersonHeadMapper;

    @Inject
    private ElitePresentationBodyMapper elitePersonBodyMapper;

    public Result getTestLinks() {
        String method = "TestLinks";
//        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        String params = request().body().asJson().toString();
        Map<String, Object> mapParams = (Map<String, Object>) JSON.parse(params);
        //判断签名是否正确
//        if (!SignVerifyUtil.checkSecuret(mapParams, Configuration.root().getString("forTest.url"), method, Constant.TESTSECURET)) {
//            return ok(ResultView.fail(Constant.RESULT_WAIT_FOR_DO, "签名校验失败"));
//        }
        //判断参数是否为空
        try {
            ValidBatchUtils.isNotEmpty(mapParams, "InvitationLinks");
            String invitationLinks = mapParams.get("InvitationLinks").toString();
            List<Map> maps = JSON.parseArray(invitationLinks, Map.class);
            if (maps.size() == 0) {
                return ok(ResultView.fail(Constant.RESULT_FAIL, "未获得数据"));
            }
            List<ProEliteTestRecord> list = new ArrayList<>();
            for (Map map : maps) {
                ProEliteTestRecord record = new ProEliteTestRecord();
                record.setId(StringUtils.getTableId("pro_elite_test_record"));
                record.setDr("N");
                record.setCreateTime(System.currentTimeMillis());
                record.setCreatorId("system");
                record.setEnable("Y");
                record.setEmployeeCode(StringUtils.objToString(map.get("ExternalId")));
                record.setTestUrl(StringUtils.objToString(map.get("InvitationLink")));
                record.setTestOnceId(StringUtils.objToString(map.get("RespondentUid")));
                list.add(record);
            }
            testRecordService.batchInsertRecord(list);
            eliteService.batchUpdateForTest(maps);
            for (ProEliteTestRecord record : list) {
                noticeService.sendNotice(record.getEmployeeCode(), "100", false, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.RESULT_FAIL, "系统异常"));
        }
        return ok(ResultView.success(Constant.RESULT_SUCCESS));
    }


    public Result getTestResult() {
        String method = "TestResult";
        String params = request().body().asJson().toString();
        Map<String, Object> mapParams = (Map<String, Object>) JSON.parse(params);
//        判断签名是否正确
//        if (!SignVerifyUtil.checkSecuret(mapParams, Configuration.root().getString("forTest.url"), method, Constant.TESTSECURET)) {
//            return ok(ResultView.fail(Constant.RESULT_WAIT_FOR_DO, "签名校验失败"));
//        }
        //判断参数是否为空
        try {
            ValidBatchUtils.isNotEmpty(mapParams, "RespondentUid", "EndTime");
            String RespondentUid = mapParams.get("RespondentUid").toString();
            String endTime = mapParams.get("EndTime").toString();
            ProEliteTestRecord record = new ProEliteTestRecord();
            record.setTestEndTime(endTime);
            record.setTestOnceId(RespondentUid);
            testRecordService.updateByTestId(record);

        } catch (Exception e) {
            return ok(ResultView.fail(Constant.RESULT_FAIL, "系统异常"));
        }
        return ok(ResultView.success(Constant.RESULT_SUCCESS));
    }


    public Result getReportLinks() {
        String method = "ReportLinks";
        String params = request().body().asJson().toString();
        Map<String, Object> mapParams = (Map<String, Object>) JSON.parse(params);
        Map<String, String> reportLevelMap = new HashMap<>();
        reportLevelMap.put("好", "300");
        reportLevelMap.put("中", "200");
        reportLevelMap.put("差", "100");
        //判断签名是否正确
//        if (!SignVerifyUtil.checkSecuret(mapParams, Configuration.root().getString("forTest.url"), method, Constant.TESTSECURET)) {
//            return ok(ResultView.fail(Constant.RESULT_WAIT_FOR_DO, "签名校验失败"));
//        }
        //判断参数是否为空
        try {
            ValidBatchUtils.isNotEmpty(mapParams, "IndividualReportLinks");
            String invitationLinks = mapParams.get("IndividualReportLinks").toString();
            List<Map> maps = JSON.parseArray(invitationLinks, Map.class);
            if (maps.size() == 0) {
                return ok(ResultView.fail(Constant.RESULT_FAIL, "未获得数据"));
            }
            Logger.error("接收测评",maps.toString());
            List<String> ids = new ArrayList<>();
            List<Map> reportLinks = JSON.parseArray(maps.get(0).get("ReportLinks").toString(), Map.class);
            List<Map<String,Object>> result = new ArrayList<>();
            for (Map link : reportLinks) {
                ids.add(StringUtils.objToString(link.get("RespondentUid")));
                Map<String,Object> map = new HashMap<>();
                map.put("ReportLevel",link.get("ReportLevel"));
                map.put("RespondentUid",link.get("RespondentUid"));
                map.put("ReportLink",link.get("ReportLink"));
                map.put("ReportJsonInfo",link.get("ReportJsonInfo"));//新增人员评价
                result.add(map);
            }

            List<ElitePresentationHead> presentationHeadList=new ArrayList<>();//测评记录
            List<ElitePresentationBody> presentationBodyList=new ArrayList<>();//测评记录
            //查询需要更新的测评记录表
            if (ids.size() > 0) {
                List<ProEliteTestRecord> list = testRecordService.queryListByTestOnceId(ids);
                if (list.isEmpty()) {
                    return ok(ResultView.success(Constant.SUCCESS));
                }
                for (ProEliteTestRecord record : list) {
                    for (Map<String,Object> link : result) {
                        if (record.getTestOnceId().equals(StringUtils.objToString(link.get("RespondentUid")))) {
                            record.setModifyTime(System.currentTimeMillis());
                            record.setModifierId("systemReport");
                            record.setTestResult(StringUtils.objToString(link.get("ReportLevel")));
                            record.setTestReportUrl(StringUtils.objToString(link.get("ReportLink")));
                            record.setIsTask("Y");
                            //主表信息准备
//                            List<Map> listMap= (List<Map>) link.get("ReportJsonInfo");
//                            JSONArray  a= (JSONArray) link.get("ReportJsonInfo");
//                            HashMap hashMap = new HashMap();
//                            List<Map> dishs = JSON.parseArray(s.toJSONString(), hashMap);
//                            System.out.println(((List<Map>) link.get("ReportJsonInfo")).toArray());
//                            List<Map> reportLinks1 = ((List<Map>) link.get("ReportJsonInfo"));
                            Map reportLinks1 = (Map) link.get("ReportJsonInfo");
                          //  String headId=StringUtils.getTableId("elitePresentationHead");
                            String headId=UUID.randomUUID().toString().replaceAll("-", "");

                            ElitePresentationHead elitePresentationHead=new ElitePresentationHead();
                            elitePresentationHead.setId(headId);
                            elitePresentationHead.setCreateTime(System.currentTimeMillis());
                            elitePresentationHead.setCreatorId("system");
                            elitePresentationHead.setEmployeeCode(record.getEmployeeCode());
                            elitePresentationHead.setEmployeeName(reportLinks1.get("Name").toString());
                            elitePresentationHead.setAge(reportLinks1.get("Age").toString());
                            elitePresentationHead.setGender(reportLinks1.get("Gender").toString());
                            elitePresentationHead.setPositionName(reportLinks1.get("Position").toString());

                            SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MMdd HH:mm:ss" );
                            Date date = sdf.parse(reportLinks1.get("TestTime").toString().replace("T"," "));
                            elitePresentationHead.setPresentationTime(date.getTime());
                            elitePresentationHead.setShopName(reportLinks1.get("StoreName").toString());
//                            elitePresentationHead.setReminder();
                            elitePresentationHead.setResult(reportLinks1.get("OverallLevel").toString().equals("合格")?"Y":"N");
                            elitePresentationHead.setTs(System.currentTimeMillis());
                            elitePresentationHead.setTestId(record.getId());
                            elitePresentationHead.setTestOnceId(record.getTestOnceId());
                            elitePresentationHead.setDr("N");
                            presentationHeadList.add(elitePresentationHead);
                            //子表信息准备
                            List<Map> reportLinks2 = (List<Map>)reportLinks1.get("Competencies");
                            List<Map> reportLinks3 = (List<Map>)reportLinks1.get("AnswerStyle");
                            for (Map map : reportLinks2) {
                                ElitePresentationBody elitePresen=new ElitePresentationBody();
                                String bodyId=UUID.randomUUID().toString().replaceAll("-", "");
                                elitePresen.setId(bodyId);
                              //  elitePresen.setId(StringUtils.getTableId("elitePresentationBody"));
                                elitePresen.setParentId(headId);
                                elitePresen.setDimensionName(map.get("Name").toString());
                                elitePresen.setGrade(map.get("Level").toString());
                                elitePresen.setDr("N");
                                elitePresen.setTs(System.currentTimeMillis());
                                presentationBodyList.add(elitePresen);
                            }
                            for (Map map : reportLinks3) {
                                ElitePresentationBody elitePresen=new ElitePresentationBody();
                                String bodyIds=UUID.randomUUID().toString().replaceAll("-", "");

                                elitePresen.setId(bodyIds);
                               // elitePresen.setId(StringUtils.getTableId("elitePresentationBody"));
                                elitePresen.setParentId(headId);
                                elitePresen.setDimensionName(map.get("Name").toString());
                                elitePresen.setDr("N");
                                elitePresen.setDescribes(map.get("Description").toString());
                                elitePresen.setTs(System.currentTimeMillis());
                                presentationBodyList.add(elitePresen);
                            }
                        }
                    }
                }
                testRecordService.batchUpdateById(list);
                if(presentationHeadList.size()>0){
                    elitePersonHeadMapper.setHeadList(presentationHeadList);
                }
                if(presentationBodyList.size()>0){
                    elitePersonBodyMapper.setBodyList(presentationBodyList);
                }


            }
        } catch (Exception e) {
            Logger.error("测评报告接收失败..." + e.getMessage());
            return ok(ResultView.fail(Constant.RESULT_FAIL, "系统异常"));
        }
        return ok(ResultView.success(Constant.RESULT_SUCCESS));
    }

    public Result getEvaluation() {
        String method = "EvaluationResult";
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        //判断签名是否正确
//        if (!SignVerifyUtil.checkSecuret(mapParams, Configuration.root().getString("forTest.url"), method, Constant.EVALUATIONSECURET)) {
//            return ok(ResultView.fail(Constant.RESULT_WAIT_FOR_DO, "签名校验失败"));
//        }
        //判断参数是否为空
        try {
            ValidBatchUtils.isNotEmpty(mapParams, "employeeCode");
            ProEliteExaminationOnline examinationOnline = new ProEliteExaminationOnline();
            examinationOnline.setEmployeeCode(StringUtils.objToString(mapParams.get("employeeCode")));
            examinationOnline.setRelultScore(Integer.valueOf(StringUtils.objToString(mapParams.get("score"))));
            examinationOnline.setPassScore(Integer.valueOf(StringUtils.objToString(mapParams.get("passScore"))));
            examinationOnline.setCreateTime(System.currentTimeMillis());
            examinationOnline.setIsPass(StringUtils.objToString(mapParams.get("isPass")));
            examinationOnline.setExaminationCode(StringUtils.objToString(mapParams.get("examinationCode")));
            examinationOnline.setExaminationDateTime(StringUtils.objToString(mapParams.get("examinationDateTime")));
            examinationOnline.setCreatorId("System");
            examinationOnline.setDr("N");
            onlineService.insert(examinationOnline);
            if ("Y".equals(examinationOnline.getIsPass())) {
                noticeService.sendNoticeForExam(examinationOnline, "5", false);
            }else {
                noticeService.sendNoticeForExam(examinationOnline, "4", false);
            }
        } catch (Exception e) {
            return ok(ResultView.fail(Constant.RESULT_FAIL, "系统异常"));
        }
        return ok(ResultView.success(Constant.RESULT_SUCCESS));
    }


    /**
     * 构造服务器返回的header
     */
    private Map getHeaderMap(String method, String alertMessage, String resultCode) {
        Map<String, String> map = new HashMap<>();
        map.put("ALERTMESSAGE", alertMessage);
        map.put("BUSINESSID", method);
        map.put("CLIENTVERSION", "");
        map.put("DEVICETYPE", "");
        map.put("LANGUAGE", "");
        map.put("LASTBUSINESSID", "");
        map.put("LASTDOSERVERTIME", "");
        map.put("LASTDOTOTALTIME", "");
        map.put("LASTDOTZXTIME", "");
        map.put("MAC", "");
        map.put("RESULTCODE", resultCode);
        map.put("TABLEID", "");
        map.put("UUID", "");
        map.put("WAITERID", "");
        map.put("billNo", "");
        map.put("config", "");
        return map;
    }
}
