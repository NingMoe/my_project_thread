package controllers;

import com.google.inject.Inject;
import com.hht.view.ResultView;
import common.Constant;
import models.ProEliteTrainPool;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteTrainPoolService;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/11 0011.
 */
public class EliteTrainPoolController extends Controller {
    @Inject
    private FormFactory formFactory;

    @Inject
    private EliteTrainPoolService eliteTrainPoolService;

    public Result insertProEliteTrainPool() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        ProEliteTrainPool pool = null;
        try {
            pool = eliteTrainPoolService.addProEliteTrainPool(mapParams);
            return ok(ResultView.success(Constant.SUCCESS, "保存成功", pool));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "保存失败"));
        }

    }


    public Result updateProEliteTrainPool() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            Map<String, Object> map = eliteTrainPoolService.updateProEliteTrainPool(mapParams);
            if ((boolean) map.get("flag")) {
                return ok(ResultView.success(Constant.SUCCESS,"修改成功",null));
            }
            return ok(ResultView.fail(Constant.FAIL, "修改失败"));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "修改失败"));
        }

    }

    public Result getProEliteTrainPools() throws Exception {
        try {
            ProEliteTrainPool pool = eliteTrainPoolService.getProEliteTrainPools();

            if (pool != null) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", pool));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", pool));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }


}
