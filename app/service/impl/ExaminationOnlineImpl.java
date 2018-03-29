package service.impl;

import com.google.inject.Inject;
import mapper.ProEliteExaminationOnlineMapper;
import models.ProEliteExaminationOnline;
import service.ExaminationOnlineService;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/20 18:59
 */
public class ExaminationOnlineImpl implements ExaminationOnlineService {

    @Inject
    private ProEliteExaminationOnlineMapper onlineMapper;

    @Override
    public void insert(ProEliteExaminationOnline examinationOnline) {
        onlineMapper.insert(examinationOnline);
    }
}
