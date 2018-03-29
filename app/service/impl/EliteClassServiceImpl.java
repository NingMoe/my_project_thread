package service.impl;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.hht.interceptor.Page;
import mapper.ProEliteClassMapper;
import mapper.ProEliteClassPersonMapper;
import models.ProEliteClass;
import org.mybatis.guice.transactional.Transactional;
import service.EliteClassService;
import utils.BeanToMapUtils;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.BeanToMapUtils.fromBeanToMap;
import static utils.StringUtils.objToString;

/**
 * Created by Administrator on 2017/7/15 0015.
 */
public class EliteClassServiceImpl implements EliteClassService {

    @Inject
    private ProEliteClassMapper classMapper;
    @Inject
    private ProEliteClassPersonMapper classPersonMapper;
    /**
     * 新增学期班级
     * @param mapParams
     * @return
     */
    @Override
    @Transactional
    public ProEliteClass addProEliteClass(Map<String, Object> mapParams) throws Exception {
        ProEliteClass eliteClass = new ProEliteClass();
        eliteClass.setId(StringUtils.getTableId("eliteClass"));
        eliteClass.setTerm(objToString(mapParams.get("term")));
        eliteClass.setTeacherName(objToString(mapParams.get("teacherName")));
        //副讲老师
        eliteClass.setAssistantTeacherName(objToString(mapParams.get("assistantTeacherName")));

        eliteClass.setTrainPlace(objToString(mapParams.get("trainPlace")));
        eliteClass.setReportLocaltion(objToString(mapParams.get("reportLocaltion")));
        eliteClass.setClassType(objToString(mapParams.get("classType")));
        eliteClass.setCreatorId(objToString(mapParams.get("creatorId")));
        eliteClass.setCreateTime(System.currentTimeMillis());
        eliteClass.setDr("N");
        if (mapParams.get("beginTime") != null) {
            eliteClass.setBeginTime(Long.valueOf(objToString(mapParams.get("beginTime"))));
        }
        if (mapParams.get("trainDays") != null) {
            eliteClass.setTrainDays(Integer.valueOf(objToString(mapParams.get("trainDays"))));
        }
        if (mapParams.get("traineeNum") != null) {
            eliteClass.setTraineeNum(Integer.valueOf(objToString(mapParams.get("traineeNum"))));
        }
        if(mapParams.get("reportTime")!=null){
            eliteClass.setReportTime(Long.valueOf(objToString(mapParams.get("reportTime"))));
        }
        if(mapParams.get("relationName")!=null){
            //添加联系人
            eliteClass.setRelationName(objToString(mapParams.get("relationName")));
        }
        if(mapParams.get("mobileNo")!=null){
            //添加手机号
            eliteClass.setMobileNo(objToString(mapParams.get("mobileNo")));;
        }
        if(mapParams.get("qrCodeUrl")!=null){
            //添加 二维码 url
            eliteClass.setQrCodeUrl(objToString(mapParams.get("qrCodeUrl")));
        }



        classMapper.insertSelective(eliteClass);
        return eliteClass;
    }
    /**
     * 修改学期班级
     * @param mapParams
     * @return
     */
    @Override
    @Transactional
    public int updateProEliteClass(Map<String, Object> mapParams) throws Exception {
        ProEliteClass eliteClass = new ProEliteClass();
        eliteClass.setId(objToString(mapParams.get("id")));
        eliteClass.setTerm(objToString(mapParams.get("term")));
        eliteClass.setTeacherName(objToString(mapParams.get("teacherName")));
        eliteClass.setAssistantTeacherName(objToString(mapParams.get("assistantTeacherName")));
        eliteClass.setTrainPlace(objToString(mapParams.get("trainPlace")));
        eliteClass.setReportLocaltion(objToString(mapParams.get("reportLocaltion")));
        eliteClass.setCreatorId(objToString(mapParams.get("creatorId")));
        //  eliteClass.setCreateTime(System.currentTimeMillis());
        eliteClass.setModifyTime(System.currentTimeMillis());

        //修改联系人
        eliteClass.setRelationName(objToString(mapParams.get("relationName")));
        //修改手机号
        eliteClass.setMobileNo(objToString(mapParams.get("mobileNo")));
        //修改二维码 url
        eliteClass.setQrCodeUrl(objToString(mapParams.get("qrCodeUrl")));

        eliteClass.setModifierId(objToString(mapParams.get("modifierId")));
        if (mapParams.get("beginTime") != null) {
            eliteClass.setBeginTime(Long.valueOf(objToString(mapParams.get("beginTime"))));
        }
        if (mapParams.get("trainDays") != null) {
            eliteClass.setTrainDays(Integer.valueOf(objToString(mapParams.get("trainDays"))));
        }
        if (mapParams.get("traineeNum") != null) {
            eliteClass.setTraineeNum(Integer.valueOf(objToString(mapParams.get("traineeNum"))));
        }
        if(mapParams.get("reportTime")!=null){
            eliteClass.setReportTime(Long.valueOf(objToString(mapParams.get("reportTime"))));
        }
        int update = classMapper.updateByPrimaryKeySelective(eliteClass);
        return update;
    }

    /**
     * 查询学期班级(分页)
     *
     * @param mapParams
     * @return
     */
    @Override
    public Page getProEliteClasssByPage(Map<String, Object> mapParams) throws Exception {
        Page page = new Page();
        Map<String, Object> map = Maps.newHashMap();

        if (org.apache.commons.lang3.StringUtils.isNotBlank(objToString(mapParams.get("pageNum")))) {
            page.setPageNum(Integer.valueOf(objToString(mapParams.get("pageNum"))));
        } else {
            page.setPageNum(1);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(objToString(mapParams.get("pageSize")))) {
            page.setPageRecordCount(Integer.valueOf(objToString(mapParams.get("pageSize"))));
        } else {
            page.setPageRecordCount(10);
        }
        String term = objToString(mapParams.get("term"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(term)) {
            map.put("term", term);
        }
        String teacherName = objToString(mapParams.get("teacherName"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(teacherName)) {
            map.put("teacherName", teacherName);
        }
        String classType = objToString(mapParams.get("classType"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(classType)) {
            map.put("classType", classType);
        }
        page.setParams(map);
        List<ProEliteClass> classList = classMapper.getProEliteClasssByListPage(page);
        List<Map<String, Object>> listMap = new ArrayList<>();
        if (classList != null && classList.size() > 0) {
            for (ProEliteClass eliteClass : classList) {
                Map<String, Object> beanMap = fromBeanToMap(eliteClass);
                Map<String, Object> maps = Maps.newHashMap();
                maps.put("classId", eliteClass.getId());
                int personCount = classPersonMapper.selectCountByMap(maps);
                Map<String, Object> hashMap = Maps.newHashMap();
                hashMap.put("result", 200);
                hashMap.put("classId", eliteClass.getId());
                int failCount = classPersonMapper.selectCountByMap(hashMap);
                hashMap.put("result", 300);
                int passCount = classPersonMapper.selectCountByMap(hashMap);
                if (personCount > 0) {
                    int count = eliteClass.getTraineeNum() - personCount;
                    if (count < 0) {
                        count = 0;
                    }
                    beanMap.put("blankCount", count);
                } else {
                    beanMap.put("blankCount", eliteClass.getTraineeNum());
                }
                beanMap.put("failCount", failCount);
                beanMap.put("passCount", passCount);
                listMap.add(beanMap);
            }
        }
        page.setList(listMap);
        return page;
    }
    /**
     * 根据学期查询班级
     * @param mapParams
     * @return
     */
    @Override
    public Map<String, Object> getProEliteClassByTerm(Map<String, Object> mapParams) throws Exception {
        Map<String, Object> map = null;
        if (mapParams != null) {
            ProEliteClass eliteClass = classMapper.selectByMap(mapParams);
            map = BeanToMapUtils.fromBeanToMap(eliteClass);
            Map<String, Object> maps = Maps.newHashMap();
            if (eliteClass != null) {
                maps.put("classId", eliteClass.getId());
            }
            int referrerCount = classPersonMapper.selectCountByMap(maps);
            map.put("referrerCount", referrerCount);
        }
        return map;
    }

    /**
     * 班级毕业情况（大堂班，店经理班）
     * @return
     */
    @Override
    public Page getClassGraduatesCollect(Map<String, Object> params) throws Exception {
        Page page = this.page(params);
        page.setParams(params);
        List<ProEliteClass> classList = classMapper.getClassByListPage(page);
        List<Map<String, Object>> listMap = new ArrayList<>();
        if (classList.size() > 0) {
            for (ProEliteClass eliteClass : classList) {
                Map<String, Object> beanMap = fromBeanToMap(eliteClass);
                Map<String, Object> maps = Maps.newHashMap();
                maps.put("classId", eliteClass.getId());
                int personCount = classPersonMapper.selectCountByMap(maps);
                if (personCount > 0) {
                    maps.put("result", 300);
                    int passCount = classPersonMapper.selectCountByMap(maps);
                    maps.put("result", 200);
                    int NotPassCount = classPersonMapper.selectCountByMap(maps);
                    int passScale = (int)((double)(passCount * 100)/ personCount);
                    beanMap.put("NotPassCount", NotPassCount);
                    beanMap.put("personCount", personCount);
                    beanMap.put("passCount", passCount);
                    beanMap.put("passScale", passScale + "%");
                } else {
                    beanMap.put("NotPassCount", 0);
                    beanMap.put("personCount", 0);
                    beanMap.put("passCount", 0);
                    beanMap.put("passScale", 0 + "%");
                }

                listMap.add(beanMap);
            }
        }
        page.setList(listMap);
        return page;
    }
    /***
     * 分页通用
     * @param mapParams
     * @return
     */
    private Page page(Map<String, Object> mapParams) {
        Page<Map<String, Object>, Map<String, Object>> page = new Page();
        //分页参数
        String pageNum = objToString(mapParams.get("pageNum"));
        String pageSize = objToString(mapParams.get("pageSize"));
        page.setPageNum(Integer.valueOf(pageNum));
        page.setPageRecordCount(Integer.valueOf(pageSize));
        return page;
    }

}
