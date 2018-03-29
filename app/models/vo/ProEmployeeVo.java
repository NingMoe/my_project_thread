package models.vo;

import models.ProEmployee;
import models.ProPosition;

import java.util.List;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/25 19:05
 */
public class ProEmployeeVo extends ProEmployee {

    private List<ProPosition> positionNames;

    private String shopId;

    private String shopName;

    public List<ProPosition> getPositionNames() {
        return positionNames;
    }

    public void setPositionNames(List<ProPosition> positionNames) {
        this.positionNames = positionNames;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
