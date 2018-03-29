package service;

import com.google.inject.ImplementedBy;
import com.hht.interceptor.Page;
import models.ProEliteClassPerson;
import models.ProEliteClassPersonProject;
import service.impl.EliteClassPersonServiceImpl;
import vo.ProEliteClassPersonVo;

import java.util.List;
import java.util.Map;

/**
 *
 * @Author: Qi Jian Li
 * @Date: 17/7/15
 */
@ImplementedBy(EliteClassPersonServiceImpl.class)
public interface EliteClassPersonService {


    /**
     * 删除班级学员
     * @param mapParams
     * @return
     */
    int deleteEliteClassPerson(Map<String, Object> mapParams)throws Exception;


    /**
     * 组班
     * @param mapParams
     * @return
     */
    int addEliteClassPerson(Map<String, Object> mapParams)throws Exception;

    /**
     * 查询某学期班级下的全部学员(分页)
     * @param mapParams
     * @return
     */
    Page getEliteClassPersonByPage(Map<String, Object> mapParams)throws Exception;


    /**
     * 发证
     * @param mapParams
     * @return
     */
    Map<String, Object> updateClassCrtificate(Map<String, Object> mapParams)throws Exception;



    /**
     * 未通过（发证操作）
     * @param mapParams
     * @return
     */
    Map<String, Object> updateClassNotPass(Map<String, Object> mapParams)throws Exception;

    /**
     * 查询本店获得大堂证未在大堂岗位人员
     * @param mapParams
     * @returns
     */
    List<Map<String,Object>> getPassNotInLobby(Map<String, Object> mapParams)throws Exception;


    void sendNotice(Map<String, Object> result);

    void sendNotPassNotice(Map<String, Object> result);

    List<ProEliteClassPerson> getElitePersionList(List<ProEliteClassPersonVo> personVoList);

    /**
     * 查询员工是否已经组班
     * @param mapParams
     * @return
     */
    List<ProEliteClassPerson> getClassPersons(Map<String, Object> mapParams)throws Exception;

    /**
     * 查询员工报班次数
     * @param groom
     * @return
     */
    int getEmployeesClassTimesByType(Map<String, Object> groom)throws Exception;
    /**
     * 查询所有报过班次数
     */
    List<Map<String,Object>> getEmployeesAllTimesByType(Map<String, Object> groom)throws Exception;
    /**
     * 班级学员详情
     * @return
     */
    Page getClassPersonInfo(Map<String, Object> mapParams)throws Exception;
    /**
     * 支持批量更新
     */
    int  batchUpdate(List<ProEliteClassPersonVo> list);
    /**
     * 查询员工历史所在班级
     */
    Map<String,Object> getHistroy(Map<String,Object>params);

    /**
     *删除项目数据
     * @param mapParams
     * @return
     */
    int deleteEliteClassPersonProjectById(Map<String, Object> mapParams)throws Exception;

    /**
     * 根据ID
     * 查看项目信息
     * @param mapParams
     * @return
     */
    List<ProEliteClassPersonProject> getEliteClassPersonProjectList(Map<String, Object> mapParams)throws Exception;

    List<Map<String,Object>> getExportCard(Map<String, Object> mapParams)throws Exception;

}
