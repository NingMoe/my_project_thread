package mapper;

import models.ProShopLobby;

public interface ProShopLobbyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_shop_lobby
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_shop_lobby
     *
     * @mbg.generated
     */
    int insert(ProShopLobby record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_shop_lobby
     *
     * @mbg.generated
     */
    int insertSelective(ProShopLobby record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_shop_lobby
     *
     * @mbg.generated
     */
    ProShopLobby selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_shop_lobby
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ProShopLobby record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_shop_lobby
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ProShopLobby record);

    int updateByEmployeeId(ProShopLobby shopLobby);
}