package service.impl;

import com.google.inject.Inject;
import com.hht.interceptor.Page;
import mapper.ProShopEliteMapper;
import service.ReportFormService;

import java.util.List;
import java.util.Map;

import static utils.StringUtils.objToString;

/**
 * Created by MR.GANG on 2017/8/14.
 */
public class ReportFormServiceImpl implements ReportFormService {
    @Inject
    private ProShopEliteMapper proShopEliteMapper;

    //    @Override
//    public List<Map<String, Object>> selectEliteByShopName(Map<String, Object> mapParams) {
//        List<Map<String, Object>> mapList=proShopEliteMapper.selectEliteByShopName(mapParams);
//        return mapList;
//    }
//    @Override
//    public List<Map<String, Object>> selectEliteByShopId(Map<String, Object> mapParams) {
//        List<Map<String, Object>> mapList = proShopEliteMapper.selectEliteByShopId(mapParams);
//        List<Map<String, Object>> maps = proShopEliteMapper.selectManagerByShopId();
//        for (int i = 0; i < mapList.size(); i++) {
//            for (int j = 0; j < maps.size(); j++) {
//                if (mapList.get(i).get("shopId").equals(maps.get(j).get("shopId"))) {
//                    mapList.get(i).put("ManagerName", maps.get(j).get("employeeName"));
//                }
//            }
//        }
//        return mapList;
//    }

    @Override
    public Page selectEliteByShopName(Map<String, Object> mapParams) throws Exception {

        String pageSize = objToString(mapParams.get("pageSize"));
        String pageNum = objToString(mapParams.get("pageNum"));
        //设置分页参数
        Page page = new Page();
        //分页参数
        if (pageNum != null && !"".equals(pageNum)) {
            page.setPageNum(Integer.valueOf(pageNum));
        }
        if (pageSize != null && !"".equals(pageSize)) {
            page.setPageRecordCount(Integer.valueOf(pageSize));
        }
        page.setParams(mapParams);
        List<Map<String, Object>> mapList = proShopEliteMapper.selectEliteByShopListPage(page);
        page.setList(mapList);
        return page;
    }

    @Override
    public Page selectEliteByShopId(Map<String, Object> mapParams) throws Exception {
        String pageSize = objToString(mapParams.get("pageSize"));
        String pageNum = objToString(mapParams.get("pageNum"));
        //设置分页参数
        Page page = new Page();
        //分页参数
        if (pageNum != null && !"".equals(pageNum)) {
            page.setPageNum(Integer.valueOf(pageNum));
        }
        if (pageSize != null && !"".equals(pageSize)) {
            page.setPageRecordCount(Integer.valueOf(pageSize));
        }
        page.setParams(mapParams);

        List<Map<String, Object>> mapList = proShopEliteMapper.selectEliteByShopIdListPage(page);
        List<Map<String, Object>> maps = proShopEliteMapper.selectManagerByShopId();
        for (int i = 0; i < mapList.size(); i++) {
            for (int j = 0; j < maps.size(); j++) {
                if (mapList.get(i).get("shopId").equals(maps.get(j).get("shopId"))) {
                    mapList.get(i).put("ManagerName", maps.get(j).get("employeeName"));
                }
            }
        }
        page.setList(mapList);

        return page;
    }

    /**
     * 查询 导出明细信息
     * @param mapParams
     * @return
     * @throws Exception
     */
    @Override
    public Page queryReportForm(Map<String, Object> mapParams) throws Exception {
        String pageSize = objToString(mapParams.get("pageSize"));
        String pageNum = objToString(mapParams.get("pageNum"));
        //设置分页参数
        Page page = new Page();
        //分页参数
        if (pageNum != null && !"".equals(pageNum)) {
            page.setPageNum(Integer.valueOf(pageNum));
        }
        if (pageSize != null && !"".equals(pageSize)) {
            page.setPageRecordCount(Integer.valueOf(pageSize));
        }
        page.setParams(mapParams);

        List<Map<String, Object>> mapList = proShopEliteMapper.queryReportFormListPage(page);
        List<Map<String, Object>> maps = proShopEliteMapper.selectManagerByShopId();
        for (int i = 0; i < mapList.size(); i++) {
            for (int j = 0; j < maps.size(); j++) {
                if (mapList.get(i).get("shopId").equals(maps.get(j).get("shopId"))) {
                    mapList.get(i).put("ManagerName", maps.get(j).get("employeeName"));
                }
            }
        }
        page.setList(mapList);

        return page;
    }
}



