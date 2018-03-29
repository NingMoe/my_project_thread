package service;

import com.google.inject.ImplementedBy;
import models.ProEliteExaminationOnline;
import service.impl.ExaminationOnlineImpl;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/20 18:59
 */

@ImplementedBy(ExaminationOnlineImpl.class)
public interface ExaminationOnlineService {

    void insert(ProEliteExaminationOnline examinationOnline);
}
