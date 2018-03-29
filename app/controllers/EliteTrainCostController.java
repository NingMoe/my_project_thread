package controllers;

import com.google.inject.Inject;
import com.hht.view.ResultView;
import common.Constant;
import models.ProEliteTrainCost;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteTrainCostService;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/11 0011.
 */
public class EliteTrainCostController extends Controller {
    @Inject
    private FormFactory formFactory;

    @Inject
    private EliteTrainCostService eliteTrainCostService;

    public Result insertProEliteTrainCost() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        ProEliteTrainCost trainCost = null;
        try {
            trainCost = eliteTrainCostService.addProEliteTrainCost(mapParams);
            return ok(ResultView.success(Constant.SUCCESS, "保存成功", trainCost));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.success(Constant.FAIL, "保存失败", trainCost));
        }

    }


    public Result updateProEliteTrainCost() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            Map<String, Object> map = eliteTrainCostService.updateProEliteTrainCost(mapParams);
            if ((boolean) map.get("flag")) {
                return ok(ResultView.success(Constant.SUCCESS,"修改成功",null));
            }
            return ok(ResultView.fail(Constant.FAIL, "修改失败"));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "修改失败"));
        }

    }

    public Result getProEliteTrainCosts() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            ProEliteTrainCost cost = eliteTrainCostService.getProEliteTrainCosts();
            if (cost != null) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", cost));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", cost));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }


}
