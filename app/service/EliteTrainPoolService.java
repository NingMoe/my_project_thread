package service;

import com.google.inject.ImplementedBy;
import models.ProEliteTrainPool;
import service.impl.EliteTrainPoolServiceImpl;

import java.util.Map;

/**
 *
 * @Author: Qi Jian Li
 * @Date: 17/7/11
 */
@ImplementedBy(EliteTrainPoolServiceImpl.class)
public interface EliteTrainPoolService {


    /**
     * 新增金鹰基础设置
     * @param mapParams
     * @return
     */
    ProEliteTrainPool addProEliteTrainPool(Map<String, Object> mapParams)throws Exception;


    /**
     * 金鹰基础设置
     * @param mapParams
     * @return
     */
    Map<String,Object> updateProEliteTrainPool(Map<String, Object> mapParams)throws Exception;

    /**
     * 查询金鹰基础设置
     * @return
     */
    ProEliteTrainPool getProEliteTrainPools()throws Exception;


}
