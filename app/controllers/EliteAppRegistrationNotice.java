package controllers;

import com.google.inject.Inject;
import com.hht.view.ResultView;
import models.ProEliteNoticeRecord;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteNoticeRecordService;

import java.util.List;
import java.util.Map;

/**
 * Created by MR.GANG on 2017/7/19.
 */
public class EliteAppRegistrationNotice extends Controller{
    @Inject
    private FormFactory formFactory;
    @Inject
    private EliteNoticeRecordService noticeRecordService;

    public Result getEmployeeNoticeRecords()
    {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
//            List<ProEliteNoticeRecord> mapList = noticeRecordService.getNoticeByEmployeeCode(mapParams);
            List<Map<String,Object>> mapList = noticeRecordService.getNoticesByEmployeeCode(mapParams);
            if (mapList!= null && mapList.size() > 0) {
                return ok(ResultView.success(0, "查询成功", mapList));
            }
            return ok(ResultView.success(0, "暂无数据", mapList));
        } catch (Exception e) {
            System.out.printf(e.getMessage());
            return ok(ResultView.fail(-1, "查询失败"));
        }
    }

}
