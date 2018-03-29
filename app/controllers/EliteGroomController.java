package controllers;

import com.alibaba.fastjson.JSON;
import com.google.inject.Inject;
import com.hht.interceptor.Page;
import com.hht.view.ResultView;
import common.Constant;
import models.ProEliteGroom;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.CertificateService;
import service.EliteGroomService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/11 0011.
 */
public class EliteGroomController extends Controller {
    @Inject
    private FormFactory formFactory;

    @Inject
    private EliteGroomService eliteGroomService;

    @Inject
    private CertificateService certificateService;

    public Result getProEliteTrainGrooms() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            Page page = eliteGroomService.getProEliteTrainGrooms(mapParams);

            if (page.getList() != null && page.getList().size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", page));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", page));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }

    public Result addGroomEmployeesToClass() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            Map dataMap = new HashMap();
            if(mapParams.get("classType")!=null){
                String classType = mapParams.get("classType").toString();
                String employeeCode = mapParams.get("employeeCode").toString();
                dataMap.put("classType",classType);
                dataMap.put("employeeCode",employeeCode);
            }else{
                List<Map> list = JSON.parseArray(mapParams.get("list").toString(),Map.class);
                dataMap.putAll(list.get(0));
            }
            List<ProEliteGroom> groomList = eliteGroomService.getGroomEmployees(dataMap);
            if(groomList.size()>0){
                return ok(ResultView.fail(Constant.FAIL, "重复推荐"));
            }
            int insert = eliteGroomService.addGroomEmployeesToClass(mapParams);
            if (insert == 1) {
                return ok(ResultView.success(Constant.SUCCESS, "推荐成功", insert));
            }
            return ok(ResultView.fail(Constant.FAIL, "推荐失败"));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "推荐失败"));
        }

    }

    public Result getShopSelfGroomToLobby() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String, Object>> mapList = eliteGroomService.getShopSelfGroomToLobby(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }


    public Result getShopSelfGroomToLaksaClass() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String, Object>> mapList = eliteGroomService.getShopSelfGroomToLaksaClass(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }


    public Result deleteGroomEmployee() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null ||mapParams.size()==0 ) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        if (mapParams.get("employeeId")==null ) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "员工id为空"));
        }
        if (mapParams.get("groomId")==null ) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "推荐表主键为空"));
        }
        try {
            int delete = eliteGroomService.deleteGroomEmployee(mapParams);
            return ok(ResultView.success(Constant.SUCCESS, "删除成功", delete));

        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "删除失败"));
        }
    }
}
