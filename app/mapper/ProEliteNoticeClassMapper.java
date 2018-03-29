package mapper;

import models.ProEliteNoticeClass;

import java.util.List;

public interface ProEliteNoticeClassMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProEliteNoticeClass record);

    int insertSelective(ProEliteNoticeClass record);

    ProEliteNoticeClass selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProEliteNoticeClass record);

    int updateByPrimaryKey(ProEliteNoticeClass record);

    List<ProEliteNoticeClass> getNoticeList();

    void updateByNoticeTitle(String noticeTitle);

    ProEliteNoticeClass queryNoticeByNoticeTitle(String noticeTitle);
    ProEliteNoticeClass queryNoticeByNoticeTitleType(String noticeTitle);

}