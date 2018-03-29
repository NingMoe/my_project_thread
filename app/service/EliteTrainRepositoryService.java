package service;

import com.google.inject.ImplementedBy;
import com.hht.interceptor.Page;
import models.ProEliteTrainRepository;
import service.impl.EliteTrainRepositoryServiceImpl;

import java.util.Map;

/**
 *
 * @Author: Qi Jian Li
 * @Date: 17/7/12
 */
@ImplementedBy(EliteTrainRepositoryServiceImpl.class)
public interface EliteTrainRepositoryService {




    /**
     * 分页查询知识库资料
     * @param mapParams
     * @return
     */
    Page getProEliteTrainRepositorys(Map<String, Object> mapParams)throws Exception;

    /**
     * 新增资料
     * @param mapParams
     * @return
     */
    ProEliteTrainRepository addProEliteTrainRepository(Map<String, Object> mapParams)throws Exception;

    /**
     * 修改资料
     * @param mapParams
     * @return
     */
    Map<String,Object> updateProEliteTrainRepository(Map<String, Object> mapParams)throws Exception;

    /**
     * 删除资料
     * @param mapParams
     * @return
     */
    Map<String,Object> deleteProEliteTrainRepository(Map<String, Object> mapParams)throws Exception;
}
