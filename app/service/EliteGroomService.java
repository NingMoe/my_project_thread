package service;

import com.google.inject.ImplementedBy;
import com.hht.interceptor.Page;
import models.ProEliteGroom;
import service.impl.EliteTrainGroomServiceImpl;

import java.util.List;
import java.util.Map;

/**
 *
 * @Author: Qi Jian Li
 * @Date: 17/7/14
 */
@ImplementedBy(EliteTrainGroomServiceImpl.class)
public interface EliteGroomService {

    /**
     * 查询所有报名大堂班的员工（后台）
     * @param mapParams
     * @returns
     */
    Page getProEliteTrainGrooms(Map<String, Object> mapParams)throws Exception;

    /**
     * 推荐员工报班（批插入）
     * @param mapParams
     * @return
     */
    int addGroomEmployeesToClass(Map<String, Object> mapParams) throws Exception;


    /**
     * 查询本店报名大堂班的员工（前台）
     * @param mapParams
     * @returns
     */
    List<Map<String,Object>> getShopSelfGroomToLobby(Map<String, Object> mapParams)throws Exception;


    /**
     * 查询本店报名店经理班的员工（前台）
     * @param mapParams
     * @returns
     */
    List<Map<String,Object>> getShopSelfGroomToLaksaClass(Map<String, Object> mapParams)throws Exception;

    /**
     * 查询员工是否已经推荐
     * @param mapParams
     * @return
     */
    List<ProEliteGroom> getGroomEmployees(Map<String, Object> mapParams)throws Exception;

    /**
     * 删除报名表员工
     * @return
     * @throws Exception
     */
    int deleteGroomEmployee(Map<String, Object> params)throws Exception;
}
