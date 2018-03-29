package service;

import com.google.inject.ImplementedBy;
import com.hht.interceptor.Page;
import service.impl.ReportFormServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/8/14.
 */
@ImplementedBy(ReportFormServiceImpl.class)
public interface ReportFormService {
     //List<Map<String,Object>> selectEliteByShopName(Map<String, Object> mapParams);
    // List<Map<String,Object>> selectEliteByShopId(Map<String, Object> mapParams);

     Page selectEliteByShopName(Map<String, Object> mapParams)throws Exception;

     Page selectEliteByShopId(Map<String, Object> mapParams)throws Exception;

     Page queryReportForm(Map<String, Object> mapParams)throws Exception;
}
