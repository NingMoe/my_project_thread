package vo;

import models.ProEliteClassPerson;

/**
 * Created by Administrator on 2017/7/22 0022.
 */
public class ProEliteClassPersonVo extends ProEliteClassPerson{
    //主键
    private String id;
    //班级类型
    private String classType;
    //员工id
    private String employeeId;
    //员工code
    private String employeeCode;
    //备注
    private String remark;

    private String result;

    private String teacherName;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
