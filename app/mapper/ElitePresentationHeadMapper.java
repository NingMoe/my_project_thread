package mapper;

import com.hht.interceptor.Page;
import models.ElitePresentationHead;

import java.util.List;
import java.util.Map;

public interface ElitePresentationHeadMapper {
    int deleteByPrimaryKey(String id);

    int insert(ElitePresentationHead record);

    int insertSelective(ElitePresentationHead record);

    ElitePresentationHead selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ElitePresentationHead record);

    int updateByPrimaryKey(ElitePresentationHead record);

    List<ElitePresentationHead> getPresentationListPage(Page page);

    int setHeadList(List<ElitePresentationHead>  list);

}