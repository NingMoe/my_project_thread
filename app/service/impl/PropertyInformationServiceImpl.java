package service.impl;

import com.google.inject.Inject;
import com.hht.interceptor.Page;
import mapper.ElitePresentationHeadMapper;
import mapper.ProEliteGroomMapper;
import models.ElitePresentationHead;
import service.CertificateService;
import service.PropertyInformationService;

import java.util.List;
import java.util.Map;

import static utils.StringUtils.objToString;

/**
 * UserName:GaoQiYou
 * CreateTime:2017/11/1
 */
public class PropertyInformationServiceImpl implements PropertyInformationService {

    @Inject
    private ProEliteGroomMapper proEliteGroomMapper;

    @Inject
    private CertificateService certificateService;
    @Inject
    private ElitePresentationHeadMapper elitePresentationHeadMapper;

    @Override
    public Page getPropertyInformationPage(Map<String, Object> mapParams) throws Exception {
        String pageSize = objToString(mapParams.get("pageSize"));
        String pageNum = objToString(mapParams.get("pageNum"));
        Page page = new Page();
        //分页参数
        if (pageNum != null && !"".equals(pageNum)) {
            page.setPageNum(Integer.valueOf(pageNum));
        }
        if (pageSize != null && !"".equals(pageSize)) {
            page.setPageRecordCount(Integer.valueOf(pageSize));
        }
        page.setParams(mapParams);
        List<Map<String, Object>> mapList = proEliteGroomMapper.getPropertyInformationListPage(page);
        if (mapList != null && mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                List<Map<String, Object>> listCode = certificateService.queryListByCode(map.get("employeeCode").toString());
                if (listCode != null && listCode.size() > 0) {
                    map.put("certificateCount", listCode.size());
                } else {
                    map.put("certificateCount", 0);
                }
            }
    }
        page.setList(mapList);

        return page;
    }

    /**
     * 测评报告展示
     * @param mapParams
     * @return
     * @throws Exception
     */
    @Override
    public Page getPresentationPage(Map<String, Object> mapParams) throws Exception {
        String pageSize = objToString(mapParams.get("pageSize"));
        String pageNum = objToString(mapParams.get("pageNum"));
        Page page = new Page();
        //分页参数
        if (pageNum != null && !"".equals(pageNum)) {
            page.setPageNum(Integer.valueOf(pageNum));
        }
        if (pageSize != null && !"".equals(pageSize)) {
            page.setPageRecordCount(Integer.valueOf(pageSize));
        }
        page.setParams(mapParams);

        List<ElitePresentationHead> mapList = elitePresentationHeadMapper.getPresentationListPage(page);
        page.setList(mapList);
        return page;
    }

}
