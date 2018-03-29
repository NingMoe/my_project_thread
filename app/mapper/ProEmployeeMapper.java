package mapper;

import models.ProEmployee;
import models.vo.LoginEmployeeVo;
import models.vo.ProEmployeeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProEmployeeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_employee
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_employee
     *
     * @mbg.generated
     */
    int insert(ProEmployee record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_employee
     *
     * @mbg.generated
     */
    int insertSelective(ProEmployee record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_employee
     *
     * @mbg.generated
     */
    ProEmployee selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_employee
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ProEmployee record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_employee
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ProEmployee record);

    List<ProEmployee> getEmployeesByMap(Map<String, Object> map);

    List<ProEmployeeVo> queryByCode(String employeeCode);

    List<ProEmployeeVo> getShopManagerByShopId(String shopId);

    Map<String, Object>  getEmployeeByEmployeeId(String id);

    int selectCountByShopId(String shopId);

    List<ProEmployeeVo> queryListByCodes(List<String> list);


    List<ProEmployeeVo> queryListByShopIds(@Param("list") Set<String> list);

    List<String> getEmployeeCodesByShopId(String shopId);

    List<ProEmployeeVo> getCoach();

    List<ProEmployeeVo> queryEmployeeByShopIds(@Param("list") Set<String> list);

    List<ProEmployeeVo> queryEmployeeByEmployees(@Param("list") Set<String> list);

    List<LoginEmployeeVo> queryForLogin(String employeeCode);
}