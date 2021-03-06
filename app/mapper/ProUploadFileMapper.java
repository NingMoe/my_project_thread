package mapper;

import models.ProFileRelation;
import models.ProUploadFile;

import java.util.List;

public interface ProUploadFileMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_upload_file
     *
     * @mbggenerated Mon Apr 10 10:04:49 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_upload_file
     *
     * @mbggenerated Mon Apr 10 10:04:49 CST 2017
     */
    int insert(ProUploadFile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_upload_file
     *
     * @mbggenerated Mon Apr 10 10:04:49 CST 2017
     */
    int insertSelective(ProUploadFile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_upload_file
     *
     * @mbggenerated Mon Apr 10 10:04:49 CST 2017
     */
    ProUploadFile selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_upload_file
     *
     * @mbggenerated Mon Apr 10 10:04:49 CST 2017
     */
    int updateByPrimaryKeySelective(ProUploadFile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pro_upload_file
     *
     * @mbggenerated Mon Apr 10 10:04:49 CST 2017
     */
    int updateByPrimaryKey(ProUploadFile record);

    int addBatch(List<ProUploadFile> list);

    List<ProUploadFile> selectByIds(List<ProFileRelation> list);
}