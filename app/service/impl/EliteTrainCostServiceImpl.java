package service.impl;

import com.google.inject.Inject;
import mapper.ProEliteTrainCostMapper;
import models.ProEliteTrainCost;
import org.mybatis.guice.transactional.Transactional;
import service.EliteTrainCostService;
import utils.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Qi Jian Li
 * @Date: 17/7/11
 */
public class EliteTrainCostServiceImpl implements EliteTrainCostService {
    @Inject
    private ProEliteTrainCostMapper proEliteTrainCostMapper;


    @Override
    @Transactional
    public ProEliteTrainCost addProEliteTrainCost(Map<String, Object> mapParams) throws Exception {
        ProEliteTrainCost cost = new ProEliteTrainCost();
        cost.setId(StringUtils.getTableId("cost"));
        cost.setEvaluatingManagerCost(new BigDecimal(StringUtils.objToString(mapParams.get("evaluatingManagerCost"))));
        cost.setEvaluatingEmployeeCost(new BigDecimal(StringUtils.objToString(mapParams.get("evaluatingEmployeeCost"))));
        cost.setLobbyManagerCost(new BigDecimal(StringUtils.objToString(mapParams.get("lobbyManagerCost"))));
        cost.setLobbyEmployeeCost(new BigDecimal(StringUtils.objToString(mapParams.get("lobbyEmployeeCost"))));
        cost.setReserveManagerCost(new BigDecimal(StringUtils.objToString(mapParams.get("reserveManagerCost"))));
        cost.setReserveEmployeeCost(new BigDecimal(StringUtils.objToString(mapParams.get("reserveEmployeeCost"))));
        cost.setReserveManagerAmerce(new BigDecimal(StringUtils.objToString(mapParams.get("reserveManagerAmerce"))));
        cost.setReserveEmployeeAmerce(new BigDecimal(StringUtils.objToString(mapParams.get("reserveEmployeeAmerce"))));
        cost.setCreatorId(StringUtils.objToString(mapParams.get("creatorId")));
        cost.setCreateTime(System.currentTimeMillis());
        cost.setModifyTime(System.currentTimeMillis());
        cost.setDr("N");
        proEliteTrainCostMapper.insert(cost);
        return cost;
    }

    @Override
    @Transactional
    public Map<String, Object> updateProEliteTrainCost(Map<String, Object> mapParams) throws Exception {
        Map<String, Object> map = new HashMap<>();

        ProEliteTrainCost cost = new ProEliteTrainCost();

        cost.setId(StringUtils.objToString(mapParams.get("costId")));
        cost.setEvaluatingManagerCost(new BigDecimal(StringUtils.objToString(mapParams.get("evaluatingManagerCost"))));
        cost.setEvaluatingEmployeeCost(new BigDecimal(StringUtils.objToString(mapParams.get("evaluatingEmployeeCost"))));
        cost.setLobbyManagerCost(new BigDecimal(StringUtils.objToString(mapParams.get("lobbyManagerCost"))));
        cost.setLobbyEmployeeCost(new BigDecimal(StringUtils.objToString(mapParams.get("lobbyEmployeeCost"))));
        cost.setReserveManagerCost(new BigDecimal(StringUtils.objToString(mapParams.get("reserveManagerCost"))));
        cost.setReserveEmployeeCost(new BigDecimal(StringUtils.objToString(mapParams.get("reserveEmployeeCost"))));
        cost.setReserveManagerAmerce(new BigDecimal(StringUtils.objToString(mapParams.get("reserveManagerAmerce"))));
        cost.setReserveEmployeeAmerce(new BigDecimal(StringUtils.objToString(mapParams.get("reserveEmployeeAmerce"))));
        cost.setCreatorId(StringUtils.objToString(mapParams.get("creatorId")));
        cost.setModifierId(StringUtils.objToString(mapParams.get("modifierId")));
        cost.setCreateTime(System.currentTimeMillis());
        cost.setModifyTime(System.currentTimeMillis());
        cost.setDr("N");
        proEliteTrainCostMapper.updateByPrimaryKeySelective(cost);
        map.put("flag", true);
        return map;
    }

    @Override
    public ProEliteTrainCost getProEliteTrainCosts() {
        ProEliteTrainCost cost = proEliteTrainCostMapper.getProEliteTrainCosts();
        return cost;
    }
}
