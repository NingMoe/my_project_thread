package service;

import com.google.inject.ImplementedBy;
import models.ProEmployee;
import models.vo.LoginEmployeeVo;
import models.vo.ProEmployeeVo;
import service.impl.EmployeeServiceImpl;

import java.util.List;
import java.util.Set;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/14 17:14
 */
@ImplementedBy(EmployeeServiceImpl.class)
public interface EmployeeService {


    ProEmployee queryById(String empoloyeeId);

    /**
     * 根据职员编号查询
     * @param employeeCode
     * @return
     */
    List<ProEmployeeVo> queryByCode(String employeeCode);

    List<ProEmployeeVo> getShopManagerByShopId(String shopId);

    List<ProEmployeeVo> queryListByCodes(List<String> list);

    List<ProEmployeeVo> queryListByShopIds(Set<String> list);
    List<ProEmployeeVo> getCoach();

    /**
     * 查询门店人员总数
     * @param shopId
     * @return
     */
    int selectCountByShopId(String shopId);

    List<LoginEmployeeVo> queryForLogin(String employeeCode);
}
