package service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.hht.interceptor.Page;
import mapper.*;
import models.*;
import org.mybatis.guice.transactional.Transactional;
import service.CertificateService;
import service.ShopEliteService;
import service.ShopService;
import utils.BeanToMapUtils;
import utils.StringUtils;
import ws.EvaluationService;
import ws.NoticeService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static utils.StringUtils.objToString;

/**
 * Created by Administrator on 2017/7/18 0018.
 */
public class ShopEliteServiceImpl implements ShopEliteService {
    @Inject
    private ProEmployeeMapper proEmployeeMapper;
    @Inject
    private ProShopEliteMapper shopEliteMapper;
    @Inject
    private ProPositionMapper positionMapper;
    @Inject
    private ProEliteExaminationOnlineMapper examinationOnlineMapper;
    @Inject
    private ProEliteTrainPoolMapper TrainPoolMapper;
    @Inject
    private ProEliteClassPersonMapper classPersonMapper;
    @Inject
    private NoticeService noticeService;
    @Inject
    private EvaluationService evaluationService;
    @Inject
    private ProEmployeeMapper employeeMapper;
    @Inject
    private ProEliteTestRecordMapper recordMapper;
    @Inject
    private CertificateService certificateService;
    @Inject
    private ProEliteNoticeRecordMapper noticeRecord;
    @Inject
    private ProEliteGroomMapper groomMapper;

    @Inject
    private ShopService shopService;


    @Override
    public List<Map<String, Object>> getEmployees(Map<String, Object> mapParams) throws Exception {

        List<Map<String, Object>> mapList = new ArrayList<>();
        List<ProEmployee> employeeList = null;
        if (mapParams != null) {
            //获取本店已经在金鹰池中的人员ID
            String shopId = StringUtils.objToString(mapParams.get("shopId"));
            String employeeId = StringUtils.objToString(mapParams.get("employeeId"));
            List<String> idList = shopEliteMapper.selectEmployeeIdsByShopId(shopId);
            idList.add(employeeId);
            String name = objToString(mapParams.get("employeeName"));
            if (org.apache.commons.lang3.StringUtils.isNotBlank(name)) {
                mapParams.put("employeeName", "%" + name + "%");
            }
            if (idList != null && idList.size() > 0) {
                mapParams.put("id", idList);
            }
            employeeList = proEmployeeMapper.getEmployeesByMap(mapParams);
            if (employeeList != null && employeeList.size() > 0) {
                for (ProEmployee employee : employeeList) {
                    Map<String, Object> map = BeanToMapUtils.fromBeanToMap(employee);
                    //年龄
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    Calendar c1 = Calendar.getInstance();
                    Integer age = null;
                    if (employee.getBirthday() != null) {
                        c1.setTime(employee.getBirthday());
                        age = c.get(Calendar.YEAR) - c1.get(Calendar.YEAR);

                    }
                    map.put("age", age);

                    //工龄
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Integer WorkAge = null;
                    if (employee.getFirstEntryTime() != null) {
                        Date date = format.parse(format.format(employee.getFirstEntryTime()));
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        Calendar cale = Calendar.getInstance();
                        cale.setTime(date);
                        WorkAge = cal.get(Calendar.YEAR) - cale.get(Calendar.YEAR) + 1;
                    }
                    map.put("WorkAge", WorkAge);
                    mapList.add(map);
                }
            } else {
                return mapList;
            }
        }
        return mapList;
    }

    @Override
    @Transactional
    public int addPersonToShopElite(Map<String, Object> mapParams) throws Exception {
        if (mapParams != null) {
            List<ProShopElite> list = JSON.parseArray(mapParams.get("list").toString(), ProShopElite.class);
            //获取门店店员总数
            String shopId = "";
            for (ProShopElite proShopElite : list) {
                shopId = proShopElite.getShopId();
                break;
            }
          /*  ProEmployeePosition position = new ProEmployeePosition();
            position.setShopId(shopId);
            int count = positionMapper.selectCountByShopId(position);*/
            int count = employeeMapper.selectCountByShopId(shopId);


            //根据预设金鹰池比例计算最大入池数
            ProEliteTrainPool pool = TrainPoolMapper.getProEliteTrainPools();
            double d = (double) count * pool.getElitePer() / 100;
            int belielCount = (int) Math.round(d);
            double b = (double) belielCount * pool.getUpPer() / 100;
            int poolCount = (int) Math.round(b) + belielCount;
            //    poolCount = Math.round((count * pool.getElitePer()) / 100);
            //判断推荐人数是否超出
            int shopPoolCount = shopEliteMapper.selectCountByShopId(shopId);
            //超出
            if (shopPoolCount >= poolCount) {
                return 2;
            }
            for (ProShopElite proShopElite : list) {
                proShopElite.setId(StringUtils.getTableId("ProShopElite"));
                proShopElite.setDr("N");
                proShopElite.setElitePoolTime(System.currentTimeMillis());
                proShopElite.setClassStatus("100");
                proShopElite.setEliteStatus("100");
            }
            shopEliteMapper.insertBatch(list);
            List<Map<String, Object>> mapList = new ArrayList<>();

            for (ProShopElite proShopElite : list) {
                //推荐通知
                noticeService.sendNotice(proShopElite.getEmployeeCode(), "1", false, null);
                //通知调用测评
                Map<String, Object> map = employeeMapper.getEmployeeByEmployeeId(proShopElite.getEmployeeId());
                if (map != null) {
                    map.put("CandidateUniqueId", map.get("CandidateUniqueId"));
                    //年龄
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    Calendar c1 = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = sdf.parse(StringUtils.objToString(map.get("Age")));
                        c1.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int age = c.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
                    map.put("Age", age);
                    map.put("firstTime", map.get("firstTime"));
                    map.put("lastTime", map.get("lastTime"));
                    map.put("culture", map.get("Culture"));
                    //性别
                    if ("1".equals(map.get("Gender"))) {
                        map.put("Gender", "男");
                    } else if ("0".equals(map.get("Gender"))) {
                        map.put("Gender", "女");
                    }

                    mapList.add(map);
                }
            }
            evaluationService.sendTestPerson(mapList);
            return 1;
        }
        return 0;
    }

    /**
     * 查询未评测列表
     *
     * @param mapParams
     * @return
     */
    @Override
    public List<Map<String, Object>> getShopElitePersonsByNoTest(Map<String, Object> mapParams) throws Exception {
        List<Map<String, Object>> mapList = null;
        if (mapParams != null) {
            mapList = shopEliteMapper.getShopElitePersonsByNoTest(mapParams);
        }
        return mapList;

    }

    /**
     * 查询已评测列表
     *
     * @param mapParams
     * @return
     */
    @Override
    public List<Map<String, Object>> getShopElitePersonsByTest(Map<String, Object> mapParams) throws Exception {
        List<Map<String, Object>> mapList = null;
        if (mapParams != null) {
            mapList = shopEliteMapper.getShopElitePersonsByTest(mapParams);
        }
        return mapList;
    }

    /**
     * 审核
     *
     * @param mapParams
     * @return
     */
    @Override
    @Transactional
    public List<ProShopElite> updateShopElitePersonsBycheck(Map<String, Object> mapParams) throws Exception {
        List<ProShopElite> list = new ArrayList<>();
        if (mapParams != null) {
            ProShopElite shopElite = new ProShopElite();
            String check = StringUtils.objToString(mapParams.get("check"));
            ProShopElite proShopElite = shopEliteMapper.selectByPrimaryKey(StringUtils.objToString(mapParams.get("id")));
            if (org.apache.commons.lang3.StringUtils.isNotBlank(check)) {
                shopElite.setId(StringUtils.objToString(mapParams.get("id")));
                if ("200".equals(check)) {
                    shopElite.setEliteStatus("200");
                    shopElite.setBecomeTime(System.currentTimeMillis());
                    shopEliteMapper.updateByPrimaryKeySelective(shopElite);
                    //审核进入金鹰池通知
                    if (proShopElite != null) {
                        noticeService.sendNotice(proShopElite.getEmployeeCode(), "3", false, null);
                    }

                } else if ("100".equals(check)) {
                    shopElite.setDr("Y");
                    shopEliteMapper.updateByPrimaryKeySelective(shopElite);
                    //不通过删除学员测评记录
                    recordMapper.updateByEmployeeCode(proShopElite.getEmployeeCode());
                    //不通过删除学员通知记录
                    noticeRecord.updateByEmployeeCode(proShopElite.getEmployeeCode());

                }

            }
            //调用收费接口
            list.add(proShopElite);
        }
        return list;
    }

    /**
     * 本店金鹰
     *
     * @param mapParams
     * @return
     */
    @Override
    public List<Map<String, Object>> getShopSelfElites(Map<String, Object> mapParams) throws Exception {
        List<Map<String, Object>> mapList = null;
        if (mapParams != null) {
            mapParams.put("eliteStatus", 200);
            mapList = shopEliteMapper.selectShopElitePersonsByShopId(mapParams);
            if (mapList != null && mapList.size() > 0) {
                for (Map<String, Object> map : mapList) {
                    List<Map<String, Object>> listByCode = certificateService.
                            queryListByCode(map.get("employeeCode").toString());
                    if (listByCode != null && listByCode.size() > 0) {
                        map.put("certificateCount", listByCode.size());
                    } else {
                        map.put("certificateCount", 0);
                    }
                    map.put("checkTime", map.get("elitePoolTime"));
                    map.put("checkResult", "同意进入金鹰池");

                }
                return mapList;
            }
        }
        return mapList;


    }

    /**
     * 删除金鹰
     *
     * @param mapParams
     * @return
     */
    @Override
    @Transactional
    public int deleteShopSelfElites(Map<String, Object> mapParams) throws Exception {
        if (mapParams != null) {
            List<ProShopElite> list = JSON.parseArray(mapParams.get("list").toString(), ProShopElite.class);
            for (ProShopElite proShopElite : list) {
                proShopElite.setDr("Y");
                shopEliteMapper.updateByPrimaryKeySelective(proShopElite);
                ProEliteClassPerson classPerson = new ProEliteClassPerson();
                //删除组班信息
                classPerson.setEmployeeId(proShopElite.getEmployeeId());
                classPersonMapper.updateClassPersonByEmployeeId(classPerson);
                ProEmployee employee = employeeMapper.selectByPrimaryKey(proShopElite.getEmployeeId());
                if (employee != null) {
                    //删除测评信息
                    recordMapper.updateByEmployeeCode(employee.getEmployeeCode());
                    //删除通知信息
                    noticeRecord.updateByEmployeeCode(employee.getEmployeeCode());
                    //删除考试信息
                    examinationOnlineMapper.deleteByEmployeeCode(employee.getEmployeeCode());
                    //删除报名信息
                    ProEliteGroom groom = new ProEliteGroom();
                    groom.setEmployeeId(proShopElite.getEmployeeId());
                    groomMapper.updateEliteClassGroomByEmployeeId(groom);
                }


            }
            return 1;
        }
        return 0;
    }

    /**
     * 批量更新金鹰测试ID
     *
     * @param maps
     */
    @Override
    @Transactional
    public void batchUpdateForTest(List<Map> maps) {
        shopEliteMapper.batchUpdateForTest(maps);
    }

    /**
     * 查询本店已组大堂班的员工（前台）
     *
     * @param mapParams
     * @returns
     */
    @Override
    public List<Map<String, Object>> getShopSelfInLobbyClass(Map<String, Object> mapParams) throws Exception {
        List<Map<String, Object>> mapList = null;
        if (mapParams != null) {
            mapList = shopEliteMapper.getShopSelfInLobbyClass(mapParams);
        }
        return mapList;
    }

    /**
     * 查询本店大堂班已发证员工（前台）
     *
     * @param mapParams
     * @returns
     */

    @Override
    public List<Map<String, Object>> getShopSelfPassLobbyClass(Map<String, Object> mapParams) throws Exception {
        List<Map<String, Object>> mapList = null;
        if (mapParams != null) {
            mapList = shopEliteMapper.getShopSelfPassLobbyClass(mapParams);
            if (mapList != null && mapList.size() > 0) {
                for (Map<String, Object> map : mapList) {
                    List<Map<String, Object>> certificateList = certificateService.queryListByCode(StringUtils.objToString(map.get("employeeCode")));
                    if (certificateList != null) {
                        map.put("certificateCount", certificateList.size());
                    } else {
                        map.put("certificateCount", 0);
                    }
                }
            }
        }
        return mapList;
    }

    /**
     * 查询本店已组店经理班的员工（前台）
     *
     * @param mapParams
     * @returns
     */

    @Override
    public List<Map<String, Object>> getShopSelfInLaksaClass(Map<String, Object> mapParams) throws Exception {
        List<Map<String, Object>> mapList = null;
        if (mapParams != null) {
            mapList = shopEliteMapper.getShopSelfInLaksaClass(mapParams);
        }
        return mapList;
    }

    /**
     * 查询本店店经理班已发证员工（前台）
     *
     * @param mapParams
     * @returns
     */
    @Override
    public List<Map<String, Object>> getShopSelfPassLaksaClass(Map<String, Object> mapParams) throws Exception {
        List<Map<String, Object>> mapList = null;
        if (mapParams != null) {
            mapList = shopEliteMapper.getShopSelfPassLaksaClass(mapParams);
        }
        return mapList;
    }

    /**
     * 查询本店所有的毕业生
     *
     * @param mapParams
     * @returns
     */
    @Override
    public List<Map<String, Object>> getShopSelfGraduates(Map<String, Object> mapParams) throws Exception {
        List<Map<String, Object>> mapList = null;
        if (mapParams != null) {

            //查询大堂班获证人数
            mapParams.put("classType", "100");
            List<Map<String, Object>> lobbyList = shopEliteMapper.getShopSelfGraduates(mapParams);
            //查询后备店经理获证人数
            mapParams.put("classType", "200");
            mapParams.put("isManager", "Y");
            mapList = shopEliteMapper.getShopSelfGraduates(mapParams);

            mapList.addAll(lobbyList);
        }
        return mapList;
    }

    /**
     * 金鹰池情况
     *
     * @param mapParams
     * @returns
     */
    @Override
    public Map<String, Object> getElitePoolCountByShopId(Map<String, Object> mapParams) throws Exception {
        Map<String, Object> map = Maps.newHashMap();
        if (mapParams != null) {
            String shopId = StringUtils.objToString(mapParams.get("shopId"));
            if (shopId != null) {
                /*ProEmployeePosition position = new ProEmployeePosition();
                position.setShopId(shopId);
                int count = positionMapper.selectCountByShopId(position);*/
                int count = employeeMapper.selectCountByShopId(shopId);
                //根据预设金鹰池比例计算最大入池数
                ProEliteTrainPool pool = TrainPoolMapper.getProEliteTrainPools();
                double d = (double) count * pool.getElitePer() / 100;
                int belielCount = (int) Math.round(d);
                double b = (double) belielCount * pool.getUpPer() / 100;
                int poolCount = (int) Math.round(b) + belielCount;
                //本店金鹰池人数
                int shopPoolCount = shopEliteMapper.selectCountByShopId(shopId);
                //剩余名额
                if (shopPoolCount == 0) {
                    map.put("shopPoolCount", shopPoolCount);
                    map.put("remainCount", poolCount);
                    return map;
                }
                int remainCount = poolCount - shopPoolCount;
                if (remainCount < 0) {
                    map.put("remainCount", 0);
                } else {
                    map.put("remainCount", remainCount);
                }
                map.put("shopPoolCount", shopPoolCount);

            }
        }
        return map;
    }

    /**
     * 本店获证情况
     *
     * @param mapParams
     * @returns
     */
    @Override
    public Map<String, Object> getShopCertificationCountByShopId(Map<String, Object> mapParams) throws Exception {
        Map<String, Object> map = Maps.newHashMap();
        if (mapParams != null) {
            String shopId = StringUtils.objToString(mapParams.get("shopId"));
            //大堂证获得人数
            int lobbyCertificatCount = shopEliteMapper.selectPassLobbyCount(shopId);
            //店经理证获得人数
            ProShopElite Elite = new ProShopElite();
            // Elite.setClassStatus("200");
            Elite.setEliteStatus("400");
            Elite.setShopId(shopId);
            int laksaCertificatCount = shopEliteMapper.selectCertificatInfoByShopId(Elite);
            //获证但未在大堂岗人数
            int notInLobbyCount = shopEliteMapper.selectPassNotInLobbyCountByShopId(shopId);
            //测评人数
            int testCount = shopEliteMapper.selectTestCountByShopId(shopId);
            //考试人数
            int examCount = shopEliteMapper.selectExamCountByShopId(shopId);
            map.put("LobbyCertificatCount", lobbyCertificatCount);
            map.put("LaksaCertificatCount", laksaCertificatCount);
            //毕业生人数(实际就是大堂班毕业的人数)
            map.put("graduateCount", lobbyCertificatCount);
            map.put("notInLobbyCount", notInLobbyCount);
            map.put("testCount", testCount);
            map.put("examCount", examCount);


        }
        return map;
    }

    /**
     * 首页测评列表（前台）
     *
     * @param mapParams
     * @returns
     */
    @Override
    public List<Map<String, Object>> getTestsByShopId(Map<String, Object> mapParams) throws Exception {

        List<Map<String, Object>> mapList = shopEliteMapper.getTestsByShopId(mapParams);
        return mapList;
    }

    /**
     * 首页考试列表（前台）
     *
     * @param mapParams
     * @returns
     */
    @Override
    public List<Map<String, Object>> getExamsByShopId(Map<String, Object> mapParams) throws Exception {
        List<Map<String, Object>> mapList = shopEliteMapper.getExamsByShopId(mapParams);
        return mapList;
    }

    /**
     * 根据职员代码查询
     *
     * @param employeeCode
     * @return
     */
    @Override
    public ProShopElite queryByEmployeeCode(String employeeCode) {
        return shopEliteMapper.queryByEmployeeCode(employeeCode);
    }

    @Override
    @Transactional
    public void batchUpdateForTestEnd(List<ProEliteTestRecord> list) {
        shopEliteMapper.batchUpdateForTestEnd(list);
    }

    /**
     * 查询没有测评返回结果的待测评金鹰人员
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> queryForTest() {
        return shopEliteMapper.queryForTest();
    }

    @Override
    @Transactional
    public void taskForElite() {
        //查询预警时间
        ProEliteTrainPool eliteTrainPools = TrainPoolMapper.getProEliteTrainPools();
        DateFormat df = new SimpleDateFormat("HH:mm");
        String[] split = df.format(new Date()).split(":");
        Integer hour = Integer.valueOf(split[0]);
        Integer min = Integer.valueOf(split[1]);
        if (hour.equals(eliteTrainPools.getWarnTime()) && min <= 4) {
            List<ProShop> list = shopService.queryShopList();
            //TODO 写死
//            List<ProShop> list = new ArrayList<>();
//            ProShop shop = new ProShop();
//            shop.setId("C13352966C000000A60000000016E000");
//            shop.setEmployeeCode("10000565");
//            list.add(shop);

            for (ProShop proShop : list) {
                //计算金鹰池预警人数
                int count = employeeMapper.selectCountByShopId(proShop.getId());
                double d = (double) count * eliteTrainPools.getWarnPer() / 100;
                int belielCount = (int) Math.round(d);
                //查询金鹰池人数
                int shopPoolCount = shopEliteMapper.selectCountByShopId(proShop.getId());
                //金鹰池人数低于预警人数,通知店经理
                if (shopPoolCount < belielCount) {
                    noticeService.sendNotice(proShop.getEmployeeCode(), "21", true, null);
                }
            }

        }
    }


    //--------------------------------------后台报表----------------------------------------------------------------

    /**
     * 门店金鹰汇总
     *
     * @return
     */
    @Override
    public Page getShopEliteCountInfo(Map<String, Object> mapParams) throws Exception {

        Page page = this.page(mapParams);
        page.setParams(mapParams);
        //所有店的金鹰人数
        List<Map<String, Object>> shopEliteList = shopEliteMapper.getShopEliteCountListPage(page);
        if (shopEliteList.size() == 0) {
            return page;
        }

        List<String> shopIdList = new ArrayList<>();
        for (Map<String, Object> map : shopEliteList) {
            shopIdList.add(map.get("shopId").toString());



        }
        //所有店的员工总数
        List<Map<String, Object>> employeeList = shopEliteMapper.getShopEmployeeCountByList(shopIdList);
        for (Map<String, Object> shopElite : shopEliteList) {
            //门店id
            String shopId = shopElite.get("shopId").toString();
            //已有门店金鹰池人数
            int eliteCount = Integer.valueOf(shopElite.get("eliteCount").toString());

            for (Map<String, Object> employee : employeeList) {
                if (shopId.equals(employee.get("shopId").toString())) {
                    //门店总人数
                    int employeeCount = Integer.valueOf(employee.get("employeeCount").toString());
                    //金鹰比例
                    int scale = (int) ((double) (eliteCount * 100) / employeeCount);
                    //允许最大金鹰名额
                    ProEliteTrainPool pool = TrainPoolMapper.getProEliteTrainPools();
                    double d = (double) employeeCount * pool.getElitePer() / 100;
                    int belielCount = (int) Math.round(d);
                    double b = (double) belielCount * pool.getUpPer() / 100;
                    int poolCount = (int) Math.round(b) + belielCount;

                    //剩余名额
                    if (eliteCount == 0) {
                        shopElite.put("eliteCount", eliteCount);
                        shopElite.put("remainCount", poolCount);

                    } else {
                        int remainCount = poolCount - eliteCount;
                        if (remainCount < 0) {
                            shopElite.put("remainCount", 0);
                        } else {
                            shopElite.put("remainCount", remainCount);
                        }
                        shopElite.put("eliteCount", eliteCount);
                    }
                    shopElite.put("poolCount", poolCount);
                    shopElite.put("employeeCount", employeeCount);
                    shopElite.put("scale", scale + "%");
                    break;

                }

            }


        }
        page.setList(shopEliteList);
        return page;
    }

    /**
     * 根据门店Id查询金鹰人员
     *
     * @param mapParams
     * @return
     */
    @Override
    public Page getShopEliteByShopId(Map<String, Object> mapParams) throws Exception {
        Page page = this.page(mapParams);
        page.setParams(mapParams);
        List<Map<String, Object>> mapList = shopEliteMapper.selectEliteEmployeeByListPage(page);
        List<ProShop> proShops = shopService.queryShopList();
        List<ProPosition> proPosition = positionMapper.getPositions();
        if (mapList != null && mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                //岗位
                for (int i = 0; i < proPosition.size(); i++) {
                    if(proPosition.get(i).getId().equals(map.get("positionId").toString())){
                        map.put("positionName", proPosition.get(i).getPositionName());
                        continue;
                    }
                }
                map.put("checkTime", map.get("elitePoolTime"));
                for (ProShop proShop : proShops) {
                    if(proShop.getId().equals(map.get("shopId"))){
                        map.put("shopName",proShop.getShopName());
                    }
                }
            }

        }
        page.setList(mapList);
        return page;
    }

    @Override
    public ProShopElite selectByPrimaryKey(String id) {
        return shopEliteMapper.selectByPrimaryKey(id);
    }

    @Override
    public int selectCountByShopId(String shopId) {
        return shopEliteMapper.selectCountByShopId(shopId);
    }


    /***
     * 分页通用
     *
     * @param mapParams
     * @return
     */
    private Page page(Map<String, Object> mapParams) {
        Page page = new Page();
        //分页参数
        String pageNum = objToString(mapParams.get("pageNum"));
        if (pageNum != null && !"".equals(pageNum)) {
            page.setPageNum(Integer.valueOf(pageNum));
        } else {
            page.setPageNum(1);
        }
        String pageSize = objToString(mapParams.get("pageSize"));
        if (pageSize != null && !"".equals(pageSize)) {
            page.setPageRecordCount(Integer.valueOf(pageSize));
        } else {
            page.setPageRecordCount(10);
        }
        return page;
    }

    @Override
    public List<Map<String, Object>> getClassForEmployeeCode(String employeeCode) {
        return classPersonMapper.getClassForEmployeeCode(employeeCode);
    }

    /**
     * 更新门店金鹰表
     *
     * @param shopElite
     */
    @Override
    public void updateByBean(ProShopElite shopElite) {

        shopEliteMapper.updateByBean(shopElite);
    }

    /**
     * 门店培训毕业生汇总
     *
     * @param params
     * @return
     */
    @Override
    public Page getShopGraduateCollect(Map<String, Object> params) throws Exception {
        String pageSize = params.get("pageSize").toString();
        String pageNum = params.get("pageNum").toString();
        //设置分页参数
        Page page = new Page();
        page.setPageNum(Integer.valueOf(pageNum));
        page.setPageRecordCount(Integer.valueOf(pageSize));
        page.setParams(params);
        //所有店的金鹰人数
        List<Map<String, Object>> shopEliteList = shopEliteMapper.getShopEliteCountByListPage(page);
        if (shopEliteList.size() <= 0) {
            page.setList(new ArrayList());
            return page;
        }
        List<String> shopIdList = new ArrayList<>();
        for (Map<String, Object> map : shopEliteList) {
            shopIdList.add(map.get("shopId").toString());
        }
        //所有店的员工总数
        List<Map<String, Object>> employeeList = shopEliteMapper.getShopEmployeeCountByList(shopIdList);
        //考试通过的人数
        List<Map<String, Object>> examPassList = shopEliteMapper.getExamPassCountByList(shopIdList);
        if (examPassList.size() <= 0) {
            page.setList(new ArrayList());
            return page;
        }
        //大堂班毕业人数
        Map map =  new HashMap();
        map.put("isLobby","Y");
        map.put("list",shopIdList);
        List<Map<String, Object>> lobbyPassList = shopEliteMapper.getClassPassCountByList(map);
        //店经理毕业人数
        map.put("isManager","Y");
        List<Map<String, Object>> managerPassList = shopEliteMapper.getClassPassCountByList(map);
        for (Map<String, Object> shopElite : shopEliteList) {
            shopElite.put("examPassCount", "");
            shopElite.put("passScale", "");
            //门店id
            String shopId = shopElite.get("shopId").toString();
            //已有门店金鹰池人数
            int eliteCount = Integer.valueOf(shopElite.get("eliteCount").toString());
            for (Map<String, Object> employee : employeeList) {
                if (shopId.equals(employee.get("shopId").toString())) {
                    //门店总人数
                    int employeeCount = Integer.valueOf(employee.get("employeeCount").toString());
                    //金鹰比例
                    int eliteScale = (int) ((double) (eliteCount * 100) / employeeCount);
                    //允许最大金鹰名额
                    ProEliteTrainPool pool = TrainPoolMapper.getProEliteTrainPools();
                    double d = (double) employeeCount * pool.getElitePer() / 100;
                    int belieCount = (int) Math.round(d);
                    double b = (double) belieCount * pool.getUpPer() / 100;
                    int poolCount = (int) Math.round(b) + belieCount;
                    shopElite.put("eliteScale", eliteScale + "%");
                    shopElite.put("poolCount", poolCount);
                    break;
                }
            }

            for (Map<String, Object> examPass : examPassList) {
                if (shopId.equals(examPass.get("shopId").toString())) {
                    //通过的人数
                    int  examPassCount = Integer.valueOf(examPass.get("passCount").toString());
                    shopElite.put("examPassCount", examPassCount);
                    //通过占比
                    int passScale = (int) ((double) (examPassCount * 100) / eliteCount);
                    shopElite.put("passScale", passScale + "%");
                    break;
                }
            }
            //大堂通过的人数
            for (Map<String, Object> lobbyPass : lobbyPassList) {
                if (shopId.equals(lobbyPass.get("shopId").toString())) {
                    int  classPassCount = Integer.valueOf(lobbyPass.get("classPassCount").toString());
                    shopElite.put("lobbyPassCount", classPassCount);
                    break;
                }
            }
            //店经理通过的人数
            for (Map<String, Object> managerPass : managerPassList) {
                if (shopId.equals(managerPass.get("shopId").toString())) {
                    int  classPassCount = Integer.valueOf(managerPass.get("classPassCount").toString());
                    shopElite.put("managerPassCount", classPassCount);
                    break;
                }
            }
        }
        page.setList(shopEliteList);
        return page;
    }
    /**
     * 查询已推荐入池的人员
     * @param list
     * @return
     */
    @Override
    public List<ProShopElite> getElitesByCodeList(List<String> list) throws Exception {
        return shopEliteMapper.getElitesByCodeList(list);
    }

}
