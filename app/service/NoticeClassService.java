package service;

import com.google.inject.ImplementedBy;
import models.ProEliteNoticeClass;
import service.impl.NoticeClassServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/7/28 15:51
 */
@ImplementedBy(NoticeClassServiceImpl.class)
public interface NoticeClassService {

    List<ProEliteNoticeClass> getNoticeList();

    void addNotice(Map<String, Object> mapParams);

    ProEliteNoticeClass noticeRest(Map<String, Object> mapParams);
}
