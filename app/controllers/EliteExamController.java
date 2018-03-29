package controllers;

import com.google.inject.Inject;
import com.hht.interceptor.Page;
import com.hht.view.ResultView;
import common.Constant;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteExamService;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/11 0011.
 */
public class EliteExamController extends Controller {
    @Inject
    private FormFactory formFactory;

    @Inject
    private EliteExamService examService;


    public Result getExamTransitEmployees() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String, Object>> mapList = examService.getExamTransitEmployees(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }


    public Result insertExamRecord() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            int insert = examService.insertExamRecord(mapParams);
            if (insert == 1) {
                return ok(ResultView.success(Constant.SUCCESS, "保存成功", insert));
            }
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "保存失败"));
        }

    }

    /**
     * 门店金鹰考试通过汇总
     *
     * @return
     */
    public Result getShopEliteExamPassCollect() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            Page page = examService.getShopEliteExamPassCollect(mapParams);
            if (page.getList() != null && page.getList().size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", page));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", page));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }

    /**
     * 门店金鹰考试通过详情
     *
     * @return
     */
    public Result getShopEliteExamPassInfo() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            Page page = examService.getShopEliteExamPassInfo(mapParams);
            if (page.getList() != null && page.getList().size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", page));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", page));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }

    /**
     * 月度考试汇总
     *
     * @return
     */
    public Result getExamCollectOfMonth() throws Exception {
        Map<String, Object> params = formFactory.form().bindFromRequest().get().getData();
        if (params == null||params.size()==0) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        if (params.get("pageNum")==null||params.get("pageSize")==null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "分页参数为空"));
        }
        try {
            Page page = examService.getExamCollectOfMonth(params);
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
