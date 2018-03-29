package service;

import com.google.inject.ImplementedBy;
import models.ProEliteNoticeRecord;
import service.impl.EliteNoticeRecordServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @author qijianli
 *  Created by Administrator on 2017/7/21 0017.
 */
@ImplementedBy(EliteNoticeRecordServiceImpl.class)
public interface EliteNoticeRecordService {

    /**
     * 查询消息通知（店经理）
     * @param mapParams
     * @return
     * @throws Exception
     */

    List<ProEliteNoticeRecord> getLaksaSelfNoticeRecords(Map<String, Object> mapParams)throws Exception;


   int getUnreadNoticeCount(Map<String, Object> mapParams)throws Exception;


    /**
     * 插入消息通知
     * @param noticeRecord
     */
    void insertNoticeRecord(ProEliteNoticeRecord noticeRecord);

    List<ProEliteNoticeRecord> getNoticeByEmployeeCode(Map<String, Object> mapParams);
    List<Map<String,Object>> getNoticesByEmployeeCode(Map<String, Object> mapParams);

    /**
     * 消息已读
     * @param mapParams
     */
    int updateNoticeRerodIsRead(Map<String, Object> mapParams);


    void batchInsertNoticeRecord(List<ProEliteNoticeRecord> list);
}
