package models;

public class ProEliteGroom {
    private String id;

    private String employeeId;

    private String employeeCode;

    private String shopId;

    private String classType;

    private Long groomTime;

    private String creatorId;

    private Long createTime;

    private String modifierId;

    private Long modifyTime;

    private Long ts;

    private String dr;

    private String isRoom;

    private String remark;

    private String status;

    private String auditorId;

    private Long auditingTime;

    private String projectName;

    private String lobbyCertificateNum;

    private String isConformJob;

    private String isSatisfyShopLevel;

    private String reason;

    private String isSatisfied;

    private String lobbyType;

    private String lobbyRemark;

    public String getLobbyType() {
        return lobbyType;
    }

    public void setLobbyType(String lobbyType) {
        this.lobbyType = lobbyType;
    }

    public String getLobbyRemark() {
        return lobbyRemark;
    }

    public void setLobbyRemark(String lobbyRemark) {
        this.lobbyRemark = lobbyRemark;
    }

    public String getIsSatisfied() {
        return isSatisfied;
    }

    public void setIsSatisfied(String isSatisfied) {
        this.isSatisfied = isSatisfied;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId == null ? null : employeeId.trim();
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode == null ? null : employeeCode.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType == null ? null : classType.trim();
    }

    public Long getGroomTime() {
        return groomTime;
    }

    public void setGroomTime(Long groomTime) {
        this.groomTime = groomTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId == null ? null : creatorId.trim();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId == null ? null : modifierId.trim();
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr == null ? null : dr.trim();
    }

    public String getIsRoom() {
        return isRoom;
    }

    public void setIsRoom(String isRoom) {
        this.isRoom = isRoom == null ? null : isRoom.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId == null ? null : auditorId.trim();
    }

    public Long getAuditingTime() {
        return auditingTime;
    }

    public void setAuditingTime(Long auditingTime) {
        this.auditingTime = auditingTime;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getLobbyCertificateNum() {
        return lobbyCertificateNum;
    }

    public void setLobbyCertificateNum(String lobbyCertificateNum) {
        this.lobbyCertificateNum = lobbyCertificateNum == null ? null : lobbyCertificateNum.trim();
    }

    public String getIsConformJob() {
        return isConformJob;
    }

    public void setIsConformJob(String isConformJob) {
        this.isConformJob = isConformJob == null ? null : isConformJob.trim();
    }

    public String getIsSatisfyShopLevel() {
        return isSatisfyShopLevel;
    }

    public void setIsSatisfyShopLevel(String isSatisfyShopLevel) {
        this.isSatisfyShopLevel = isSatisfyShopLevel == null ? null : isSatisfyShopLevel.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }
}