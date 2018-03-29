package service.impl;

import com.google.inject.Inject;
import com.hht.interceptor.Page;
import mapper.ProEliteTrainRepositoryMapper;
import mapper.ProEmployeeMapper;
import models.ProEliteTrainRepository;
import org.mybatis.guice.transactional.Transactional;
import service.EliteTrainRepositoryService;
import utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/12 0012.
 */
public class EliteTrainRepositoryServiceImpl implements EliteTrainRepositoryService {

    @Inject
    private  ProEliteTrainRepositoryMapper proEliteTrainRepositoryMapper;
    @Inject
    private  ProEmployeeMapper employeeMapper;


    @Override
    public  Page getProEliteTrainRepositorys(Map<String, Object> mapParams) throws Exception {
        Page page = new Page();
        //参数校验
        if(org.apache.commons.lang3.StringUtils.isNotBlank(StringUtils.objToString(mapParams.get("pageNum")))){
            page.setPageNum(Integer.valueOf(StringUtils.objToString(mapParams.get("pageNum"))));
        }else{
            page.setPageNum(0);
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(StringUtils.objToString(mapParams.get("pageSize")))){
            page.setPageRecordCount(Integer.valueOf(StringUtils.objToString(mapParams.get("pageSize"))));
        }else{
            page.setPageRecordCount(10);
        }
        List<Map<String,Object>> repositories = proEliteTrainRepositoryMapper.getRepositoryByListPage(page);

        page.setList(repositories);
        return page;
    }
    @Override
    @Transactional
    public ProEliteTrainRepository addProEliteTrainRepository(Map<String, Object> mapParams) throws Exception{
        ProEliteTrainRepository repository = new ProEliteTrainRepository();
        repository.setId(StringUtils.getTableId("repository"));
        repository.setPropertyId(StringUtils.objToString(mapParams.get("propertyId")));
        repository.setFileName(StringUtils.objToString(mapParams.get("fileName")));
        repository.setIsDown(StringUtils.objToString(mapParams.get("isDown")));
        repository.setOperatorId(StringUtils.objToString(mapParams.get("operatorId")));
        repository.setOperateTime(System.currentTimeMillis());
        repository.setDr("N");
        //文件上传成功后返回的文件路径
        repository.setFileUrl(StringUtils.objToString(mapParams.get("fileUrl")));
        repository.setFileCoverUrl(StringUtils.objToString(mapParams.get("fileCoverUrl")));
        repository.setFileAppCover(StringUtils.objToString(mapParams.get("fileAppCover")));
        repository.setFileAppUrl(StringUtils.objToString(mapParams.get("fileAppUrl")));
        proEliteTrainRepositoryMapper.insertSelective(repository);
        return repository;
    }

    @Override
    @Transactional
    public Map<String, Object> updateProEliteTrainRepository(Map<String, Object> mapParams) throws Exception{
        Map<String, Object> map = new HashMap<>();
        ProEliteTrainRepository repository = new ProEliteTrainRepository();
        repository.setId(StringUtils.objToString(mapParams.get("id")));
        repository.setPropertyId(StringUtils.objToString(mapParams.get("propertyId")));
        repository.setFileName(StringUtils.objToString(mapParams.get("fileName")));
        repository.setIsDown(StringUtils.objToString(mapParams.get("isDown")));
       // repository.setOperatorId(StringUtils.objToString(mapParams.get("operatorId")));
        repository.setModifierId(StringUtils.objToString(mapParams.get("modifierId")));
      //  repository.setOperateTime(System.currentTimeMillis());
        repository.setModifyTime(System.currentTimeMillis());
        repository.setDr("N");
        repository.setFileUrl(StringUtils.objToString(mapParams.get("fileUrl")));
        //封面
        repository.setFileCoverUrl(StringUtils.objToString(mapParams.get("fileCoverUrl")));
        repository.setFileAppCover(StringUtils.objToString(mapParams.get("fileAppCover")));
        repository.setFileAppUrl(StringUtils.objToString(mapParams.get("fileAppUrl")));
        proEliteTrainRepositoryMapper.updateByPrimaryKeySelective(repository);
        map.put("flag",true);
        return map;
    }


    @Override
    @Transactional
    public Map<String, Object> deleteProEliteTrainRepository(Map<String, Object> mapParams) throws Exception {
        Map<String, Object> map = new HashMap<>();
        ProEliteTrainRepository repository  = new ProEliteTrainRepository();
        repository.setId( StringUtils.objToString(mapParams.get("repositoryId")));
        repository.setDr("Y");
        proEliteTrainRepositoryMapper.updateByPrimaryKeySelective(repository);
        map.put("flag", true);
        return map;
    }

}
