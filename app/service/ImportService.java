package service;

import com.google.inject.ImplementedBy;
import service.impl.ImportServiceImpl;

import java.util.Map;

/**
 * Created by MR.GANG on 2017/10/30.
 */
@ImplementedBy(ImportServiceImpl.class)
public interface ImportService {
    Map<String,Object> getLobbyClassImport(Map<String,Object> params);//大堂班补录信息导入
    Map<String,Object> getLobbyAllClassImport(Map<String,Object> params);
    Map<String,Object> getAllClassImport(Map<String,Object> params);//店经理人员信息导入
    Map<String,Object> setExportCardPerson(Map<String,Object> params);//导入发证人员
}
