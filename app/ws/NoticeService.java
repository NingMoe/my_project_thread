package ws;

import com.google.inject.ImplementedBy;
import models.ProEliteClass;
import models.ProEliteClassPerson;
import models.ProEliteExaminationOnline;
import ws.Impl.NoticeServiceImpl;

import java.util.List;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/25 18:42
 */
@ImplementedBy(NoticeServiceImpl.class)
public interface NoticeService {
    /**
     * @param employeeCode
     * @param noticeCode
     * @param isShopManager
     * @param eliteClass
     * @return
     */
    String sendNotice(String employeeCode, String noticeCode, boolean isShopManager , ProEliteClass eliteClass);
    String sendNoticeNotPass(String employeeCode, String noticeCode, boolean isShopManager , ProEliteClass eliteClass, ProEliteClassPerson proEliteClassPerson);

    /**
     *
     * @param employeeCodeList 传人员Code数组
     * @param noticeCode
     * @param isShopManager
     * @param eliteClass
     * @return
     */
    String sendNoticeByCodeList(List employeeCodeList, String noticeCode, boolean isShopManager , ProEliteClass eliteClass);

    /**
     * 审核未通过给店经理发消息
     * @param employeeCode
     * @param noticeCode
     * @param isShopManager
     * @return
     */
    String sendNoticeAu(String employeeCode, String noticeCode, boolean isShopManager ,String info);


    /**
     * 考试结束发送通知
     * @param examinationOnline
     * @param noticeCode
     * @param b
     */
    void sendNoticeForExam(ProEliteExaminationOnline examinationOnline, String noticeCode, boolean b);
    /**
     * 给教练发送通知
     */
    void sendCoachNotice(String employeeCode, String noticeCode , ProEliteClass eliteClass);
}
