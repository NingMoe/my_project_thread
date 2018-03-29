package service.impl;

import com.google.inject.Inject;
import mapper.*;
import models.*;
import service.EliteAppIndexService;

import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/7/20.
 */
public class EliteAppIndexServiceImpl implements EliteAppIndexService {
    @Inject
    private ProEliteNoticeRecordMapper proEliteNoticeRecordMapper;
    @Inject
    private ProEliteTestRecordMapper proEliteTestRecordMapper;
    @Inject
    private ProEliteNoticeClassRecordMapper proEliteNoticeClassRecordMapper;
    @Inject
    private ProEliteNoticeClassMapper proEliteNoticeClassMapper;
    @Inject
    private ProEliteNoticeClass proEliteNoticeClass;
    @Inject
    private  ProEliteClassMapper proEliteClassMapper;
    @Override
    public List<ProEliteNoticeRecord> getEmployeeMessege(Map<String, Object> mapParams) {
        List<ProEliteNoticeRecord> proEliteNoticeRecordList =proEliteNoticeRecordMapper.getLaksaSelfNoticeRecords(mapParams);
        return  proEliteNoticeRecordList;
    }

    @Override
    public List<ProEliteNoticeClassRecord> getEliteNoticeClass(Map<String, Object> mapParams) {
        List<ProEliteNoticeClassRecord> proEliteNoticeClassRecordList= proEliteNoticeClassRecordMapper.selectClassNoticeByEmployeeCode(mapParams);
        //查询图片url
        if(proEliteNoticeClassRecordList!=null&&proEliteNoticeClassRecordList.size()!=0){
            proEliteNoticeClass= proEliteNoticeClassMapper.queryNoticeByNoticeTitleType(proEliteNoticeClassRecordList.get(0).getNoticeTitle());
            for(int i=0;i<proEliteNoticeClassRecordList.size();i++){
                proEliteNoticeClassRecordList.get(i).setUrl(proEliteNoticeClass.getUrl());
            }
        }
        return proEliteNoticeClassRecordList;
    }

    @Override
    public  List<ProEliteTestRecord> getTestInfo(Map<String, Object> mapParams) {
        List<ProEliteTestRecord> proEliteTestRecords= proEliteTestRecordMapper.getTestByEmployee(mapParams);
        return proEliteTestRecords;
    }

    @Override
    public ProEliteClass getClassByEmployeeCode(Map<String, Object> mapParams) {
        ProEliteClass proEliteClass=proEliteClassMapper.getClassByEmployeeCode(mapParams);
        return proEliteClass;
    }
}
