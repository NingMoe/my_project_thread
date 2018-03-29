package service;

import com.google.inject.ImplementedBy;
import service.impl.WeinxinUtilServiceImpl;
@ImplementedBy(WeinxinUtilServiceImpl.class)
public interface WeinxinUtilService {
    public String sendWX(String systemId, String userHrId, String msgContent)
            throws Exception ;

    /**
     *
     * @param systemId
     *            系统标识 催办系统为noBpb8UK9OXRuXfid
     * @param userHrId
     *            数据库中的login_name hz_sgb
     * @param msgContent
     *            发送图文消息
     * @return 返回字符串true 就是发送成功，其他为失败，且是失败原因
     * @throws Exception
     */

    public String sendAttrWX(String systemId, String userHrId,
                             String msgContent) throws Exception ;

    /**
     * 解析微信反馈的xml字符串
     *
     * @param xmlDoc
     * @return
     */
    public String xmlElements(String xmlDoc) ;
}
