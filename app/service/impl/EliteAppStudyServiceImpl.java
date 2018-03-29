package service.impl;

import com.google.inject.Inject;
import mapper.ProEliteTrainRepositoryMapper;
import service.EliteAppStudyService;
import service.EliteTrainRepositoryService;

import java.util.*;

/**
 * Created by MR.GANG on 2017/8/9.
 */
public class EliteAppStudyServiceImpl implements EliteAppStudyService {
    @Inject
    private ProEliteTrainRepositoryMapper proEliteTrainRepositoryMapper;
    @Override
    public Map<String, List<Map<String,Object>>> getAppStudy() {
        List<Map<String, Object>> list=proEliteTrainRepositoryMapper.getRepository();
        Map<String, List<Map<String,Object>>> mapMap=new HashMap<>();
        Set<String> propertyIdList=new HashSet();
        for (int i = 0; i < list.size(); i++) {
            String  propertyId=list.get(i).get("propertyId").toString();
            propertyIdList.add(propertyId);
        }
        for (String str : propertyIdList) {
            List listArray=new ArrayList();
            for (int j = 0; j < list.size(); j++) {
                if(str.equals(list.get(j).get("propertyId").toString())){
                    listArray.add(list.get(j));
                }
            }
            mapMap.put(str,listArray);
        }
        return mapMap;
    }
}
