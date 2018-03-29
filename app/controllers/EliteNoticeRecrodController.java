package controllers;

import com.google.inject.Inject;
import com.hht.view.ResultView;
import common.Constant;
import models.ProEliteNoticeRecord;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteNoticeRecordService;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/21 0011.
 */
public class EliteNoticeRecrodController extends Controller {
    @Inject
    private FormFactory formFactory;
    @Inject
    private EliteNoticeRecordService noticeRecordService;

    public Result getLaksaSelfNoticeRecords() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<ProEliteNoticeRecord> mapList = noticeRecordService.getLaksaSelfNoticeRecords(mapParams);
            if (mapList!= null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }

    public Result getUnreadNoticeCount() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            int unread = noticeRecordService.getUnreadNoticeCount(mapParams);
            return ok(ResultView.success(Constant.SUCCESS, "查询成功", unread));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }



    public Result updateNoticeRerodIsRead() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            int update = noticeRecordService.updateNoticeRerodIsRead(mapParams);
            if (update == 1) {
                return ok(ResultView.success(Constant.SUCCESS, "修改成功", update));
            }
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "修改失败"));
        }
    }
}
