package controllers;

import com.google.inject.Inject;
import com.hht.view.ResultView;
import models.ProEliteClass;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteAppSignUpService;

import java.util.Map;

/**
 * Created by MR.GANG on 2017/7/23.
 */
public class EliteAppSignUp extends Controller{
    @Inject
    private FormFactory formFactory;
    @Inject
    private EliteAppSignUpService eliteAppSignUpService;
    public Result updateEmployeeSignUp() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        int result=eliteAppSignUpService.EmployeeSignUp(mapParams);
        if(result==0){
            return ok(ResultView.success(0, "确认完成", null));
        }else{
            return ok(ResultView.success(1, "确认完成", null));
        }
    }
}
