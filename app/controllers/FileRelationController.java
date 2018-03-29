package controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.inject.Inject;
import com.hht.utils.AssertUtil;
import com.hht.view.ResultView;
import constants.Constant;
import models.ProFileRelation;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.FileRelationService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 文件上传Controller
 *
 * @version 1.0
 * @since 2017-04-10
 */
public class FileRelationController extends Controller {

    @Inject
    private FormFactory formFactory;

    @Inject
    private FileRelationService fileRelationService;

    /**
     * 保存文件
     * @return
     * @throws IOException
     */
    public Result saveFilePath() throws IOException {
        String jsonStr = request().body().asJson().toString();
        List<ProFileRelation> list = JSON.parseArray(jsonStr, ProFileRelation.class);
        int result=fileRelationService.addFileRelationBatch(list);
        if(result>0){
            return badRequest(ResultView.fail(Constant.CODEMESSAGE.SAVEFAIL.code, Constant.CODEMESSAGE.SAVEFAIL.message));
        }
        return ok(ResultView.success(Constant.CODEMESSAGE.SUCCESS.message));
    }

    /**
     * 获取文件
     * @return
     * @throws IOException
     */
    public Result getFilePath() throws IOException {
        Map<String, Object> map = formFactory.form().bindFromRequest().get().getData();
        ProFileRelation proFileRelation = new JSONObject(map).toJavaObject(ProFileRelation.class);
        AssertUtil.assertEmpty(proFileRelation.getBillId(),Constant.CODEMESSAGE.IDISNULL.code,Constant.CODEMESSAGE.IDISNULL.message);
        List<ProFileRelation> list=fileRelationService.selectAll(proFileRelation);
        return ok(ResultView.success(list));
    }

    /**
     * 删除文件
     * @return
     * @throws IOException
     */
    public Result deleteFilePath() throws IOException {
        String jsonStr = request().body().asJson().toString();
        List<ProFileRelation> list = JSON.parseArray(jsonStr, ProFileRelation.class);
        int result=fileRelationService.delFileRelationBatch(list);
        if(result>0){
            return badRequest(ResultView.fail(Constant.CODEMESSAGE.SAVEFAIL.code,Constant.CODEMESSAGE.SAVEFAIL.message));
        }
        return ok(ResultView.success(Constant.CODEMESSAGE.SUCCESS.message));
    }
}
