package service.impl;

import com.alibaba.fastjson.JSON;
import com.google.inject.Inject;
import mapper.ProEliteClassMapper;
import mapper.ProEliteGroomMapper;
import mapper.ProShopEliteMapper;
import models.ProEliteClass;
import models.ProEliteGroom;
import models.ProShopElite;
import org.mybatis.guice.transactional.Transactional;
import service.EliteAppAuditService;
import ws.NoticeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by MR.GANG on 2017/7/26.
 */
public class EliteAppAuditServiceImpl implements EliteAppAuditService {
    @Inject
    private ProEliteGroom proEliteGroom;
    @Inject
    private ProEliteGroomMapper proEliteGroomMapper;
    @Inject
    private ProShopEliteMapper shopEliteMapper;
    @Inject
    private ProEliteClassMapper proEliteClassMapper;
    @Inject
    private NoticeService noticeService;
    @Inject
    private  ProShopElite proShopElite;
    @Inject
    private ProShopEliteMapper proShopEliteMapper;
    @Override
    @Transactional
    public int updateEmployeeStatus(Map<String, Object> mapParams) throws Exception {
        int result = 0;
        List<ProEliteGroom> list = null;
        try {

            if (mapParams != null) {
                list = JSON.parseArray(mapParams.get("list").toString(), ProEliteGroom.class);
                result = proEliteGroomMapper.updateByEmployeeCodeByList(list);
                if(list.size()==1){
                    if(list.get(0).getStatus().equals("100")){
                        proShopElite=new ProShopElite();
                        proShopElite.setEmployeeCode(list.get(0).getEmployeeCode());
                        proShopElite.setClassStatus("100");//已确认
                        //00待推
                        proShopEliteMapper.updateStatusByEmployeeCode(proShopElite);
                    }
                }
//                proShopElite=new ProShopElite();
//                proShopElite.setEmployeeCode(mapParams.get("employeeCode").toString());
//                proShopElite.setClassStatus("200");//已确认
//                //00待推
//                int result=proShopEliteMapper.updateStatusByEmployeeCode(proShopElite);
            }
            if (result > 0) {
                Map map = new HashMap();

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getStatus().toString().equals("100")) {
                        map.put("employeeCode", list.get(0).getEmployeeCode());
//                        ProEliteClass proEliteClass = proEliteClassMapper.getTermByEmployeeCode(map);
                        String noticeStr = noticeService.sendNoticeAu(list.get(0).getEmployeeCode(), "10", true, list.get(0).getRemark());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getAudit(Map<String, Object> mapParams) {
        List<Map<String, Object>> mapList = null;
        if (mapParams != null) {
            mapList = shopEliteMapper.getAudiInfoByShopId(mapParams);
        }
        return mapList;
    }
}
