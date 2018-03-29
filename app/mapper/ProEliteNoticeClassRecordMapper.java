package mapper;

import models.ProEliteNoticeClassRecord;

import java.util.List;
import java.util.Map;

public interface ProEliteNoticeClassRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProEliteNoticeClassRecord record);

    int insertSelective(ProEliteNoticeClassRecord record);

    ProEliteNoticeClassRecord selectByPrimaryKey(String id);
    List<ProEliteNoticeClassRecord> selectClassNoticeByEmployeeCode(Map<String,Object> map);

    int updateByPrimaryKeySelective(ProEliteNoticeClassRecord record);

    int updateByPrimaryKey(ProEliteNoticeClassRecord record);

    int deleteByEmployeeCode(String employeeCode);

    int insertBatchClassRecord(List<ProEliteNoticeClassRecord> list);

    int deleteByEmployeeCodeList(List<Map<String,Object>> mapList);
}