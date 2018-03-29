package service;

import com.google.inject.ImplementedBy;
import com.hht.interceptor.Page;
import models.ProEliteTestRecord;
import models.ProShopElite;
import service.impl.ShopEliteServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @author qijianli
 *         Created by Administrator on 2017/7/18 0017.
 */
@ImplementedBy(ShopEliteServiceImpl.class)
public interface ShopEliteService {

    /**
     * 门店待推荐入池列表
     *
     * @param mapParams
     * @return
     */
    List<Map<String, Object>> getEmployees(Map<String, Object> mapParams) throws Exception;


    /**
     * 推荐入池 （批插入）
     *
     * @param mapParams
     * @return
     */
    int addPersonToShopElite(Map<String, Object> mapParams) throws Exception;


    /**
     * 查询未评测列表
     *
     * @param mapParams
     * @return
     */
    List<Map<String, Object>> getShopElitePersonsByNoTest(Map<String, Object> mapParams) throws Exception;

    /**
     * 查询已评测列表
     *
     * @param mapParams
     * @return
     */
    List<Map<String, Object>> getShopElitePersonsByTest(Map<String, Object> mapParams) throws Exception;


    /**
     * 审核
     *
     * @param mapParams
     * @return
     */
    List<ProShopElite> updateShopElitePersonsBycheck(Map<String, Object> mapParams) throws Exception;

    /**
     * 本店金鹰
     *
     * @param mapParams
     * @return
     */
    List<Map<String, Object>> getShopSelfElites(Map<String, Object> mapParams) throws Exception;


    /**
     * 删除金鹰
     *
     * @param mapParams
     * @return
     */
    int deleteShopSelfElites(Map<String, Object> mapParams) throws Exception;


    /**
     * 批量更新金鹰测试ID
     *
     * @param list
     */
    void batchUpdateForTest(List<Map> list);


    /**
     * 查询本店已组大堂班的员工（前台）
     *
     * @param mapParams
     * @returns
     */
    List<Map<String, Object>> getShopSelfInLobbyClass(Map<String, Object> mapParams) throws Exception;


    /**
     * 查询本店大堂班已发证员工（前台）
     *
     * @param mapParams
     * @returns
     */
    List<Map<String, Object>> getShopSelfPassLobbyClass(Map<String, Object> mapParams) throws Exception;


    /**
     * 查询本店已组店经理班的员工（前台）
     *
     * @param mapParams
     * @returns
     */
    List<Map<String, Object>> getShopSelfInLaksaClass(Map<String, Object> mapParams) throws Exception;


    /**
     * 查询本店店经理班已发证员工（前台）
     *
     * @param mapParams
     * @returns
     */
    List<Map<String, Object>> getShopSelfPassLaksaClass(Map<String, Object> mapParams) throws Exception;


    /**
     * 查询本店所有的毕业生（前台）
     *
     * @param mapParams
     * @returns
     */
    List<Map<String, Object>> getShopSelfGraduates(Map<String, Object> mapParams) throws Exception;


    /**
     * 金鹰池情况（前台）
     *
     * @param mapParams
     * @returns
     */
    Map<String, Object> getElitePoolCountByShopId(Map<String, Object> mapParams) throws Exception;


    /**
     * 本店获证情况（前台）
     *
     * @param mapParams
     * @returns
     */
    Map<String, Object> getShopCertificationCountByShopId(Map<String, Object> mapParams) throws Exception;


    /**
     * 首页测评列表（前台）
     *
     * @param mapParams
     * @returns
     */
    List<Map<String, Object>> getTestsByShopId(Map<String, Object> mapParams) throws Exception;


    /**
     * 首页考试列表（前台）
     *
     * @param mapParams
     * @returns
     */
    List<Map<String, Object>> getExamsByShopId(Map<String, Object> mapParams) throws Exception;


    /**
     * 根据职员代码查询
     *
     * @param employeeCode
     * @return
     */
    ProShopElite queryByEmployeeCode(String employeeCode);

    void batchUpdateForTestEnd(List<ProEliteTestRecord> list);

    /**
     * 查询没有测评返回结果的待测评金鹰人员
     *
     * @return
     */
    List<Map<String, Object>> queryForTest();

    /**
     * 金鹰池预警消息
     */
    void taskForElite();
    //========================后台统计报表========================================

    /**
     * 门店金鹰汇总
     *
     * @return
     */
    Page getShopEliteCountInfo(Map<String, Object> mapParams) throws Exception;


    /**
     * 根据门店Id查询金鹰人员
     *
     * @param mapParams
     * @return
     */
    Page getShopEliteByShopId(Map<String, Object> mapParams) throws Exception;

    /**
     * 根据主键查询门店金鹰
     * @param id
     * @return
     */
    ProShopElite selectByPrimaryKey(String id);

    /***
     *查询门店金鹰人数
     * @param shopId
     * @return
     */
    int selectCountByShopId(String shopId);
    List<Map<String,Object>> getClassForEmployeeCode(String employeeCode);

    /**
     * 更新门店金鹰表
     * @param shopElite
     */
    void updateByBean(ProShopElite shopElite);

    /**
     * 门店培训毕业生汇总
     * @param params
     * @return
     */
    Page getShopGraduateCollect(Map<String, Object> params) throws Exception;

    /**
     * 查询已推荐入池的人员
     * @param list
     * @return
     */
    List<ProShopElite> getElitesByCodeList(List<String> list)throws Exception;
}
