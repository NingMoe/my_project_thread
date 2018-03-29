package mapper;

import models.ElitePresentationBody;

import java.util.List;

public interface ElitePresentationBodyMapper {
    int deleteByPrimaryKey(String id);

    int insert(ElitePresentationBody record);

    int insertSelective(ElitePresentationBody record);

    ElitePresentationBody selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ElitePresentationBody record);

    int updateByPrimaryKey(ElitePresentationBody record);

    int setBodyList(List<ElitePresentationBody> list);
}