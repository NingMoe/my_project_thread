package ws.call;

import com.google.inject.Inject;
import common.Constant;
import mapper.ProEliteNoticeClassMapper;
import mapper.ProEliteNoticeClassRecordMapper;
import models.*;
import models.vo.ProEmployeeVo;
import org.apache.poi.openxml4j.opc.Configuration;
import org.mybatis.guice.transactional.Transactional;
import service.EliteTrainCostService;
import service.EmployeeService;
import service.WeinxinUtilService;
import utils.StringUtils;
import ws.NoticeClassSendService;

import java.text.SimpleDateFormat;
import java.util.*;

public class NoticeClassSendServiceImpl implements NoticeClassSendService {
    @Inject
    private EmployeeService employeeService;
    @Inject
    private EliteTrainCostService trainCostService;
    @Inject
    private ProEliteNoticeClassMapper proEliteNoticeClassMapper;
    @Inject
    private ProEliteNoticeClassRecordMapper proEliteNoticeClassRecordMapper;
    @Inject
    private WeinxinUtilService weinxinUtilService;
    @Override
    @Transactional
    public String sendNotice(String employeeCode, String noticeCode, ProEliteClass eliteClass) {
        try {
            List<ProEmployeeVo> employees = employeeService.queryByCode(employeeCode);
            ProEmployeeVo employee = employees.get(0);
            ProEliteTrainCost cost = trainCostService.getProEliteTrainCosts();
            Map<String, Object> params = new HashMap<>();
            params.put("noticeCode", noticeCode);
            String noticeText = "";
            String noticeTitle="";
            ProEliteNoticeClass notice = proEliteNoticeClassMapper.queryNoticeByNoticeTitleType(noticeCode);
            noticeText = getNoticeText(notice.getNoticeText(), employee, eliteClass, cost,notice);
            noticeTitle = notice.getNoticeTitle();
            ProEliteNoticeClassRecord noticeRecord = new ProEliteNoticeClassRecord();
            noticeRecord.setDr("N");
            noticeRecord.setId(StringUtils.getTableId("pro_elite_notice_record"));
            noticeRecord.setCreateTime(System.currentTimeMillis());
            noticeRecord.setCreatorId("system");
            noticeRecord.setNoticeText(noticeText);
            noticeRecord.setNoticeTitle(noticeTitle);
            noticeRecord.setNoticeClassId(employeeCode);
            noticeRecord.setIsUse("N");
            proEliteNoticeClassRecordMapper.insert(noticeRecord);
//            String systemId = play.Configuration.root().getString("systemId");
            //TODO 调用微信通知接口
            weinxinUtilService.sendWX(Constant.SYSTEMID,employeeCode,noticeText);
        } catch (Exception e) {
            e.printStackTrace();
            return "FALSE";
        }
        return "SUCCESS";
    }


    private String getNoticeText(String noticeContent, ProEmployeeVo employee, ProEliteClass eliteClass, ProEliteTrainCost cost,ProEliteNoticeClass notice) {
        if (noticeContent.contains("{#报到时间#}")) {
            String times=StringUtils.longToDate(eliteClass.getBeginTime(), "yyyy年MM月dd日")+StringUtils.longToDate(eliteClass.getReportTime(), "HH时mm分");
            noticeContent = noticeContent.replace("{#报到时间#}", times);
        }
        if (noticeContent.contains("{#报到地点#}")) {
            noticeContent = noticeContent.replace("{#报到地点#}", eliteClass.getReportLocaltion());
        }
        if (noticeContent.contains("{#天数#}")) {
            noticeContent = noticeContent.replace("{#天数#}", eliteClass.getTrainDays().toString());
        }
        if (noticeContent.contains("{#店员#}")) {
            noticeContent = noticeContent.replace("{#店员#}", employee.getEmployeeName());
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
        if (noticeContent.contains("{#培训期间#}")) {
            noticeContent = noticeContent.replace("{#培训期间#}",getDate(eliteClass.getBeginTime(),eliteClass.getTrainDays()));
        }
        if (noticeContent.contains("{#二维码#}")) {
            String str="&lt;img id='scan' src='"+notice.getUrl()+"' alt=''/ &gt;";
            noticeContent = noticeContent.replace("{#二维码#}",str);
        }
        if (noticeContent.contains("{#组班二维码#}")) {
            String str="&lt;img id='scan' src='"+eliteClass.getQrCodeUrl()+"' alt=''/ &gt;";
            noticeContent = noticeContent.replace("{#组班二维码#}",str);
        }
        if (noticeContent.contains("{#联系人#}")) {
            String str="&lt;img id='scan' src='"+eliteClass.getQrCodeUrl()+"' alt=''/ &gt;";
            noticeContent = noticeContent.replace("{#联系人#}",eliteClass.getRelationName());
        }
        if (noticeContent.contains("{#联系电话#}")) {
            noticeContent = noticeContent.replace("{#联系电话#}",eliteClass.getMobileNo());
        }
        //TODO查询天气气温接口

        return noticeContent;
    }
    private    String getDate(Long str,int days){
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//        String str = "1507161600000";
        Date currdate = new Date(str);
        String s = format1.format(currdate);
//        System.out.println("现在的日期是：" + s);
        Calendar ca = Calendar.getInstance();
        ca.setTime(currdate);
        ca.add(Calendar.DATE, days);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        String enddate = format1.format(currdate);

        String begindata=StringUtils.longToDate(str, "yyyy-MM-dd");
        return begindata+" 到 "+enddate;
//        System.out.println("增加天数以后的日期：" + enddate);
    }
}
