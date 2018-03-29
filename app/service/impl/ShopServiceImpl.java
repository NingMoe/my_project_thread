package service.impl;

import com.google.inject.Inject;
import mapper.ProShopMapper;
import models.ProShop;
import service.ShopService;

import java.util.List;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/8/3 15:33
 */
public class ShopServiceImpl implements ShopService {

    @Inject
    private ProShopMapper shopMapper;

    @Override
    public List<ProShop> queryShopList() {
        return shopMapper.queryShopList();
    }

    @Override
    public List<ProShop> queryShopName() {
        return shopMapper.queryShopName();
    }
}
