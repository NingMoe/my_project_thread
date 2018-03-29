package common;

import com.google.inject.Inject;
import com.hht.view.ResultView;
import models.ProEmployee;
import models.ProPosition;
import models.ProShop;
import play.Logger;
import play.data.FormFactory;
import play.mvc.Result;
import service.EmployeeService;
import service.PositionService;
import service.ShopService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.mvc.Results.ok;

/**
 * <b>人员信息查询</b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/14 16:38
 */
public class PropertyController {

    public static HashMap<String,ProEmployee> map = new HashMap<>();
    public static HashMap<String,String> proPositionHashMap = new HashMap<>();

    @Inject
    private FormFactory formFactory;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private PositionService positionService;

    @Inject
    private ShopService shopService;

    public Result getEmployee(){
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();

        String empoloyeeId = String.valueOf(mapParams.get("empoloyeeId"));

        // 接口编码不可为空
        if(empoloyeeId == null){
            return ok(ResultView.fail(Constant.INTER_FAIL,"员工ID不能为空"));
        }
        ProEmployee employee = map.get(empoloyeeId);
        if(employee==null){
            employee = employeeService.queryById(empoloyeeId);
            map.put(empoloyeeId,employee);
        }

        if(employee == null){
            Logger.info("请求成功，不存在数据："+ResultView.success(Constant.INTER_SUCCESS,Constant.REQ_SUCCESS,null));
            return ok(ResultView.success(Constant.INTER_SUCCESS,Constant.REQ_SUCCESS,null));
        }else{
            Logger.info("请求成功，响应数据："+ResultView.success(Constant.INTER_SUCCESS,Constant.REQ_SUCCESS,employee));
            return ok(ResultView.success(Constant.INTER_SUCCESS,Constant.REQ_SUCCESS,employee));
        }

    }

    public Result getPosition(){
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();

        String id = String.valueOf(mapParams.get("id"));

        String positionName = proPositionHashMap.get(id);
        if(positionName==null || "".equals(positionName)){
            ProPosition position = positionService.queryById(id);
            proPositionHashMap.put(id,position.getPositionName());
        }

        if(positionName==null || "".equals(positionName)){
            Logger.info("请求成功，不存在数据："+ResultView.success(Constant.INTER_SUCCESS,Constant.REQ_SUCCESS,null));
            return ok(ResultView.success(Constant.INTER_SUCCESS,Constant.REQ_SUCCESS,null));
        }else{
            Logger.info("请求成功，响应数据："+ResultView.success(Constant.INTER_SUCCESS,Constant.REQ_SUCCESS,positionName));
            return ok(ResultView.success(Constant.INTER_SUCCESS,Constant.REQ_SUCCESS,positionName));
        }
    }

    public Result getPositions(){
            List<ProPosition> positions = positionService.Positions();
            HashMap<String,String> map = new HashMap<>();
        for (ProPosition position : positions) {
            map.put(position.getId(),position.getPositionName());
        }
            Logger.info("请求成功，响应数据："+ResultView.success(Constant.INTER_SUCCESS,Constant.REQ_SUCCESS,map));
            return ok(ResultView.success(Constant.INTER_SUCCESS,Constant.REQ_SUCCESS,map));
    }

    public Result getShopNames(){
        List<ProShop> shops = shopService.queryShopName();
        HashMap<String,String> map = new HashMap<>();
        for (ProShop proShop : shops) {
            map.put(proShop.getId(),proShop.getShopName());
        }
        Logger.info("请求成功，响应数据："+ResultView.success(Constant.INTER_SUCCESS,Constant.REQ_SUCCESS,map));
        return ok(ResultView.success(Constant.INTER_SUCCESS,Constant.REQ_SUCCESS,map));
    }
}
