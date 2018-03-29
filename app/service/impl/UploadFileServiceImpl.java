package service.impl;

import com.google.inject.Inject;
import mapper.ProUploadFileMapper;
import models.ProFileRelation;
import models.ProUploadFile;
import service.UploadFileService;

import java.util.List;

/**
 * Created by zhengjie on 2017-03-08.
 */
public class UploadFileServiceImpl implements UploadFileService {

    @Inject
    private ProUploadFileMapper proUploadFileMapper;

    /**
     * 批量插入
     *
     * @return
     */
    public int addUploadFileBatch(List<ProUploadFile> list) {
        return proUploadFileMapper.addBatch(list);
    }

    /**
     * 根据id获取
     * @param list
     * @return
     */
    public List<ProUploadFile> selectByIds(List<ProFileRelation> list){
        return proUploadFileMapper.selectByIds(list);
    }
}
