package service.impl;

import com.google.inject.Inject;
import mapper.ProEliteNoticeClassMapper;
import models.ProEliteNoticeClass;
import org.mybatis.guice.transactional.Transactional;
import service.NoticeClassService;

import java.util.List;
import java.util.Map;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/7/28 15:51
 */
public class NoticeClassServiceImpl implements NoticeClassService {

    @Inject
    private ProEliteNoticeClassMapper noticeClassMapper;

    @Override
    public List<ProEliteNoticeClass> getNoticeList() {
        return noticeClassMapper.getNoticeList();
    }

    @Override
    @Transactional
    public void addNotice(Map<String, Object> mapParams) {
        String noticeTitle = mapParams.get("noticeTitle").toString();
        String noticeText = mapParams.get("noticeText").toString();
        String creatorId = mapParams.get("creatorId").toString();
        String url = mapParams.get("url").toString();
        //更新其他dr为Y
        noticeClassMapper.updateByNoticeTitle(noticeTitle);
        ProEliteNoticeClass noticeClass = new ProEliteNoticeClass();
        noticeClass.setNoticeText(noticeText);
        noticeClass.setNoticeTitle(noticeTitle);
        noticeClass.setIsDefault("N");
        noticeClass.setUrl(url);
        noticeClass.setCreatorId(creatorId);
        noticeClass.setCteateTime(System.currentTimeMillis());
        noticeClass.setDr("N");
        noticeClassMapper.insert(noticeClass);
    }

    @Override
    @Transactional
    public ProEliteNoticeClass noticeRest(Map<String, Object> mapParams) {
        return noticeClassMapper.queryNoticeByNoticeTitle(mapParams.get("noticeTitle").toString());
    }
}
