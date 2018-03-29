package service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.hht.interceptor.Page;
import mapper.ProEliteGroomMapper;
import mapper.ProEmployeeMapper;
import mapper.ProShopEliteMapper;
import mapper.ProShopMapper;
import models.ProEliteGroom;
import models.ProShop;
import models.ProShopElite;
import org.mybatis.guice.transactional.Transactional;
import play.Configuration;
import service.EliteClassPersonService;
import service.EliteGroomService;
import service.ShopEliteService;
import utils.JdkHttpUtil;
import utils.StringUtils;
import ws.NoticeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static utils.StringUtils.objToString;

/**
 * Created by Administrator on 2017/7/14 0014.
 */
public class EliteTrainGroomServiceImpl implements EliteGroomService {
    @Inject
    private ProEliteGroomMapper groomMapper;
    @Inject
    private ProShopEliteMapper eliteMapper;
    @Inject
    private ProEmployeeMapper employeeMapper;
    @Inject
    private ProShopMapper shopMapper;
    @Inject
    private NoticeService noticeService;
    @Inject
    private EliteClassPersonService personService;
    @Inject
    private ShopEliteService shopEliteService;

    /**
     * 查询所有报名大堂班的员工（后台）
     *
     * @param mapParams
     * @returns
     */
    @Override
    public Page getProEliteTrainGrooms(Map<String, Object> mapParams) throws Exception {
        //将时间类型转成long
        Page page = new Page();
        Map<String, Object> map = Maps.newHashMap();

        String shopName = objToString(mapParams.get("shopName"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(shopName)) {
            map.put("shopName", "%" + shopName + "%");
        }
        String groomTime = objToString(mapParams.get("groomTime"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(groomTime)) {
            map.put("groomTime", "%-" + groomTime + "-%");
        }
        String employeeName = objToString(mapParams.get("employeeName"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(employeeName)) {
            map.put("employeeName", "%" + employeeName + "%");
        }

        String lobbyType = objToString(mapParams.get("lobbyType"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(lobbyType)) {
            map.put("lobbyType",lobbyType);
        }


        String positionId = objToString(mapParams.get("positionId"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(positionId)) {
            map.put("positionId", positionId);
        }

        String employeeCode = objToString(mapParams.get("employeeCode"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(employeeCode)) {
            employeeCode=employeeCode.replace("，",",");
            String[]  strs=employeeCode.split(",");
            List<String> codeList = new ArrayList<>();
            for (int i = 0; i < strs.length; i++) {
                codeList.add(strs[i]);
            }
            map.put("list",codeList);
        }
        //查询起止时间
        String stTime = objToString(mapParams.get("stTime"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(stTime)) {
          map.put("stTime", Long.valueOf(stTime));
        }
        String endTime = objToString(mapParams.get("endTime"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(endTime)) {
            map.put("endTime", Long.valueOf(endTime));
        }


        //分页参数
        String pageNum = objToString(mapParams.get("pageNum"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(pageNum)) {
            page.setPageNum(Integer.valueOf(pageNum));
        } else {
            page.setPageNum(1);
        }
        String pageSize = objToString(mapParams.get("pageSize"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(pageSize)) {
            page.setPageRecordCount(Integer.valueOf(pageSize));
        } else {
            page.setPageRecordCount(10);
        }
        List<Map<String, Object>> grooms;
        String classType = objToString(mapParams.get("classType"));
        map.put("classType", classType);
        String status = objToString(mapParams.get("status"));
        if ("200".equals(classType)) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(status)) {
                if ("400".equals(status)) {
                    page.setParams(map);
                    grooms = groomMapper.getGroomEmployeesByListPage(page);
                } else {
                    map.put("status", status);
                    page.setParams(map);
                    grooms = groomMapper.getEliteGroomsByListPage(page);
                }

            } else {
                map.put("status", "200");
                page.setParams(map);
                grooms = groomMapper.getEliteGroomsByListPage(page);
            }

            grooms = this.getEmployeeInfo(grooms);
            page.setList(grooms);
            return page;
        } else {
            page.setParams(map);
            grooms = groomMapper.getEliteGroomsByListPage(page);
            grooms = this.getEmployeeInfo(grooms);
            page.setList(grooms);
        }

        return page;
    }

    private List<Map<String, Object>> getEmployeeInfo(List<Map<String, Object>> grooms) throws Exception {

        if (grooms != null && grooms.size() > 0) {
           List<Map<String,Object>> objectMap= personService.getEmployeesAllTimesByType(grooms.get(0));
            for (Map<String, Object> groom : grooms) {
                //性别
                if ("0".equals(groom.get("gender"))) {
                    groom.put("gender", "女");

                } else if ("1".equals(groom.get("gender"))) {
                    groom.put("gender", "男");
                }
                //年龄
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                Calendar c1 = Calendar.getInstance();
                Integer age = null;
                //   String birthday="";
                if (groom.get("birthday") != null && !"".equals(groom.get("birthday"))) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = format.parse(groom.get("birthday").toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c1.setTime(date);
                    age = c.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
                    //    birthday = date.toString();
                }
                String date = "";
                if (groom.get("groomTime") != null && !"".equals(groom.get("groomTime"))) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Long time = new Long(groom.get("groomTime").toString());
                        String d = format.format(time);
                        date = format.format(format.parse(d)).toString();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                String lastEntryTime = "";
                if (groom.get("lastEntryTime") != null) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Long time = new Long(groom.get("lastEntryTime").toString());
                        String d = format.format(time);
                        lastEntryTime = format.format(format.parse(d)).toString();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
//                ProEliteGroom eliteGroom = new ProEliteGroom();
//                eliteGroom.setEmployeeId(groom.get("employeeId").toString());
//                eliteGroom.setClassType(groom.get("classType").toString());
//                int count = groomMapper.selectToClassCoByEmPloyeeId(eliteGroom);

                //婚姻状况
                if ("1".equals(objToString(groom.get("isMarry")))) {
                    groom.put("isMarry", "未婚");
                } else if ("2".equals(objToString(groom.get("isMarry")))) {
                    groom.put("isMarry", "已婚");
                } else if ("3".equals(objToString(groom.get("isMarry")))) {
                    groom.put("isMarry", "离异");
                } else {
                    groom.put("isMarry", "未知");
                }
//                int count = personService.getEmployeesClassTimesByType(groom);
                int count=0;
                for (Map<String, Object> stringObjectMap : objectMap) {
                    if(StringUtils.objToString(stringObjectMap.get("employeeCode")).equals(StringUtils.objToString(groom.get("employeeCode")))){
                        count=Integer.parseInt(StringUtils.objToString(stringObjectMap.get("count")));
                        break;
                    }
                }
                groom.put("classTimes", count);
                groom.put("lastEntryTime", lastEntryTime);
                groom.put("groomTime", date);
                groom.put("age", age);
                groom.put("birthday", groom.get("birthday")==null?"":groom.get("birthday").toString());
            }
        }
        return grooms;
    }


    /**
     * 推荐员工报班（批插入）
     *
     * @param mapParams
     * @return
     */
    @Override
    @Transactional
    public int addGroomEmployeesToClass(Map<String, Object> mapParams) throws Exception {

        String classType = StringUtils.objToString(mapParams.get("classType"));
        //推荐上店经理班
        if ("200".equals(classType)) {
            ProEliteGroom groom = new ProEliteGroom();
            groom.setId(StringUtils.getTableId("ProEliteGroom"));
            String isRoom = StringUtils.objToString(mapParams.get("isRoom"));
            groom.setCreatorId(StringUtils.objToString(mapParams.get("createId")));
            groom.setEmployeeId(StringUtils.objToString(mapParams.get("employeeId")));
            groom.setProjectName(StringUtils.objToString(mapParams.get("projectName")));
            groom.setIsRoom(isRoom);
            groom.setRemark(StringUtils.objToString(mapParams.get("remark")));
            groom.setClassType("200");
            String groomTime = StringUtils.objToString(mapParams.get("groomTime"));
            if (org.apache.commons.lang3.StringUtils.isNotBlank(groomTime)) {
                groom.setGroomTime(Long.valueOf(groomTime));
            }
            String shopId = StringUtils.objToString(mapParams.get("shopId"));
            groom.setShopId(shopId);
            groom.setEmployeeCode(StringUtils.objToString(mapParams.get("employeeCode")));
            groom.setGroomTime(Long.parseLong(mapParams.get("groomTime").toString()));
            groom.setCreateTime(System.currentTimeMillis());
            groom.setDr("N");
            if ("Y".equals(isRoom)) {
                //获得大堂经理认证的人数

                List<String> codeList = employeeMapper.getEmployeeCodesByShopId(shopId);
                Map<String, Object> param = new HashMap<>();
                param.put("list", codeList);

                String url = Configuration.root().getString("certificate.url") + "/getTranTblcertificate";
                String result = JdkHttpUtil.post(url, JSON.toJSONString(param));
                JSONObject jSONObject = JSONObject.parseObject(result);
                String lobbyCertificateNum = objToString(jSONObject.get("obj"));
                //所有员工是否符合三个岗位
                String url1 = Configuration.root().getString("certificate.url") + "/getExamCert";
                String result1 = JdkHttpUtil.post(url1, JSON.toJSONString(param));
                JSONObject jSONObject1 = JSONObject.parseObject(result1);
                String isConformJob = objToString(jSONObject1.get("obj"));
                //是否满足门店级别
                ProShop shop = shopMapper.selectByPrimaryKey(shopId);
                String outCode = "";
                if (shop != null) {
                    outCode = shop.getOutCode();
                }
                String url2 = Configuration.root().getString("certificate.url") + "/getSelectCsPlvlTb";
                String result2 = JdkHttpUtil.get(url2, "shopNo=" + outCode);
                JSONObject jSONObject2 = JSONObject.parseObject(result2);
                List<Map> isSatisfyShopLevel = (List<Map>) jSONObject2.get("obj");
                groom.setLobbyCertificateNum(lobbyCertificateNum);
                if ("1".equals(isConformJob)) {
                    groom.setIsConformJob("Y");
                } else {
                    groom.setIsConformJob("N");
                }
                if (isSatisfyShopLevel.size() > 0) {
                    groom.setIsSatisfyShopLevel("Y");
                } else {
                    groom.setIsSatisfyShopLevel("N");
                }
                //审核状态为未审核
                groom.setStatus("300");
                groomMapper.insert(groom);
                //给新店审核发通知
                noticeService.sendCoachNotice(objToString(mapParams.get("employeeCode")), "24", null);

            } else if ("N".equals(isRoom)) {
                //审核状态为审核通过
                groom.setStatus("200");
                groom.setReason(StringUtils.objToString(mapParams.get("reason")));
                groomMapper.insert(groom);
            }
            //更新报班状态为已推荐
            ProShopElite shopElite = new ProShopElite();
            shopElite.setEmployeeId(groom.getEmployeeId());
            shopElite.setClassStatus("300");
            eliteMapper.updateByBean(shopElite);

        } else {
            //推荐上大堂班
            List<ProEliteGroom> list = JSON.parseArray(mapParams.get("list").toString(), ProEliteGroom.class);
            if (list != null && list.size() > 0) {
                for (ProEliteGroom groom : list) {
                    groom.setId(StringUtils.getTableId("ProEliteGroom"));
//                    groom.setGroomTime(list.get());
                    groom.setCreateTime(System.currentTimeMillis());
                    groom.setDr("N");
                    groomMapper.insert(groom);
                    //更新报班状态为已推荐
                    ProShopElite shopElite = new ProShopElite();
                    shopElite.setEmployeeId(groom.getEmployeeId());
                    shopElite.setClassStatus("300");
                    eliteMapper.updateByBean(shopElite);
                }

            }
        }
        return 1;

    }

    /**
     * 查询本店报名大堂班的员工（前台）
     *
     * @param mapParams
     * @returns
     */
    @Override
    public List<Map<String, Object>> getShopSelfGroomToLobby(Map<String, Object> mapParams) throws Exception {
        List<Map<String, Object>> mapList = null;
        if (mapParams != null) {
            mapList = groomMapper.getShopSelfGroomToLobby(mapParams);
        }
        return mapList;
    }

    /**
     * 查询本店报名店经理班的员工（前台）
     *
     * @param mapParams
     * @returns
     */
    @Override
    public List<Map<String, Object>> getShopSelfGroomToLaksaClass(Map<String, Object> mapParams) throws Exception {
        List<Map<String, Object>> mapList = null;
        if (mapParams != null) {
            mapList = groomMapper.getShopSelfGroomToLaksaClass(mapParams);
        }
        return mapList;
    }

    /**
     * 查询员工是否已经推荐
     *
     * @param mapParams
     * @return
     */
    @Override
    public List<ProEliteGroom> getGroomEmployees(Map<String, Object> mapParams) {
        return groomMapper.getGroomEmployees(mapParams);
    }

    /**
     * 删除报名表员工
     *
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public int deleteGroomEmployee(Map<String, Object> params) throws Exception {
        //删除报名表数据
        ProEliteGroom groom = new ProEliteGroom();
        groom.setId(params.get("groomId").toString());
        groom.setDr("Y");
        groomMapper.updateByPrimaryKeySelective(groom);
        //更新门店金鹰表
        String employeeId = objToString(params.get("employeeId"));
        ProShopElite shopElite = new ProShopElite();
        shopElite.setEmployeeId(employeeId);
        shopElite.setClassStatus("100");
        shopEliteService.updateByBean(shopElite);
        return 0;
    }
}
