package controllers;

import com.hht.view.ResultView;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by MR.GANG on 2017/7/28.
 */
public class status extends Controller{
    public Result getStatus(){
        return ok(ResultView.success(200, "查询成功", null));
    }
}
