package controllers;

import com.google.inject.Inject;
import com.hht.interceptor.Page;
import com.hht.view.ResultView;
import common.Constant;
import models.ProEliteClass;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteClassService;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/11 0011.
 */
public class EliteClassController extends Controller {
    @Inject
    private FormFactory formFactory;

    @Inject
    private EliteClassService eliteClassService;

    public Result insertProEliteClass() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        ProEliteClass eliteClass = null;
        try {
            eliteClass = eliteClassService.addProEliteClass(mapParams);
            return ok(ResultView.success(Constant.SUCCESS, "保存成功", eliteClass));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.success(Constant.FAIL, "保存失败", eliteClass));
        }

    }


    public Result updateProEliteClass() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            int update = eliteClassService.updateProEliteClass(mapParams);
            if (update == 1) {
                return ok(ResultView.success(Constant.SUCCESS, "修改成功", update));
            }
            return ok(ResultView.fail(Constant.FAIL, "修改失败"));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "修改失败"));
        }


    }

    /**
     * 查询学期班级(分页)
     *
     * @return
     */

    public Result getProEliteClasssByPage() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            Page page = eliteClassService.getProEliteClasssByPage(mapParams);
            if (page.getList() != null && page.getList().size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", page));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", page));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }

    public Result getProEliteClassByTerm() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            Map<String, Object> map = eliteClassService.getProEliteClassByTerm(mapParams);
            if (map != null && map.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", map));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", map));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }

    //------------------------报表汇总--------------------------------------------------------------
    /**
     * 班级毕业情况（大堂班，店经理班）
     *
     * @return
     */

    public Result getClassGraduatesCollect() throws Exception {
        Map<String, Object> params = formFactory.form().bindFromRequest().get().getData();
        if (params == null||params.size()==0) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        if (params.get("pageNum")==null||params.get("pageSize")==null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "分页参数为空"));
        }
        if (params.get("classType")==null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "班级类型为空"));
        }

        try {

            Page page = eliteClassService.getClassGraduatesCollect(params);
            if (page.getList() != null && page.getList().size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", page));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", page));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }

}






