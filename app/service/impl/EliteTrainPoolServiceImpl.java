package service.impl;

import com.google.inject.Inject;
import mapper.ProEliteTrainPoolMapper;
import models.ProEliteTrainPool;
import org.mybatis.guice.transactional.Transactional;
import service.EliteTrainPoolService;
import utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @Author: Qi Jian Li
 * @Date: 17/7/11
 */
public class EliteTrainPoolServiceImpl implements EliteTrainPoolService {
    @Inject
    private ProEliteTrainPoolMapper proEliteTrainPoolMapper;

    @Override
    @Transactional
    public ProEliteTrainPool addProEliteTrainPool(Map<String, Object> mapParams) throws Exception {
        ProEliteTrainPool pool = new ProEliteTrainPool();
        pool.setId(StringUtils.getTableId("pro_elite_train_pool"));
        pool.setElitePer(Integer.valueOf(StringUtils.objToString(mapParams.get("elitePer"))));
        pool.setUpPer(Integer.valueOf(StringUtils.objToString(mapParams.get("upPer"))));
        pool.setWarnPer(Integer.valueOf(StringUtils.objToString(mapParams.get("warnPer"))));
        pool.setWarnTime(Integer.valueOf(StringUtils.objToString(mapParams.get("warnTime"))));
        pool.setCreatorId(StringUtils.objToString(mapParams.get("creatorId")));
        pool.setLobbySignTime(Integer.valueOf(StringUtils.objToString(mapParams.get("lobbySignTime"))));
        pool.setReserveSignTime(Integer.valueOf(StringUtils.objToString(mapParams.get("reserveSignTime"))));
        pool.setLobbyMsg(StringUtils.objToString(mapParams.get("lobbyMsg")));
        pool.setReserveMsg(StringUtils.objToString(mapParams.get("reserveMsg")));
        pool.setCreateTime(System.currentTimeMillis());
        pool.setModifyTime(System.currentTimeMillis());
   //     pool.setModifierId(StringUtils.objToString(mapParams.get("modifierId")));
        pool.setDr("N");
        proEliteTrainPoolMapper.insert(pool);
        return pool;
    }

    @Override
    @Transactional
    public Map<String, Object> updateProEliteTrainPool(Map<String, Object> mapParams) throws Exception {
        Map<String, Object> map = new HashMap<>();
        ProEliteTrainPool pool = new ProEliteTrainPool();
        pool.setId(StringUtils.objToString(mapParams.get("poolId")));
        pool.setElitePer(Integer.valueOf(StringUtils.objToString(mapParams.get("elitePer"))));
        pool.setUpPer(Integer.valueOf(StringUtils.objToString(mapParams.get("upPer"))));
        pool.setWarnPer(Integer.valueOf(StringUtils.objToString(mapParams.get("warnPer"))));
        pool.setLobbySignTime(Integer.valueOf(StringUtils.objToString(mapParams.get("lobbySignTime"))));
        pool.setReserveSignTime(Integer.valueOf(StringUtils.objToString(mapParams.get("reserveSignTime"))));
        pool.setLobbyMsg(StringUtils.objToString(mapParams.get("lobbyMsg")));
        pool.setReserveMsg(StringUtils.objToString(mapParams.get("reserveMsg")));
        pool.setCreateTime(System.currentTimeMillis());
        pool.setModifyTime(System.currentTimeMillis());
        pool.setModifierId(StringUtils.objToString(mapParams.get("modifierId")));
        pool.setWarnTime(Integer.valueOf(StringUtils.objToString(mapParams.get("warnTime"))));
      //  pool.setDr("N");
        proEliteTrainPoolMapper.updateByPrimaryKeySelective(pool);
        map.put("flag", true);
        return map;
    }

    @Override
    @Transactional
    public ProEliteTrainPool getProEliteTrainPools() throws Exception {
        ProEliteTrainPool pool=  proEliteTrainPoolMapper.getProEliteTrainPools();
        return pool;

    }
}
