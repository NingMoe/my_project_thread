package service.impl;

import com.google.inject.Inject;
import mapper.ProEliteExaminationOnlineMapper;
import models.ProEliteExaminationOnline;
import service.EliteAppExaminationService;

import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/8/2.
 */
public class EliteAppExaminationServiceImpl implements EliteAppExaminationService {
    @Inject
    private ProEliteExaminationOnlineMapper proEliteExaminationOnlineMapper;
    @Inject
    private  ProEliteExaminationOnline proEliteExaminationOnline;

    @Override
    public List<Map<String,Object>> getExaminationOnline(Map<String, Object> params) {
       List<Map<String,Object>> mapList= proEliteExaminationOnlineMapper.getProEliteExaminationByEmployee(params);
        return mapList;
    }

    @Override
    public ProEliteExaminationOnline getProBeanByEmployee(Map<String, Object> params) {
        proEliteExaminationOnline=proEliteExaminationOnlineMapper.getProBeanByEmployee(params);
        return proEliteExaminationOnline;
    }
}
