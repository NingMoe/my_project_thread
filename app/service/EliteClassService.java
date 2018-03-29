package service;

import com.google.inject.ImplementedBy;
import com.hht.interceptor.Page;
import models.ProEliteClass;
import service.impl.EliteClassServiceImpl;

import java.util.Map;

/**
 *
 * @Author: Qi Jian Li
 * @Date: 17/7/11
 */
@ImplementedBy(EliteClassServiceImpl.class)
public interface EliteClassService {


    /**
     * 新增学期班级
     * @param mapParams
     * @return
     */
    ProEliteClass addProEliteClass(Map<String, Object> mapParams)throws Exception;


    /**
     * 修改学期班级
     * @param mapParams
     * @return
     */
    int updateProEliteClass(Map<String, Object> mapParams)throws Exception;

    /**
     * 查询学期班级(分页)
     * @param mapParams
     * @return
     */
    Page getProEliteClasssByPage(Map<String, Object> mapParams)throws Exception;

    /**
     * 根据学期查询班级
     * @param mapParams
     * @return
     */
    Map<String, Object> getProEliteClassByTerm(Map<String, Object> mapParams)throws Exception;
    /**
     * 班级毕业情况（大堂班，店经理班）
     * @return
     */
    Page getClassGraduatesCollect(Map<String, Object> params)throws Exception;
}
