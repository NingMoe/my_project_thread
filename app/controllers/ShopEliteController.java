package controllers;

import com.alibaba.fastjson.JSON;
import com.google.inject.Inject;
import com.hht.interceptor.Page;
import com.hht.view.ResultView;
import common.Constant;
import models.ProEliteTrainPool;
import models.ProShopElite;
import org.dom4j.DocumentException;
import play.Logger;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteTrainPoolService;
import service.EmployeeService;
import service.ShopEliteService;
import utils.StringUtils;
import ws.WsCostService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/11 0011.
 */
public class ShopEliteController extends Controller {
    @Inject
    private FormFactory formFactory;

    @Inject
    private ShopEliteService eliteService;

    @Inject
    private WsCostService costService;
    @Inject
    private EmployeeService employeeService;
    @Inject
    private EliteTrainPoolService poolService;

    public Result addPersonToShopElite() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<ProShopElite> eliteList = JSON.parseArray(mapParams.get("list").toString(), ProShopElite.class);
            List<String> list = new ArrayList<>();
            for (ProShopElite shopElite : eliteList) {
                list.add(shopElite.getEmployeeCode());
            }
            List<ProShopElite> ShopElites = eliteService.getElitesByCodeList(list);
            if(ShopElites.size()>0){
                return ok(ResultView.fail(10, "包含重复推荐人员"));
            }
            int insert = eliteService.addPersonToShopElite(mapParams);
            if (insert == 0) {
                return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
            } else if (insert == 2) {
                return ok(ResultView.fail(-2, "金鹰池人数已超出"));
            }
            return ok(ResultView.success(Constant.SUCCESS, "保存成功", insert));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "保存失败"));
        }

    }


    public Result updateShopElitePersonsBycheck() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if(mapParams==null||mapParams.size()==0){
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            String check = StringUtils.objToString(mapParams.get("check"));
            if("200".equals(check)){
                ProShopElite proShopElite = eliteService.selectByPrimaryKey(StringUtils.objToString(mapParams.get("id")));
                int count = employeeService.selectCountByShopId(proShopElite.getShopId());
                //根据预设金鹰池比例计算最大入池数
                ProEliteTrainPool pool = poolService.getProEliteTrainPools();
                double d = (double) count * pool.getElitePer() / 100;
                int belieCount = (int) Math.round(d);
                double b = (double) belieCount * pool.getUpPer() / 100;
                int poolCount = (int) Math.round(b) + belieCount;
                //判断推荐人数是否超出
                int shopPoolCount = eliteService.selectCountByShopId(proShopElite.getShopId());
                //超出
                if (shopPoolCount >= poolCount ) {
                    return ok(ResultView.fail(Constant.OVER_TOP, "人员超出"));
                }

            }
            List<ProShopElite> eliteList = eliteService.updateShopElitePersonsBycheck(mapParams);
            if (eliteList.size() > 0) {
                new Thread() {
                    public void run() {
                        try {
                            costService.sendTestCostResult(eliteList);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Logger.error(e.toString());
                        } catch (DocumentException e) {
                            e.printStackTrace();
                            Logger.error(e.toString());
                        }
                    }
                }.start();
                return ok(ResultView.success(Constant.SUCCESS, "修改成功",null));
            }
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "修改失败"));
        }

    }

    public Result getEmployees() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String, Object>> mapList = eliteService.getEmployees(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }

    public Result getShopElitePersonsByTest() throws Exception {
        Result result;
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String, Object>> mapList = eliteService.getShopElitePersonsByTest(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            result = ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
        return result;
    }

    public Result getShopElitePersonsByNoTest() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String, Object>> mapList = eliteService.getShopElitePersonsByNoTest(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }

    public Result getShopSelfElites() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String, Object>> mapList = eliteService.getShopSelfElites(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }

    public Result deleteShopSelfElites() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            int update = eliteService.deleteShopSelfElites(mapParams);
            if (update == 1) {
                return ok(ResultView.success(Constant.SUCCESS, "删除成功", update));
            }
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "删除失败"));
        }

    }


    public Result getShopSelfInLobbyClass() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String, Object>> mapList = eliteService.getShopSelfInLobbyClass(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }

    public Result getShopSelfPassLobbyClass() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String, Object>> mapList = eliteService.getShopSelfPassLobbyClass(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }

    public Result getShopSelfInLaksaClass() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String, Object>> mapList = eliteService.getShopSelfInLaksaClass(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }


    public Result getShopSelfPassLaksaClass() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String, Object>> mapList = eliteService.getShopSelfPassLaksaClass(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }


    public Result getShopSelfGraduates() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String, Object>> mapList = eliteService.getShopSelfGraduates(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }


    public Result getElitePoolCountByShopId() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            Map<String, Object> map = eliteService.getElitePoolCountByShopId(mapParams);
            if (map != null && map.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", map));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", map));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }

    public Result getShopCertificationCountByShopId() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            Map<String, Object> map = eliteService.getShopCertificationCountByShopId(mapParams);
            if (map != null && map.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", map));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", map));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }

    /**
     * 首页测评列表（前台）
     *
     * @returns
     */
    public Result getTestsByShopId() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null && mapParams.size() == 0) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            List<Map<String, Object>> mapList = eliteService.getTestsByShopId(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }

    /**
     * 首页考试列表（前台）
     *
     * @returns
     */
    public Result getExamsByShopId() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null && mapParams.size() == 0) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            List<Map<String, Object>> mapList = eliteService.getExamsByShopId(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }

    //-------------------------------后台报表汇总-----------------------------------------------------------

    /**
     * 门店金鹰汇总
     *
     * @return
     */

    public Result getShopEliteCountInfo() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null && mapParams.size() == 0) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            Page page = eliteService.getShopEliteCountInfo(mapParams);
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
     * 根据门店Id查询金鹰人员
     *
     * @return
     */
    public Result getShopEliteByShopId() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            Page page = eliteService.getShopEliteByShopId(mapParams);
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
     * 根据员工编号查询学期
     *
     * @return
     */
    public Result getClassForEmployeeCode() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            String employeeCode = mapParams.get("employeeCode").toString();
            List<Map<String,Object>> list = eliteService.getClassForEmployeeCode(employeeCode);
            return ok(ResultView.success(Constant.SUCCESS, "查询成功", list));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }

    /**
     * 门店培训毕业生汇总
     *
     * @return
     */

    public Result getShopGraduateCollect() throws Exception {
        Map<String, Object> params = formFactory.form().bindFromRequest().get().getData();
        if (params == null||params.size()==0) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        if (params.get("pageNum")==null||params.get("pageSize")==null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "分页参数为空"));
        }
        try {
            Page page = eliteService.getShopGraduateCollect(params);
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






