package controllers;

import com.google.inject.Inject;
import com.hht.view.ResultView;
import common.Constant;
import models.ProEliteNoticeClass;
import play.data.FormFactory;
import play.mvc.Result;
import service.NoticeClassService;

import java.util.List;
import java.util.Map;

import static play.mvc.Results.ok;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/7/28 15:43
 */
public class NoticeClassController {

    @Inject
    private FormFactory formFactory;

    @Inject
    private NoticeClassService noticeClassService;

    public Result queryList() {
        try {
            List<ProEliteNoticeClass> list = noticeClassService.getNoticeList();
            if (list != null && list.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", list));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", list));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }

    public Result addNotice() {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            noticeClassService.addNotice(mapParams);

            return ok(ResultView.success(Constant.SUCCESS, "保存成功", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "保存失败"));
        }
    }

    public Result noticeRest(){
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            ProEliteNoticeClass noticeClass = noticeClassService.noticeRest(mapParams);

            return ok(ResultView.success(Constant.SUCCESS, "初始化成功", noticeClass));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "初始化失败"));
        }
    }
}
