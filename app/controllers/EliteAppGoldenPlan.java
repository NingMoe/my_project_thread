package controllers;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.hht.view.ResultView;
import models.ProEliteNoticeRecord;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteAppGoldenPlanService;

import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/7/23.
 */
public class EliteAppGoldenPlan extends Controller{
    @Inject
    private EliteAppGoldenPlanService eliteAppGoldenPlanService;
    @Inject
    private FormFactory formFactory;

    public Result getEliteAppGoldenPlanStatus(){
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        List<Map<String,Object>> mapList= eliteAppGoldenPlanService.getTrainStatus(mapParams);
            if(mapList!=null&&mapList.size()!=0){
                return ok(ResultView.success(0, "查询成功", mapList));
            }else{
                return ok(ResultView.success(-1, "查询失败",mapList));
            }
    }

}
