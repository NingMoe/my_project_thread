package service;

import com.google.inject.ImplementedBy;
import service.impl.EliteAppStudyServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/8/9.
 */
@ImplementedBy(EliteAppStudyServiceImpl.class)
public interface EliteAppStudyService {
    Map<String, List<Map<String,Object>>>  getAppStudy();
}
