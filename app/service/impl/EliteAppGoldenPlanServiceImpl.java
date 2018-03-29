package service.impl;

import com.google.inject.Inject;
import mapper.ProShopEliteMapper;
import models.ProShopElite;
import service.EliteAppGoldenPlanService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/7/23.
 */
public class EliteAppGoldenPlanServiceImpl implements EliteAppGoldenPlanService {
    @Inject
    private ProShopElite proShopElite;
    @Inject
    private ProShopEliteMapper proShopEliteMapper;
    @Override
    public  List<Map<String,Object>> getTrainStatus(Map<String,Object> mapParams) {
        List<Map<String,Object>> mapList=    proShopEliteMapper.selectByEmployeeCode(mapParams);
      return mapList;
    }
}
