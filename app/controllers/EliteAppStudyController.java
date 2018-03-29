package controllers;

import com.google.inject.Inject;
import com.hht.interceptor.Page;
import com.hht.view.ResultView;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteAppStudyService;
import service.EliteTrainRepositoryService;

import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/7/19.
 */
public class EliteAppStudyController extends Controller{
    @Inject
    private FormFactory formFactory;

    @Inject
    private EliteTrainRepositoryService eliteTrainRepositoryService;
    @Inject
    private EliteAppStudyService eliteAppStudyService;
    public Result getProEliteTrainRepositorys() throws Exception {
        Result result ;
        Map<String, List<Map<String,Object>>> stringListMap=eliteAppStudyService.getAppStudy();
         try {
            if (stringListMap != null&&stringListMap.size()>0) {
                return ok(ResultView.success(0, "查询成功", stringListMap));
            }
            return ok(ResultView.success(0, "暂无数据", stringListMap));
        } catch (Exception e) {
            result =  ok(ResultView.success(-1, "查询失败", stringListMap));
        }
        return result;
    }


}
