package service;

import com.google.inject.ImplementedBy;
import service.impl.EliteToExamineImpl;

import java.util.Map;

/**
 * Created by MR.GANG on 2017/7/22.
 */
@ImplementedBy(EliteToExamineImpl.class)
public interface EliteToExamineService {
    int getExamine(Map<String,Object> mapParams);
}
