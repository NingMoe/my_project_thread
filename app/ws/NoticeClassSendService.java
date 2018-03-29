package ws;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import models.ProEliteClass;
import ws.call.NoticeClassSendServiceImpl;

/**
 * Created by MR.GANG on 2017/7/30.
 */
@ImplementedBy(NoticeClassSendServiceImpl.class)
public interface NoticeClassSendService {
    /**
     * @param employeeCode    employeeCode
     * @param noticeCode      noticeCode  100(大堂班组班确认通知)  200 (店经理班组班确认通知)
     * @param eliteClass     班级信息
     * @return
     */
    String sendNotice(String employeeCode, String noticeCode, ProEliteClass eliteClass);

}
