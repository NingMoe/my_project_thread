package mapper;

import models.ProShop;

import java.util.List;

public interface ProShopMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProShop record);

    int insertSelective(ProShop record);

    ProShop selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProShop record);

    int updateByPrimaryKey(ProShop record);

    List<ProShop> queryShopList();

    List<ProShop> queryShopName();
}