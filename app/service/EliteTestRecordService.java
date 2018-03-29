package service;

import com.google.inject.ImplementedBy;
import models.ProEliteTestRecord;
import service.impl.EliteTestRecordServiceImpl;

import java.io.IOException;
import java.util.List;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/19 15:24
 */
@ImplementedBy(EliteTestRecordServiceImpl.class)
public interface EliteTestRecordService {

    void batchInsertRecord(List<ProEliteTestRecord> list);

    /**
     * 根据善择返回单次测试ID批量查询
     * @param list
     * @return
     */
    List<ProEliteTestRecord> queryListByTestOnceId(List<String> list);

    /**
     * 批量更新
     * @param list
     */
    void batchUpdateById(List<ProEliteTestRecord> list);

    void updateByTestId(ProEliteTestRecord record);

    void getTestNotRecord() throws IOException;

    void getTestNotUrl() throws IOException;

    void getTestEnd() throws IOException;
}
