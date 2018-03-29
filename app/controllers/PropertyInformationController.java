package controllers;

import com.google.inject.Inject;
import com.hht.interceptor.Page;
import com.hht.view.ResultView;
import common.Constant;
import play.data.FormFactory;
import play.mvc.Result;
import service.PropertyInformationService;

import java.util.Map;

import static play.mvc.Results.ok;

/**
 * UserName:GaoQiYou
 * CreateTime:2017/11/1
 */
public class PropertyInformationController {

    @Inject
     private FormFactory formFactory;
    @Inject
    private PropertyInformationService propertyInformationService;


    /**
     * PC端
     * 审核房源信息报表
     * @return
     * @throws Exception
     */
    public Result getPropertyInformationPage() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            Page page = propertyInformationService.getPropertyInformationPage(mapParams);
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
     * 测评报告展示
     * @return
     * @throws Exception
     */

    public Result getPresentationPage() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            Page page = propertyInformationService.getPresentationPage(mapParams);
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
