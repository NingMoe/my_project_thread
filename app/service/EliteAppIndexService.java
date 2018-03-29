package service;

import com.google.inject.ImplementedBy;
import models.*;
import service.impl.EliteAppIndexServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/7/20.
 */
@ImplementedBy(EliteAppIndexServiceImpl.class)
public interface EliteAppIndexService {
    List<ProEliteNoticeRecord> getEmployeeMessege(Map<String,Object> mapParams);
    List<ProEliteNoticeClassRecord> getEliteNoticeClass(Map<String,Object> mapParams);
    List<ProEliteTestRecord> getTestInfo(Map<String,Object> mapParams);
    ProEliteClass  getClassByEmployeeCode(Map<String,Object> mapParams);
}
