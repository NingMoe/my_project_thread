package service;

import com.google.inject.ImplementedBy;
import service.impl.MessagePushServiceImpl;

import java.util.List;

/**
 * Created by MR.GANG on 2017/7/21.
 */
@ImplementedBy(MessagePushServiceImpl.class)
public interface MessagePushService {
    public String pushMessage(List<String> employeeCodeList, String messageName,String pushMessage);
}
