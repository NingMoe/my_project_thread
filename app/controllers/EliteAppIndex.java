package controllers;

import com.google.inject.Inject;
import com.hht.view.ResultView;
import models.ProEliteClass;
import models.ProEliteNoticeClassRecord;
import models.ProEliteNoticeRecord;
import models.ProEliteTrainRepository;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteAppIndexService;
import service.EliteTrainRepositoryService;

import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/7/20.
 */
public class EliteAppIndex extends Controller{
    @Inject
    private FormFactory formFactory;
    @Inject
    private EliteAppIndexService eliteAppIndexService;
    //得到员工首页推送信息
    public  Result getEmployeeMessege(){
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        List<ProEliteNoticeRecord> proEliteNoticeRecordList= eliteAppIndexService.getEmployeeMessege(mapParams);
        try {
            return ok(ResultView.success(0, "保存成功", proEliteNoticeRecordList));
        } catch (Exception e) {
            return ok(ResultView.success(-1, "保存失败", proEliteNoticeRecordList));
        }
    }
//    public  Result getTestTest(){
//        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
//        List<ProEliteNoticeRecord> proEliteNoticeRecordList= eliteAppIndexService.getEmployeeMessege(mapParams);
//        try {
//            return ok(ResultView.success(0, "保存成功", proEliteNoticeRecordList));
//        } catch (Exception e) {
//            return ok(ResultView.success(-1, "保存失败", proEliteNoticeRecordList));
//        }
//    }
    public Result getGroupMessege(){
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        List<ProEliteNoticeClassRecord> proEliteNoticeClassRecordList= eliteAppIndexService.getEliteNoticeClass(mapParams);
        try {
            return ok(ResultView.success(0, "查询成功", proEliteNoticeClassRecordList));
        } catch (Exception e) {
            return ok(ResultView.success(-1, "查询失败", proEliteNoticeClassRecordList));
        }
    }

    /**
     * 查询员工发证所在学期
     * @return
     */
    public Result getClassByEmployeeCode(){
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        ProEliteClass proEliteClass= eliteAppIndexService.getClassByEmployeeCode(mapParams);
        try {
            return ok(ResultView.success(0, "查询成功", proEliteClass));
        } catch (Exception e) {
            return ok(ResultView.success(-1, "查询失败", proEliteClass));
        }
    }
}
