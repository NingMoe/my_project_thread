package models;

import java.math.BigDecimal;
import java.util.Date;

public class ProShop {
    private String id;

    private String companyId;

    private String shopName;

    private String shopNickName;

    private String namePinyin;

    private String nickNamePinyin;

    private String shopNo;

    private String manageMethod;

    private String shopBrand;

    private String shopLevel;

    private Date openingDate;

    private String contact;

    private String contactTel;

    private String contactMoble;

    private String shopEmail;

    private String shopFax;

    private String shopZipCode;

    private String shopWeb;

    private String shopOperation;

    private String foodType;

    private String trafficStatusId;

    private String areaLevel;

    private String businessIrcles;

    private String moneyTypeId;

    private String shopNation;

    private String shopArea;

    private String openStart;

    private String openEnd;

    private String reportTime;

    private BigDecimal avgMoney;

    private Integer parkingNum;

    private String orderTel;

    private String complaintTel;

    private String picture1;

    private String picture2;

    private String picture3;

    private String picture4;

    private String picture5;

    private String propaganda;

    private String shopIntroduce;

    private String shopAddress;

    private String legalPerson;

    private String businessLicence;

    private String hygieneLicence;

    private String regAddress;

    private BigDecimal businessArea;

    private Integer employeeNum;

    private Integer version;

    private Integer seatNum;

    private Integer barNum;

    private String timeZone;

    private String latitude;

    private String longitude;

    private String remark;

    private String enable;

    private String creatorId;

    private Long createTime;

    private String modifierId;

    private Long modifyTime;

    private Long ts;

    private String dr;

    private String outName;

    private String outCode;

    private Integer serial;

    private BigDecimal monthRental;

    private String ip;

    private String employeeCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getShopNickName() {
        return shopNickName;
    }

    public void setShopNickName(String shopNickName) {
        this.shopNickName = shopNickName == null ? null : shopNickName.trim();
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin == null ? null : namePinyin.trim();
    }

    public String getNickNamePinyin() {
        return nickNamePinyin;
    }

    public void setNickNamePinyin(String nickNamePinyin) {
        this.nickNamePinyin = nickNamePinyin == null ? null : nickNamePinyin.trim();
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo == null ? null : shopNo.trim();
    }

    public String getManageMethod() {
        return manageMethod;
    }

    public void setManageMethod(String manageMethod) {
        this.manageMethod = manageMethod == null ? null : manageMethod.trim();
    }

    public String getShopBrand() {
        return shopBrand;
    }

    public void setShopBrand(String shopBrand) {
        this.shopBrand = shopBrand == null ? null : shopBrand.trim();
    }

    public String getShopLevel() {
        return shopLevel;
    }

    public void setShopLevel(String shopLevel) {
        this.shopLevel = shopLevel == null ? null : shopLevel.trim();
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel == null ? null : contactTel.trim();
    }

    public String getContactMoble() {
        return contactMoble;
    }

    public void setContactMoble(String contactMoble) {
        this.contactMoble = contactMoble == null ? null : contactMoble.trim();
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail == null ? null : shopEmail.trim();
    }

    public String getShopFax() {
        return shopFax;
    }

    public void setShopFax(String shopFax) {
        this.shopFax = shopFax == null ? null : shopFax.trim();
    }

    public String getShopZipCode() {
        return shopZipCode;
    }

    public void setShopZipCode(String shopZipCode) {
        this.shopZipCode = shopZipCode == null ? null : shopZipCode.trim();
    }

    public String getShopWeb() {
        return shopWeb;
    }

    public void setShopWeb(String shopWeb) {
        this.shopWeb = shopWeb == null ? null : shopWeb.trim();
    }

    public String getShopOperation() {
        return shopOperation;
    }

    public void setShopOperation(String shopOperation) {
        this.shopOperation = shopOperation == null ? null : shopOperation.trim();
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType == null ? null : foodType.trim();
    }

    public String getTrafficStatusId() {
        return trafficStatusId;
    }

    public void setTrafficStatusId(String trafficStatusId) {
        this.trafficStatusId = trafficStatusId == null ? null : trafficStatusId.trim();
    }

    public String getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(String areaLevel) {
        this.areaLevel = areaLevel == null ? null : areaLevel.trim();
    }

    public String getBusinessIrcles() {
        return businessIrcles;
    }

    public void setBusinessIrcles(String businessIrcles) {
        this.businessIrcles = businessIrcles == null ? null : businessIrcles.trim();
    }

    public String getMoneyTypeId() {
        return moneyTypeId;
    }

    public void setMoneyTypeId(String moneyTypeId) {
        this.moneyTypeId = moneyTypeId == null ? null : moneyTypeId.trim();
    }

    public String getShopNation() {
        return shopNation;
    }

    public void setShopNation(String shopNation) {
        this.shopNation = shopNation == null ? null : shopNation.trim();
    }

    public String getShopArea() {
        return shopArea;
    }

    public void setShopArea(String shopArea) {
        this.shopArea = shopArea == null ? null : shopArea.trim();
    }

    public String getOpenStart() {
        return openStart;
    }

    public void setOpenStart(String openStart) {
        this.openStart = openStart == null ? null : openStart.trim();
    }

    public String getOpenEnd() {
        return openEnd;
    }

    public void setOpenEnd(String openEnd) {
        this.openEnd = openEnd == null ? null : openEnd.trim();
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime == null ? null : reportTime.trim();
    }

    public BigDecimal getAvgMoney() {
        return avgMoney;
    }

    public void setAvgMoney(BigDecimal avgMoney) {
        this.avgMoney = avgMoney;
    }

    public Integer getParkingNum() {
        return parkingNum;
    }

    public void setParkingNum(Integer parkingNum) {
        this.parkingNum = parkingNum;
    }

    public String getOrderTel() {
        return orderTel;
    }

    public void setOrderTel(String orderTel) {
        this.orderTel = orderTel == null ? null : orderTel.trim();
    }

    public String getComplaintTel() {
        return complaintTel;
    }

    public void setComplaintTel(String complaintTel) {
        this.complaintTel = complaintTel == null ? null : complaintTel.trim();
    }

    public String getPicture1() {
        return picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1 == null ? null : picture1.trim();
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2 == null ? null : picture2.trim();
    }

    public String getPicture3() {
        return picture3;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3 == null ? null : picture3.trim();
    }

    public String getPicture4() {
        return picture4;
    }

    public void setPicture4(String picture4) {
        this.picture4 = picture4 == null ? null : picture4.trim();
    }

    public String getPicture5() {
        return picture5;
    }

    public void setPicture5(String picture5) {
        this.picture5 = picture5 == null ? null : picture5.trim();
    }

    public String getPropaganda() {
        return propaganda;
    }

    public void setPropaganda(String propaganda) {
        this.propaganda = propaganda == null ? null : propaganda.trim();
    }

    public String getShopIntroduce() {
        return shopIntroduce;
    }

    public void setShopIntroduce(String shopIntroduce) {
        this.shopIntroduce = shopIntroduce == null ? null : shopIntroduce.trim();
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress == null ? null : shopAddress.trim();
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson == null ? null : legalPerson.trim();
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence == null ? null : businessLicence.trim();
    }

    public String getHygieneLicence() {
        return hygieneLicence;
    }

    public void setHygieneLicence(String hygieneLicence) {
        this.hygieneLicence = hygieneLicence == null ? null : hygieneLicence.trim();
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress == null ? null : regAddress.trim();
    }

    public BigDecimal getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(BigDecimal businessArea) {
        this.businessArea = businessArea;
    }

    public Integer getEmployeeNum() {
        return employeeNum;
    }

    public void setEmployeeNum(Integer employeeNum) {
        this.employeeNum = employeeNum;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(Integer seatNum) {
        this.seatNum = seatNum;
    }

    public Integer getBarNum() {
        return barNum;
    }

    public void setBarNum(Integer barNum) {
        this.barNum = barNum;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone == null ? null : timeZone.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable == null ? null : enable.trim();
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

    public String getOutName() {
        return outName;
    }

    public void setOutName(String outName) {
        this.outName = outName == null ? null : outName.trim();
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode == null ? null : outCode.trim();
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getMonthRental() {
        return monthRental;
    }

    public void setMonthRental(BigDecimal monthRental) {
        this.monthRental = monthRental;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }
}