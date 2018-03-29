package service.impl;

import com.google.inject.Inject;
import mapper.ProEliteClassMapper;
import mapper.ProEliteClassPersonMapper;
import mapper.ProEliteNoticeClassRecordMapper;
import mapper.ProShopEliteMapper;
import models.ProEliteClass;
import models.ProEliteClassPerson;
import models.ProEliteNoticeClassRecord;
import models.ProShopElite;
import org.mybatis.guice.transactional.Transactional;
import service.EliteAppSignUpService;
import ws.NoticeService;

import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/7/19.
 */
public class EliteAppSignUpServiceImpl implements EliteAppSignUpService {
    @Inject
    private  ProShopElite proShopElite;
    @Inject
    private ProEliteClassPersonMapper proEliteClassPersonMapper;

    @Inject
    private ProShopEliteMapper proShopEliteMapper;
    @Inject
    private NoticeService noticeService;
    @Inject
    private ProEliteClassMapper proEliteClassMapper;
    @Inject
    private ProEliteNoticeClassRecordMapper proEliteNoticeClassRecordMapper;
    @Inject
    private ProEliteNoticeClassRecord proEliteNoticeClassRecord;
    @Override
    @Transactional
    public int EmployeeSignUp(Map<String, Object> mapParams) throws Exception {
        String classType =mapParams.get("classType").toString();
        String signUp =mapParams.get("signUp").toString();
        String employeeCode =mapParams.get("employeeCode").toString();
        //新增消息主键
        String id =mapParams.get("id").toString();
        if(classType.equals("100")){//大堂班
            if(signUp.equals("true")){
                //更改员工状态
                proShopElite=new ProShopElite();
                proShopElite.setEmployeeCode(mapParams.get("employeeCode").toString());
                proShopElite.setClassStatus("200");//已确认
                //00待推
                int result=proShopEliteMapper.updateStatusByEmployeeCode(proShopElite);
                //更改消息状态
                proEliteNoticeClassRecord=new ProEliteNoticeClassRecord();
                proEliteNoticeClassRecord.setId(id);
                proEliteNoticeClassRecord.setIsUse("Y");
                proEliteNoticeClassRecordMapper.updateByPrimaryKeySelective(proEliteNoticeClassRecord);
                if(result>0){
                    ProEliteClass proEliteClass = proEliteClassMapper.getTermByEmployeeCode(mapParams);
                    String noticeStr= noticeService.sendNotice(employeeCode,"7",true,proEliteClass);
                    //推送消息 已同意  大堂班组班成功
                }
            }else{
                //更新员工状态
                proShopElite=new ProShopElite();
                proShopElite.setEmployeeCode(mapParams.get("employeeCode").toString());
                proShopElite.setClassStatus("100");//待推荐
                proShopEliteMapper.updateStatusByEmployeeCode(proShopElite);
                //更新通知状态
                proEliteNoticeClassRecord=new ProEliteNoticeClassRecord();
                proEliteNoticeClassRecord.setId(id);
                proEliteNoticeClassRecord.setIsUse("Y");
                proEliteNoticeClassRecord.setDr("Y");
                proEliteNoticeClassRecordMapper.updateByPrimaryKeySelective(proEliteNoticeClassRecord);
                //从组班中删除人员
                ProEliteClassPerson proEliteClassPerson=new ProEliteClassPerson();
                proEliteClassPerson.setDr("Y");
                proEliteClassPerson.setEmployeeCode(employeeCode);
                proEliteClassPersonMapper.updateClassPersonByEmployeeCode(proEliteClassPerson);
                //00待推

            }
        }else if(classType.equals("200")){//店经理班
            if(signUp.equals("true")){
                proShopElite=new ProShopElite();
                proShopElite.setEmployeeCode(mapParams.get("employeeCode").toString());
                proShopElite.setClassStatus("200");//已确认
                //00待推
                int result=proShopEliteMapper.updateStatusByEmployeeCode(proShopElite);

                //更新通知状态
                proEliteNoticeClassRecord=new ProEliteNoticeClassRecord();
                proEliteNoticeClassRecord.setId(id);
                proEliteNoticeClassRecord.setIsUse("Y");
                proEliteNoticeClassRecordMapper.updateByPrimaryKeySelective(proEliteNoticeClassRecord);

                if(result>0){
                    ProEliteClass proEliteClass = proEliteClassMapper.getTermByEmployeeCode(mapParams);
                    String noticeStr= noticeService.sendNotice(employeeCode,"9",true,proEliteClass);
                    //推送消息 已同意  大堂班组班成功
                }
            }else{
                proShopElite=new ProShopElite();
                proShopElite.setEmployeeCode(mapParams.get("employeeCode").toString());
                proShopElite.setClassStatus("100");//待推荐
                //00待推
                proShopEliteMapper.updateStatusByEmployeeCode(proShopElite);
                //更新通知状态
                proEliteNoticeClassRecord=new ProEliteNoticeClassRecord();
                proEliteNoticeClassRecord.setId(id);
                proEliteNoticeClassRecord.setIsUse("Y");
                proEliteNoticeClassRecord.setDr("Y");
                proEliteNoticeClassRecordMapper.updateByPrimaryKeySelective(proEliteNoticeClassRecord);

                ProEliteClassPerson proEliteClassPerson=new ProEliteClassPerson();
                proEliteClassPerson.setDr("Y");
                proEliteClassPerson.setEmployeeCode(employeeCode);
                proEliteClassPersonMapper.updateClassPersonByEmployeeCode(proEliteClassPerson);
            }
        }
        return 0;
    }
}
