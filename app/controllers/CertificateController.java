package controllers;

import com.google.inject.Inject;
import com.hht.view.ResultView;
import common.Constant;
import play.data.FormFactory;
import play.mvc.Result;
import service.CertificateService;

import java.util.List;
import java.util.Map;

import static play.mvc.Results.ok;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/8/5 16:37
 */
public class CertificateController {

    @Inject
    private FormFactory formFactory;

    @Inject
    private CertificateService certificateService;

    public Result getCertificateByCode(){
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        String employeeCode = mapParams.get("employeeCode").toString();
        List<Map<String,Object>> list = certificateService.queryListByCode(employeeCode);
        if(list != null && list.size()>0){
            return ok(ResultView.success(Constant.SUCCESS,Constant.REQ_SUCCESS,list));
        }else{
            return ok(ResultView.fail(Constant.FAIL,Constant.REQ_FAIL));
        }
    }
}
