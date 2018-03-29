package controllers;

import com.google.inject.Inject;
import com.hht.view.ResultView;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteAppAuditService;

import java.util.List;
import java.util.Map;
/**
 * Created by MR.GANG on 2017/7/26.
 */
public class EliteAppAuditController extends Controller{
    @Inject
    private FormFactory formFactory;
    @Inject
    private EliteAppAuditService eliteAppAuditService;
    public Result updateAudit(){
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        int result= 0;
        try {
            result = eliteAppAuditService.updateEmployeeStatus(mapParams);
        } catch (Exception e) {
            result=-1;
        }
        if(result>0){
            return ok(ResultView.success(0, "审批完成", null));
        }else{
            return ok(ResultView.success(1, "确认失败", null));
        }
      }
    public Result getAuditMessege() {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String, Object>> mapList = eliteAppAuditService.getAudit(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(0, "查询成功", mapList));
            }
            return ok(ResultView.success(0, "暂无数据", mapList));
        } catch (Exception e) {
            return ok(ResultView.fail(-1, "查询失败"));
        }
    }
}
