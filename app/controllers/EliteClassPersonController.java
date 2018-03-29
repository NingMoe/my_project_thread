package controllers;

import com.alibaba.fastjson.JSON;
import com.google.inject.Inject;
import com.hht.interceptor.Page;
import com.hht.view.ResultView;
import common.Constant;
import mapper.ProEliteClassMapper;
import mapper.ProEliteClassPersonMapper;
import mapper.ProShopEliteMapper;
import models.ProEliteClass;
import models.ProEliteClassPerson;
import models.ProEliteClassPersonProject;
import models.ProShopElite;
import org.dom4j.DocumentException;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import service.EliteClassPersonService;
import utils.StringUtils;
import vo.ProEliteClassPersonVo;
import ws.GraduationService;
import ws.WsCostService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/16 0011.
 */
public class EliteClassPersonController extends Controller {
    @Inject
    private FormFactory formFactory;
    @Inject
    private EliteClassPersonService classPersonService;
    @Inject
    private ProShopEliteMapper eliteMapper;

    @Inject
    private ProEliteClassMapper classMapper;
    @Inject
    private WsCostService costService;
    @Inject
    private ProEliteClassPersonMapper personMapper;

    @Inject
    private GraduationService graduationService;



    public Result insertEliteClassPerson() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            //组班校验
            List<ProEliteClassPerson> classPersons = classPersonService.getClassPersons(mapParams);
            //判断员工是是否在同一老师班级
            Map<String,Object> resultMap= classPersonService.getHistroy(mapParams);
            if(resultMap.get("flag").toString().equals("1")){
                return ok(ResultView.success(Constant.FAIL, "组班失败", resultMap));
            }
            //查询员工是否组班记录
            if(classPersons.size()>0){
                return ok(ResultView.fail(Constant.FAIL, "重复组班"));
            }
            int insert = classPersonService.addEliteClassPerson(mapParams);
            if (insert == 1) {
                return ok(ResultView.success(Constant.SUCCESS, "组班成功", insert));
            }
            return ok(ResultView.fail(Constant.FAIL, "组班失败"));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "组班失败"));
        }

    }

    /**
     *
     * @return
     * @throws Exception
     */
    public Result editeEliteClassPerson() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<ProEliteClassPersonVo> list = JSON.parseArray(mapParams.get("list").toString(), ProEliteClassPersonVo.class);
            int  result=classPersonService.batchUpdate(list);
            return ok(ResultView.fail(Constant.SUCCESS, "修改成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "修改失败"));
        }
    }

    public Result deleteEliteClassPerson() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            int delete = classPersonService.deleteEliteClassPerson(mapParams);
            return ok(ResultView.success(Constant.SUCCESS, "删除成功", delete));

        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "删除失败"));
        }
    }

    public Result getEliteClassPersonByPage() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            Page page = classPersonService.getEliteClassPersonByPage(mapParams);
            if (page.getList() != null && page.getList().size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", page));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", page));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }

    public Result updateClassCrtificate() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        //校验
        List<ProEliteClassPersonVo> list = JSON.parseArray(mapParams.get("list").toString(), ProEliteClassPersonVo.class);
        for (ProEliteClassPersonVo ClassPerson : list) {
            if (ClassPerson.getResult() != null) {
                String result = StringUtils.objToString(ClassPerson.getResult());
                if (!"100".equals(result)) {
                    return ok(ResultView.fail(-1, "请勿重复操作！！"));
                }
            }
        }

        try {
            final Map<String, Object> result = classPersonService.updateClassCrtificate(mapParams);

            new Thread() {
                public void run() {
                    classPersonService.sendNotice(result);
                    List<ProShopElite> list = new ArrayList<>();
                    List<ProEliteClassPerson> classPersonList = new ArrayList<>();
                    try {
                        ProEliteClass proEliteClass = classMapper.getTermByEmployeeCode(result);
                        List<ProEliteClassPersonVo> PersonVoList = (List<ProEliteClassPersonVo>) result.get("list");
                        classPersonList = classPersonService.getElitePersionList(PersonVoList);
//                        for (ProEliteClassPersonVo person : PersonVoList) {
//                            ProShopElite shopElite = eliteMapper.selectShopEliteByEmployeeId(person.getEmployeeId());
//                            list.add(shopElite);
//                        }
                        if ("100".equals(result.get("classType"))) {
                            costService.sendCostResult(classPersonList, proEliteClass, Constant.COST_NODE_LOBBY);

                        } else {
                            costService.sendCostResult(classPersonList, proEliteClass, Constant.COST_NODE_MANAGER_PASS);

                        }
                        graduationService.sendGraduationResult(classPersonList, proEliteClass);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
            return ok(ResultView.success(Constant.SUCCESS, "发证成功", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "发证失败"));
        }
    }

    public Result updateClassNotPass() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        if (mapParams.get("result") != null) {
            if (!"100".equals(StringUtils.objToString(mapParams.get("result")))) {
                return ok(ResultView.fail(-1, "请勿重复操作！！"));
            }
        }

        try {
            final Map<String, Object> result = classPersonService.updateClassNotPass(mapParams);
            new Thread() {
                public void run() {

                    classPersonService.sendNotPassNotice(result);
                    List<ProEliteClassPerson> list = new ArrayList<>();
                    String id = result.get("id").toString();
                    ProEliteClassPerson classPerson = personMapper.queryById(id);
                    ProEliteClass proEliteClass = classMapper.selectByPrimaryKey(classPerson.getClassId());
                    list.add(classPerson);
                    try {
                        if ("100".equals(proEliteClass.getClassType())) {
                            costService.sendCostResult(list, proEliteClass, Constant.COST_NODE_LOBBY);

                        } else {
                            costService.sendCostResult(list, proEliteClass, Constant.COST_NODE_MANAGER_NOPASS);

                        }
                        List<ProEliteClassPerson> PersonVoList = new ArrayList<>();
                        PersonVoList.add(classPerson);
                        graduationService.sendGraduationResult(PersonVoList, proEliteClass);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            return ok(ResultView.fail(Constant.SUCCESS, "修改成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "修改失败"));
        }
    }

    public Result getPassNotInLobby() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            List<Map<String, Object>> mapList = classPersonService.getPassNotInLobby(mapParams);
            if (mapList != null && mapList.size() > 0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", mapList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", mapList));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }

    }

//-------------------------------------------报表汇总----------------------------------

    /**
     * 班级学员详情
     * @return
     */
    public Result getClassPersonInfo() throws Exception {
        Map<String, Object> params = formFactory.form().bindFromRequest().get().getData();
        if (params == null||params.size()==0) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        if (params.get("pageNum")==null||params.get("pageSize")==null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "分页参数为空"));
        }
        try {
            Page page = classPersonService.getClassPersonInfo(params);
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
     *
     * 删除人员项目字表数据
     * @return
     * @throws Exception
     */
    public Result deleteEliteClassPersonProjectById() throws Exception {
        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        if (mapParams == null) {
            return ok(ResultView.fail(Constant.PARAM_NULL, "参数为空"));
        }
        try {
            int delete = classPersonService.deleteEliteClassPersonProjectById(mapParams);
            if (delete == 0) {
                return ok(ResultView.fail(Constant.FAIL, "删除失败"));
            }
            return ok(ResultView.success(Constant.SUCCESS, "删除成功", delete));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "删除失败"));
        }

    }


    /**
     * 根据ID
     * 查询 发证项目
     * @return
     * @throws Exception
     */
    public Result getEliteClassPersonProject() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<ProEliteClassPersonProject> projectList = classPersonService.getEliteClassPersonProjectList(mapParams);
            if (projectList != null&&projectList.size()>0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", projectList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }
    /**
     * 根据ID
     * 查询 发证项目
     * @return
     * @throws Exception
     */
    public Result getExportCard() throws Exception {

        Map<String, Object> mapParams = formFactory.form().bindFromRequest().get().getData();
        try {
            List<Map<String,Object>> projectList = classPersonService.getExportCard(mapParams);
            if (projectList != null&&projectList.size()>0) {
                return ok(ResultView.success(Constant.SUCCESS, "查询成功", projectList));
            }
            return ok(ResultView.success(Constant.SUCCESS, "暂无数据", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(ResultView.fail(Constant.FAIL, "查询失败"));
        }
    }

}

