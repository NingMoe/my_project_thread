package service;

import com.google.inject.ImplementedBy;
import models.ProFileRelation;
import service.impl.FileRelationServiceImpl;

import java.util.List;

/**
 * Created by zhengjie on 2017-03-08.
 */
@ImplementedBy(FileRelationServiceImpl.class)
public interface FileRelationService {

    int addFileRelationBatch(List<ProFileRelation> list);

    List<ProFileRelation> selectAll(ProFileRelation proFileRelation);

    int delFileRelationBatch(List<ProFileRelation> list);
}
