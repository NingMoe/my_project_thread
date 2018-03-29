package service;

import com.google.inject.ImplementedBy;
import models.ProFileRelation;
import models.ProUploadFile;
import service.impl.UploadFileServiceImpl;

import java.util.List;

/**
 * Created by zhengjie on 2017-03-08.
 */
@ImplementedBy(UploadFileServiceImpl.class)
public interface UploadFileService {

    int addUploadFileBatch(List<ProUploadFile> list);

    public List<ProUploadFile> selectByIds(List<ProFileRelation> list);
}
