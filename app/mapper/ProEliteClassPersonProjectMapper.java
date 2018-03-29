package mapper;

import models.ProEliteClassPerson;
import models.ProEliteClassPersonProject;

import java.util.List;
import java.util.Map;

public interface ProEliteClassPersonProjectMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProEliteClassPersonProject record);

    int insertSelective(ProEliteClassPersonProject record);

    ProEliteClassPersonProject selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProEliteClassPersonProject record);

    int updateByPrimaryKey(ProEliteClassPersonProject record);

    int updateEliteClassPersonProjectById(ProEliteClassPersonProject projects);

    List<ProEliteClassPersonProject> getEliteClassPersonProjectList(Map<String, Object> mapParams);

    int setAllProELiteClassPerson(List<ProEliteClassPersonProject> list);

    int batchDeleteProELiteClassPerson(List<ProEliteClassPerson> list);

    List<Map<String,Object>> selectExport(Map<String, Object> mapParams);
}