package controllers;

import com.google.inject.Inject;
import com.hht.view.ResultView;
import common.Constant;
import models.ProPosition;
import models.ProShopElite;
import models.vo.LoginEmployeeVo;
import models.vo.ProEmployeeVo;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.CertificateService;
import service.EmployeeService;
import service.ShopEliteService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/7/27 17:45
 */
public class LoginController extends Controller {

    @Inject
    private FormFactory formFactory;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private ShopEliteService eliteService;

    public Result loginElite() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        String employeeCode = mapParams.get("employeeCode").toString();
        List<LoginEmployeeVo> employees = employeeService.queryForLogin(employeeCode);
        if(employees==null || employees.size()==0){
            return ok(ResultView.fail(Constant.FAIL, "用户不存在"));
        }
        //判断是否是店经理
        for (LoginEmployeeVo employee : employees) {
                if ("店经理".equals(employee.getPositionName())) {
                    map.put("shopId", employee.getShopId());
                    map.put("employeeId", employee.getId());
                    map.put("employeeCode", employeeCode);
                    map.put("shopName", employee.getShopName());
                    map.put("rute", "1");
                    return ok(ResultView.success(Constant.SUCCESS, "登录成功", map));
                }
                if ("新店辅导班".equals(employee.getPositionName())) {
                    map.put("shopId", employee.getShopId());
                    map.put("employeeId", employee.getId());
                    map.put("employeeCode", employeeCode);
                    map.put("shopName", employee.getShopName());
                    map.put("rute", "3");
                    return ok(ResultView.success(Constant.SUCCESS, "登录成功", map));
                }
        }

        ProShopElite shopElite = eliteService.queryByEmployeeCode(employeeCode);
        if (shopElite == null) {
            return ok(ResultView.fail(Constant.FAIL, "没有登录权限"));
        }
        map.put("shopId", employees.get(0).getShopId());
        map.put("employeeCode", employeeCode);
        map.put("employeeId", employees.get(0).getId());
        map.put("rute", "2");
        return ok(ResultView.success(Constant.SUCCESS, "登录成功", map));
    }

    public Result loginChangeShop() {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        String employeeCode = mapParams.get("employeeCode").toString();
        List<LoginEmployeeVo> employees = employeeService.queryForLogin(employeeCode);
        List<Map<String,Object>> list = new ArrayList<>();
        //判断是否是店经理
        for (LoginEmployeeVo employee : employees) {
                if ("店经理".equals(employee.getPositionName())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("shopId", employee.getShopId());
                    map.put("employeeId", employee.getId());
                    map.put("employeeCode", employeeCode);
                    map.put("shopName", employee.getShopName());
                    list.add(map);
                }
        }
        return ok(ResultView.success(Constant.SUCCESS, "查询成功", list));
    }
}
