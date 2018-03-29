package controllers;

import com.google.inject.Inject;
import com.hht.interceptor.Page;
import com.hht.view.ResultView;
import common.Constant;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteNoticeService;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/16 0011.
 */
public class EliteNoticeController extends Controller {
    @Inject
    private FormFactory formFactory;

    @Inject
    private EliteNoticeService noticeService;

    public Result insertEliteNotice() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            int insert = noticeService.addEliteNotice(mapParams);
            if (insert == 0) {
                return ok(ResultView.fail(Constant.FAIL, "保存失败"));
            }
            return ok(ResultView.success(Constant.SUCCESS, "保存成功", insert));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "保存失败"));
        }

    }


    public Result updateEliteNotice() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            int update = noticeService.updateEliteNotice(mapParams);
            if (update == 0) {
                return ok(ResultView.fail(Constant.FAIL, "修改失败"));
            }
            return ok(ResultView.success(Constant.SUCCESS, "修改成功", update));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "修改失败"));
        }
    }

    public Result deleteEliteNotice() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            int delete = noticeService.deleteEliteNotice(mapParams);
            if (delete == 0) {
                return ok(ResultView.fail(Constant.FAIL, "删除失败"));
            }
            return ok(ResultView.success(Constant.SUCCESS, "删除成功", delete));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "删除失败"));
        }

    }

    public Result updateUseOrDisableEliteNotice() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            int update = noticeService.updateUseOrDisableEliteNotice(mapParams);
            if (update == 0) {
                return ok(ResultView.fail(Constant.FAIL, "操作失败"));
            }
            return ok(ResultView.success(Constant.SUCCESS, "操作成功", update));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "操作失败"));
        }

    }

    public Result getEliteNoticesByPage() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            Page page = noticeService.getEliteNoticesByPage(mapParams);
            if (page.getList() != null && page.getList().size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", page));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", page));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(-1, "查询失败"));
        }
    }


}






