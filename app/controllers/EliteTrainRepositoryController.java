package controllers;

import com.google.inject.Inject;
import com.hht.interceptor.Page;
import com.hht.view.ResultView;
import common.Constant;
import models.ProEliteTrainRepository;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteTrainRepositoryService;

import java.util.Map;

import static common.PropertyController.map;

/**
 * Created by Administrator on 2017/7/11 0011.
 */
public class EliteTrainRepositoryController extends Controller {
    @Inject
    private FormFactory formFactory;

    @Inject
    private EliteTrainRepositoryService eliteTrainRepositoryService;


    public Result getProEliteTrainRepositorys() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            Page page = eliteTrainRepositoryService.getProEliteTrainRepositorys(mapParams);
            if (page.getList() != null && page.getList().size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", page));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", page));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }

    public Result insertProEliteTrainRepository() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            ProEliteTrainRepository repository = eliteTrainRepositoryService.addProEliteTrainRepository(mapParams);
            return ok(ResultView.success(Constant.SUCCESS, "保存成功", repository));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "保存失败"));
        }

    }

    public Result updateProEliteTrainRepository() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            Map<String, Object> map = eliteTrainRepositoryService.updateProEliteTrainRepository(mapParams);
            if ((boolean) map.get("flag")) {
                return ok(ResultView.success(Constant.SUCCESS,"修改成功",null));
            }
            return ok(ResultView.fail(Constant.FAIL, "修改失败"));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL,"修改失败"));
        }

    }

    public Result deleteProEliteTrainRepository() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            Map<String, Object> map = eliteTrainRepositoryService.deleteProEliteTrainRepository(mapParams);
            if ((boolean) map.get("flag")) {
                return ok(ResultView.success(Constant.SUCCESS,"删除成功",null));
            }
            return ok(ResultView.fail(Constant.FAIL, map.get("msg").toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, map.get("msg").toString()));
        }

    }


}
