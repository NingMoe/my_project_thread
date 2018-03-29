package service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.hht.interceptor.Page;
import mapper.ProEliteNoticeMapper;
import models.ProEliteNotice;
import org.mybatis.guice.transactional.Transactional;
import service.EliteNoticeService;
import utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/17 0017.
 */
public class EliteNoticeServiceImpl implements EliteNoticeService {

    @Inject
    private ProEliteNoticeMapper noticeMapper;


    @Override
    @Transactional
    public int addEliteNotice(Map<String, Object> mapParams) throws Exception {
        ProEliteNotice notice = new ProEliteNotice();
        notice.setId(StringUtils.getTableId("ProEliteNotice"));
        notice.setNoticeTitle(StringUtils.objToString(mapParams.get("noticeTitle")));
        notice.setNoticeName(StringUtils.objToString(mapParams.get("noticeName")));
        notice.setNoticeRole(StringUtils.objToString(mapParams.get("noticeRole")));
        notice.setNoticeContent(StringUtils.objToString(mapParams.get("noticeContent")));
        notice.setEnable("Y");
        notice.setCreatorId(StringUtils.objToString(mapParams.get("creatorId")));
        notice.setCreateTime(System.currentTimeMillis());
        notice.setDr("N");
        //操作人
        notice.setOperatorId(StringUtils.objToString(mapParams.get("creatorId")));
        notice.setOperateTime(System.currentTimeMillis());
        int insert = noticeMapper.insertSelective(notice);
        return insert;
    }

    @Override
    @Transactional
    public int updateEliteNotice(Map<String, Object> mapParams) throws Exception {

        //    if (mapParams != null) {
        ProEliteNotice notice = new ProEliteNotice();
        notice.setId(StringUtils.objToString(mapParams.get("id")));
        notice.setNoticeTitle(StringUtils.objToString(mapParams.get("noticeTitle")));
        notice.setNoticeName(StringUtils.objToString(mapParams.get("noticeName")));
        notice.setNoticeRole(StringUtils.objToString(mapParams.get("noticeRole")));
        notice.setNoticeContent(StringUtils.objToString(mapParams.get("noticeContent")));
        notice.setModifierId(StringUtils.objToString(mapParams.get("modifierId")));
        notice.setModifyTime(System.currentTimeMillis());
        //操作人
        notice.setOperatorId(StringUtils.objToString(mapParams.get("modifierId")));
        notice.setOperateTime(System.currentTimeMillis());
        int update = noticeMapper.updateByPrimaryKeySelective(notice);
        return update;
    }


    //  }

    @Override
    @Transactional
    public int deleteEliteNotice(Map<String, Object> mapParams) throws Exception {

        //   if (mapParams != null) {
        List<ProEliteNotice> list = JSON.parseArray(mapParams.get("list").toString(), ProEliteNotice.class);
        for (ProEliteNotice notice : list) {
            notice.setDr("Y");
        }
        int delete = noticeMapper.updateEliteNoticeByList(list);
        return delete;
        //   }
        //    return 0;
    }

    @Override
    @Transactional
    public int updateUseOrDisableEliteNotice(Map<String, Object> mapParams) throws Exception {
        //  if (mapParams != null) {
        List<ProEliteNotice> list = JSON.parseArray(mapParams.get("list").toString(), ProEliteNotice.class);
        int update = noticeMapper.updateEliteNoticeByList(list);
        return update;
        //   }
        //   return 0;
    }

    @Override
    public Page getEliteNoticesByPage(Map<String, Object> mapParams) throws Exception {
        Page page = new Page();
        Map<String, Object> map = Maps.newHashMap();
        String noticeName = StringUtils.objToString(mapParams.get("noticeName"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(noticeName)) {
            map.put("noticeName", "%" + noticeName + "%");
        }
        //分页参数
        String pageNum = StringUtils.objToString(mapParams.get("pageNum"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(pageNum)) {
            page.setPageNum(Integer.valueOf(pageNum));
        } else {
            page.setPageNum(1);
        }
        String pageSize = StringUtils.objToString(mapParams.get("pageSize"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(pageSize)) {
            page.setPageRecordCount(Integer.valueOf(pageSize));
        } else {
            page.setPageRecordCount(10);
        }
        page.setParams(map);
        List<ProEliteNotice> persons = noticeMapper.getEliteNoticesByListPage(page);
        page.setList(persons);
        return page;

    }

    @Override
    public ProEliteNotice queryNoticeByNoticeCode(Map<String, Object> params) {
        return noticeMapper.queryNoticeByNoticeCode(params);
    }
}
