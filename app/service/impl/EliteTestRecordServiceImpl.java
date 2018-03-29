package service.impl;

import com.google.inject.Inject;
import mapper.ProEliteTestRecordMapper;
import models.ProEliteNoticeRecord;
import models.ProEliteTestRecord;
import models.vo.ProEmployeeVo;
import org.mybatis.guice.transactional.Transactional;
import service.EliteNoticeRecordService;
import service.EliteTestRecordService;
import service.EmployeeService;
import service.ShopEliteService;
import utils.StringUtils;
import ws.EvaluationService;
import ws.NoticeService;

import java.io.IOException;
import java.util.*;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/19 15:24
 */
public class EliteTestRecordServiceImpl implements EliteTestRecordService {

    @Inject
    private ProEliteTestRecordMapper recordMapper;

    @Inject
    private EvaluationService evaluationService;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private EliteNoticeRecordService noticeRecordService;

    @Inject
    private ShopEliteService shopEliteService;

    @Inject
    private NoticeService noticeService;

    @Override
    @Transactional
    public void batchInsertRecord(List<ProEliteTestRecord> list) {
        recordMapper.batchInsertRecord(list);
    }

    @Override
    public List<ProEliteTestRecord> queryListByTestOnceId(List<String> list) {
        return recordMapper.queryListByTestOnceId(list);
    }

    @Override
    public void batchUpdateById(List<ProEliteTestRecord> list) {
        recordMapper.batchUpdateById(list);
    }

    @Override
    @Transactional
    public void updateByTestId(ProEliteTestRecord record) {
        ProEliteTestRecord testRecord = recordMapper.queryRecordByTestOnceId(record.getTestOnceId());
        List<ProEliteTestRecord> list  = new ArrayList<>();
        list.add(testRecord);
        shopEliteService.batchUpdateForTestEnd(list);
        List<ProEmployeeVo> employees = employeeService.queryByCode(testRecord.getEmployeeCode());
        ProEmployeeVo employee = employees.get(0);
        recordMapper.updateByTestId(record);
        ProEliteNoticeRecord noticeRecord = new ProEliteNoticeRecord();
        noticeRecord.setDr("N");
        noticeRecord.setId(StringUtils.getTableId("pro_elite_notice_record"));
        noticeRecord.setCreateTime(System.currentTimeMillis());
        noticeRecord.setCreatorId("system");
        noticeRecord.setEmployeeCode(employee.getEmployeeCode());
        noticeRecord.setEmployeeId(employee.getId());
        noticeRecord.setIsRead("N");
        noticeRecord.setNoticeCode("110");
        noticeRecord.setNoticeText("您的测评已经于"+record.getTestEndTime()+"提交，请耐心等待店经理审核。");
        noticeRecord.setNoticeName("在线测评");
        noticeRecordService.insertNoticeRecord(noticeRecord);
        noticeService.sendNotice(employee.getEmployeeCode(),"2",true,null);

    }

    @Override
    @Transactional
    public void getTestNotRecord() throws IOException {
        List<ProEliteTestRecord> list = recordMapper.getTestNotRecord();
        if(list.size()>0){
            String result = evaluationService.getTestRecord(list);
            if ("FAIL".equals(result)){
                return;
            }
            for (ProEliteTestRecord testRecord : list) {
                testRecord.setIsTask("Y");
                testRecord.setModifierId("SystemEnd");
                testRecord.setModifyTime(System.currentTimeMillis());
            }
            recordMapper.batchUpdateForTestEnd(list);
        }
    }

    @Override
    public void getTestNotUrl() throws IOException {
        //查询没有测评报告的金鹰池待测评人员
        List<Map<String,Object>> list = shopEliteService.queryForTest();
        if(!list.isEmpty()) {
            List<Map<String,Object>> maps = new ArrayList<>();
            //查询对应人员没有测评记录的
            List<ProEliteTestRecord> testRecords = recordMapper.queryList(list);
            for (Map<String, Object> map : list) {
                for (ProEliteTestRecord record : testRecords) {
                    if(map.get("employeeCode").equals(record.getEmployeeCode())){
                        maps.add(map);
                    }
                }
            }
            list.removeAll(maps);
            maps.clear();
            if (!list.isEmpty()){
                for (Map<String, Object> map : list) {
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("CandidateUniqueId",map.get("testId"));
                    temp.put("ExternalId",map.get("employeeCode"));
                    temp.put("Name",map.get("employeeName"));
                    temp.put("Gender",map.get("sex"));
                    temp.put("Email",map.get("email"));
                    temp.put("Mobile",map.get("mobileNo"));
                    temp.put("shopName",map.get("shopName"));
                    temp.put("position",map.get("positionName"));
                    temp.put("firstTime",map.get("firstTime"));
                    temp.put("lastTime",map.get("lastTime"));
                    temp.put("culture",map.get("culture"));
                    Integer age = null;
                    try {
                        Calendar c = Calendar.getInstance();
                        c.setTime(new Date());
                        Calendar c1 = Calendar.getInstance();
                        Date date = (Date) map.get("birthday");
                        c1.setTime(date);
                        age = c.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    temp.put("Age",age);
                    maps.add(temp);
                }
                evaluationService.sendTestPerson(maps);
            }

        }

    }

    @Override
    @Transactional
    public void getTestEnd() throws IOException {
        //查询1小时 没有测评结束时间的金鹰池待测评人员
        List<ProEliteTestRecord> list = recordMapper.queryForTestEnd();
        if (list.isEmpty()){
            return;
        }
        List<ProEliteTestRecord> testEnd = evaluationService.getTestEnd(list);
        if (testEnd.size()>0){
            recordMapper.batchUpdateForTestEnd(testEnd);
        }
    }
}
