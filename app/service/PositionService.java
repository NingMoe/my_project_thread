package service;

import com.google.inject.ImplementedBy;
import models.ProPosition;
import service.impl.PositionServiceImpl;

import java.util.List;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/15 19:11
 */
@ImplementedBy(PositionServiceImpl.class)
public interface PositionService {

    ProPosition queryById(String id);

    List<ProPosition> Positions();
}
