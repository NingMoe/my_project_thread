package service;

import com.google.inject.ImplementedBy;
import com.hht.interceptor.Page;
import models.ProEliteNotice;
import service.impl.EliteNoticeServiceImpl;

import java.util.Map;

/**
 * @author qijianli
 *         Created by Administrator on 2017/7/17 0017.
 */
@ImplementedBy(EliteNoticeServiceImpl.class)
public interface EliteNoticeService {

    /**
     * 新增通知
     *
     * @param mapParams
     * @return
     */
    int addEliteNotice(Map<String, Object> mapParams) throws Exception;


    /**
     * 修改通知
     *
     * @param mapParams
     * @return
     */
    int updateEliteNotice(Map<String, Object> mapParams) throws Exception;

    /**
     * 删除(批删除)
     *
     * @param mapParams
     * @return
     */
    int deleteEliteNotice(Map<String, Object> mapParams) throws Exception;

    /**
     * 启用/停用 通知
     *
     * @param mapParams
     * @return
     */
    int updateUseOrDisableEliteNotice(Map<String, Object> mapParams) throws Exception;

    /**
     * 查询通知（分页）
     *
     * @param mapParams
     * @return
     */
    Page getEliteNoticesByPage(Map<String, Object> mapParams) throws Exception;


    /**
     * 根据通知节点,通知角色查询通知
     * @param params
     * @return
     */
    ProEliteNotice queryNoticeByNoticeCode(Map<String,Object> params);
}
