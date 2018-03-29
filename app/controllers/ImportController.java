package controllers;

import com.alibaba.fastjson.JSON;
import com.google.inject.Inject;
import com.hht.view.ResultView;
import common.Constant;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.ImportService;

import java.util.Map;


/**
 * Created by MR.GANG on 2017/10/30.
 */
public class ImportController extends Controller {
    @Inject
    private FormFactory formFactory;
    @Inject
    private ImportService importService;
    public Result getLobbyClassImport() {
        String param = request().body().asJson().toString();
        Map<String, Object> mapParams = (Map<String, Object>) JSON.parse(param);
        Map<String, Object> lobbyClassImport = importService.getLobbyClassImport(mapParams);
        return ok(ResultView.success(Constant.SUCCESS, "导入成功", lobbyClassImport));
    }
    public Result getLobbyAllClassImport(){//大堂班导入4
        String param = request().body().asJson().toString();
        Map<String, Object> mapParams = (Map<String, Object>) JSON.parse(param);
        Map<String, Object> lobbyClassImport = importService.getLobbyAllClassImport(mapParams);
        return ok(ResultView.success(Constant.SUCCESS, "导入成功", lobbyClassImport));
    }
    public Result getAllClassImport(){//店经理班导入
        String param = request().body().asJson().toString();
        Map<String, Object> mapParams = (Map<String, Object>) JSON.parse(param);
        Map<String, Object> lobbyClassImport = importService.getAllClassImport(mapParams);
        return ok(ResultView.success(Constant.SUCCESS, "导入成功", lobbyClassImport));
    }
    public Result getCardExport(){//导出发证模板
        String param = request().body().asJson().toString();
        Map<String, Object> mapParams = (Map<String, Object>) JSON.parse(param);
        Map<String, Object> lobbyClassImport = importService.getAllClassImport(mapParams);
        return ok(ResultView.success(Constant.SUCCESS, "导入成功", lobbyClassImport));
    }

    /**
     * 导入发证人员
     * @return
     */
    public   Result setExportCardPerson(){
        String param = request().body().asJson().toString();
        Map<String, Object> mapParams = (Map<String, Object>) JSON.parse(param);
//        Map<String, Object> lobbyClassImport = importService.getAllClassImport(mapParams);
        //进行班级人员校验

        //通过人员编码，班级查询导入班级 判断当前人员是否在该班级

        //导入发证人员,判断当前发证人员是否在模板中
        Map<String,Object> objectMap=importService.setExportCardPerson(mapParams);
        //查询班级人员导入数据
        return ok(ResultView.success(Constant.SUCCESS, "导入完成", objectMap));
    }
}
