package service;

import com.google.inject.ImplementedBy;
import models.ProEliteTrainCost;
import service.impl.EliteTrainCostServiceImpl;

import java.util.Map;

/**
 *
 * @Author: Qi Jian Li
 * @Date: 17/7/11
 */
@ImplementedBy(EliteTrainCostServiceImpl.class)
public interface EliteTrainCostService {


    /**
     * 新增收费设置
     * @param mapParams
     * @return
     */
    ProEliteTrainCost addProEliteTrainCost(Map<String, Object> mapParams)throws Exception;


    /**
     * 更新收费设置
     * @param mapParams
     * @return
     */
    Map<String,Object> updateProEliteTrainCost(Map<String, Object> mapParams)throws Exception;

    /**
     * 查询收费设置
     * @return
     */
    ProEliteTrainCost getProEliteTrainCosts();

}
