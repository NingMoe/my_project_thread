package service;

import com.google.inject.ImplementedBy;
import service.impl.EliteAppAuditServiceImpl;
import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/7/26.
 */
@ImplementedBy(EliteAppAuditServiceImpl.class)
public interface EliteAppAuditService {
    int updateEmployeeStatus(Map<String,Object> mapParams)throws Exception;
    List<Map<String, Object>> getAudit(Map<String,Object> mapParams);
}
