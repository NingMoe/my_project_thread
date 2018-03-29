package vo;

import java.util.Date;

/**
 *
 *  大堂班推荐查询封装数据类
 * Created by Administrator on 2017/7/15 0015.
 */
public class EliteGroomVo {
    //主键Id
    private  String id;
     //店铺名称
    private String shopName;
    //报名月份
    private String groomTime;
    //姓名
    private String employeeName;
    //岗位
    private  String  positionId;
    //职员代码
    private String  employeeCode;
    //性别
    private String gender;

    //电话
    private String mobileNo;

    //生日类型
    private String birthType;
    //生日
    private Date birthday;
    //入职日期
    private Long lastEntryTime;

    //年龄
    private String age;
    //婚姻状况
    private String civilState;
    //学历
    private String  education;
    //参加实习店经理班培训的次数
    private String times;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroomTime() {
        return groomTime;
    }

    public void setGroomTime(String groomTime) {
        this.groomTime = groomTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getBirthType() {
        return birthType;
    }

    public void setBirthType(String birthType) {
        this.birthType = birthType;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Long getLastEntryTime() {
        return lastEntryTime;
    }

    public void setLastEntryTime(Long lastEntryTime) {
        this.lastEntryTime = lastEntryTime;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCivilState() {
        return civilState;
    }

    public void setCivilState(String civilState) {
        this.civilState = civilState;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
