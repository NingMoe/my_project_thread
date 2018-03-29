package controllers;

import com.google.inject.Inject;
import com.hht.view.ResultView;
import models.ProEliteExaminationOnline;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteAppExaminationService;

import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/8/2.
 */
public class EliteAppExaminationController  extends Controller{
    @Inject
    private FormFactory formFactory;
    @Inject
    private EliteAppExaminationService eliteAppExaminationService;
    public Result getEmployeeNoticeRecords(){
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
         List<Map<String, Object>> mapList= eliteAppExaminationService.getExaminationOnline(mapParams);
        if(mapList!=null&&mapList.size()!=0){
            return ok(ResultView.success(0, "查询成功", mapList));
        }else{
            return ok(ResultView.success(1, "暂无数据", null));
        }
    }
    public Result getProBeanByEmployee(){
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        ProEliteExaminationOnline proEliteExaminationOnline = eliteAppExaminationService.getProBeanByEmployee(mapParams);
        if(proEliteExaminationOnline!=null){
            return ok(ResultView.success(0, "查询成功", proEliteExaminationOnline));
        }else{
            return ok(ResultView.success(1, "暂无数据", null));
        }
    }
}
