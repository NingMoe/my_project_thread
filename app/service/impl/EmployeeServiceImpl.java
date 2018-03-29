package service.impl;

import com.google.inject.Inject;
import mapper.ProEmployeeMapper;
import models.ProEmployee;
import models.vo.LoginEmployeeVo;
import models.vo.ProEmployeeVo;
import service.EmployeeService;

import java.util.List;
import java.util.Set;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/14 17:17
 */
public class EmployeeServiceImpl implements EmployeeService {

    @Inject
    private ProEmployeeMapper employeeMapper;

    @Override
    public ProEmployee queryById(String empoloyeeId) {
        return employeeMapper.selectByPrimaryKey(empoloyeeId);
    }

    @Override
    public List<ProEmployeeVo> queryByCode(String employeeCode) {
        return employeeMapper.queryByCode(employeeCode);
    }

    @Override
    public List<ProEmployeeVo> getShopManagerByShopId(String shopId) {
        return employeeMapper.getShopManagerByShopId(shopId);
    }

    @Override
    public List<ProEmployeeVo> queryListByCodes(List<String> employeeCodeList) {
        List<ProEmployeeVo> listByCodes= employeeMapper.queryListByCodes(employeeCodeList);
        return listByCodes;
    }

    @Override
    public List<ProEmployeeVo> queryListByShopIds(Set<String> set) {
        List<ProEmployeeVo> list= employeeMapper.queryListByShopIds(set);
        return list;
    }

    @Override
    public List<ProEmployeeVo> getCoach() {
        List<ProEmployeeVo> list=employeeMapper.getCoach();
        return list;
    }

    @Override
    public int selectCountByShopId(String shopId) {
        return employeeMapper.selectCountByShopId(shopId);
    }

    @Override
    public List<LoginEmployeeVo> queryForLogin(String employeeCode) {
        return employeeMapper.queryForLogin(employeeCode);
    }
}
