package service;

import com.google.inject.ImplementedBy;
import models.ProEliteExaminationOnline;
import service.impl.EliteAppExaminationServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/8/2.
 */
@ImplementedBy(EliteAppExaminationServiceImpl.class)
public interface EliteAppExaminationService  {
    List<Map<String,Object>> getExaminationOnline(Map<String,Object> params);
    ProEliteExaminationOnline getProBeanByEmployee(Map<String,Object> params);
}
