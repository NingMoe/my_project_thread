package service.impl;

import com.google.inject.Inject;
import mapper.ProEliteNoticeRecordMapper;
import mapper.ProEliteTestRecordMapper;
import models.ProEliteNoticeRecord;
import models.ProEliteTestRecord;
import org.mybatis.guice.transactional.Transactional;
import service.EliteNoticeRecordService;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/21 0021.
 */
public class EliteNoticeRecordServiceImpl implements EliteNoticeRecordService {

    @Inject
    private ProEliteNoticeRecordMapper noticeRecordMapper;
    @Inject
    private ProEliteTestRecordMapper proEliteTestRecordMapper;
    @Inject
    private ProEliteTestRecord proEliteTestRecord;
    @Override
    public List<ProEliteNoticeRecord> getLaksaSelfNoticeRecords(Map<String, Object> mapParams) throws Exception {
        List<ProEliteNoticeRecord> noticeRecordList = null;
        if (mapParams != null) {
            noticeRecordList = noticeRecordMapper.getLaksaSelfNoticeRecords(mapParams);
        }
        return noticeRecordList;
    }

    @Override
    public int getUnreadNoticeCount(Map<String, Object> mapParams) throws Exception {
        int unread = 0;
        if (mapParams != null) {
            unread = noticeRecordMapper.getUnreadNoticeCount(mapParams);
        }
        return unread;
    }

    @Override
    @Transactional
    public void insertNoticeRecord(ProEliteNoticeRecord noticeRecord) {
        noticeRecordMapper.insert(noticeRecord);
    }

    /**
     *
     * @param mapParams
     * @return
     */
    @Override
    public List<ProEliteNoticeRecord> getNoticeByEmployeeCode(Map<String, Object> mapParams) {
        List<ProEliteNoticeRecord> noticeRecordList = null;
        if (mapParams != null) {
            noticeRecordList = noticeRecordMapper.getNoticeByEmployeeCode(mapParams);
        }
        return noticeRecordList;
    }

    @Override
    public List<Map<String, Object>> getNoticesByEmployeeCode(Map<String, Object> mapParams) {
        List<ProEliteNoticeRecord> noticeRecordList = null;
        List<Map<String,Object>> mapList=new ArrayList<>();
        if (mapParams != null) {
            noticeRecordList = noticeRecordMapper.getNoticeByEmployeeCode(mapParams);
            for(int i=0;i<noticeRecordList.size();i++){
                Map map=new HashMap();
                map.put("isRead",noticeRecordList.get(i).getIsRead());
                map.put("noticeName",noticeRecordList.get(i).getNoticeName());
                map.put("noticeCode",noticeRecordList.get(i).getNoticeCode());
                map.put("noticeText",noticeRecordList.get(i).getNoticeText());
                map.put("ts",noticeRecordList.get(i).getTs());
                map.put("createTime",noticeRecordList.get(i).getCreateTime());
                if(noticeRecordList.get(i).getNoticeCode()!=null){
                    if(noticeRecordList.get(i).getNoticeCode().equals("1")){
                        List<ProEliteTestRecord> proEliteTestRecordList= proEliteTestRecordMapper.getTestByEmployee(mapParams);

                        if(proEliteTestRecordList!=null&&proEliteTestRecordList.size()!=0){

                            if(proEliteTestRecordList.get(0).getTestReportUrl()==null||proEliteTestRecordList.get(0).getTestReportUrl().equals("")){
                                map.put("url",proEliteTestRecordList.get(0).getTestUrl());
                                map.put("result","no");
                            }
                            else{
                                map.put("url","");
                                map.put("result","yes");
                            }
                        }else{
                            map.put("url","");
                            map.put("result","");
                        }
                    }
//                    List<ProEliteTestRecord> proEliteTestRecordList= proEliteTestRecordMapper.getTestByEmployee(mapParams);

                    mapList.add(map);
                }
                }

        }
        return mapList;
    }

    @Override
    @Transactional
    public int updateNoticeRerodIsRead(Map<String, Object> mapParams) {
        if(mapParams!=null){
            ProEliteNoticeRecord noticeRecord =  new ProEliteNoticeRecord();
            noticeRecord.setId(StringUtils.objToString(mapParams.get("id")));
            noticeRecord.setIsRead("Y");
            noticeRecordMapper.updateByPrimaryKeySelective(noticeRecord);
            return 1;
        }
        return 0;
    }
    @Override
    public void batchInsertNoticeRecord(List<ProEliteNoticeRecord> recordList) {
        noticeRecordMapper.batchInsertNoticeRecord(recordList);
    }
}
