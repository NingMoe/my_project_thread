package service;

import com.google.inject.ImplementedBy;
import models.ProShopElite;
import service.impl.EliteAppGoldenPlanServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/7/23.
 */
@ImplementedBy(EliteAppGoldenPlanServiceImpl.class)
public interface EliteAppGoldenPlanService {
    List<Map<String,Object>> getTrainStatus(Map<String, Object> mapParams);
//    List<Map<String,Object>> getTrainStatus(Map<String,Object> mapParams);
}
