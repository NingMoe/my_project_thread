package service.impl;

import com.google.inject.Inject;
import mapper.ProEliteNoticeMapper;
import mapper.ProEliteNoticeRecordMapper;
import models.ProEliteNotice;
import models.ProEliteNoticeRecord;
import service.MessagePushService;

import java.util.List;
/**
 * Created by MR.GANG on 2017/7/25.
 */
public class MessagePushServiceImpl implements MessagePushService {
   //消息推送
    //pro_elite_notice_record
    @Inject
    private ProEliteNoticeRecord proEliteNoticeRecord;
    @Inject
    private ProEliteNotice proEliteNotice;
    @Inject
    private ProEliteNoticeRecordMapper proEliteNoticeRecordMapper;
    @Inject
    private ProEliteNoticeMapper proEliteNoticeMapper;
    @Override
    public String pushMessage(List<String> employeeIdList, String messageName, String flag) {
        //得到需要推送的人员 节点
        for(int i=0;i<employeeIdList.size();i++){

        }
        //推送消息
        String userHrId = "";
        String msgContent = "{'title':'选徒弟并培养汇报学习情况4','description':'点击本消息可查看明细...','tourl':'','appid':'hdlapp025','state':'hhtmms','urlparam':'parm1=value1*parm2=value2*parm3=value3','picurl':'http://www.haidilao.com/statics/images/logo.png'}";
//        WeixinUtil wx = new WeixinUtil();
//        try {
//            String rs = wx.sendAttrWX("noBpb8UK9OXRuXfid", userHrId,
//                    msgContent);// 工资消息测试
//            System.out.println(rs);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        return null;
    }
}
