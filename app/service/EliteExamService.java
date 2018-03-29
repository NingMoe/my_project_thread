package service;

import com.google.inject.ImplementedBy;
import com.hht.interceptor.Page;
import service.impl.EliteExamServiceImpl;

import java.util.List;
import java.util.Map;

/**
 *
 * @Author: Qi Jian Li
 * @Date: 17/7/19
 */
@ImplementedBy(EliteExamServiceImpl.class)
public interface EliteExamService {

    /**
     * 本店考试合格通过的学员列表
     * @param mapParams
     * @returns
     */
    List<Map<String,Object>> getExamTransitEmployees(Map<String, Object> mapParams)throws Exception;


    /**
     * 考试成绩插入(临时方法)
     * @param mapParams
     * @returns
     */
    int insertExamRecord(Map<String, Object> mapParams)throws Exception;



    //===========================================报表=====================================

    /**
     * 门店金鹰考试通过汇总
     * @return
     */
    Page getShopEliteExamPassCollect(Map<String, Object> mapParams)throws Exception;


    /**
     * 门店金鹰考试通过详情
     * @return
     */
    Page getShopEliteExamPassInfo(Map<String, Object> mapParams)throws Exception;


    /**
     * 月度考试汇总
     * @return
     */
    Page getExamCollectOfMonth(Map<String, Object> mapParams)throws Exception;
}
