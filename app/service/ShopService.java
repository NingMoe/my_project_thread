package service;

import com.google.inject.ImplementedBy;
import models.ProShop;
import service.impl.ShopServiceImpl;

import java.util.List;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/8/3 15:33
 */

@ImplementedBy(ShopServiceImpl.class)
public interface ShopService {

    /**
     * 查询全部门店
     * @return
     */
    List<ProShop> queryShopList();

    List<ProShop> queryShopName();
}
