package mapper;

import models.ProEliteTrainCost;

public interface ProEliteTrainCostMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_elite_train_cost
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_elite_train_cost
     *
     * @mbg.generated
     */
    int insert(ProEliteTrainCost record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_elite_train_cost
     *
     * @mbg.generated
     */
    int insertSelective(ProEliteTrainCost record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_elite_train_cost
     *
     * @mbg.generated
     */
    ProEliteTrainCost selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_elite_train_cost
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ProEliteTrainCost record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_elite_train_cost
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ProEliteTrainCost record);

    /**
     * 查询收费设置
     * @return
     */
    ProEliteTrainCost getProEliteTrainCosts();

}