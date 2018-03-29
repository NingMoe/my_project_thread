package service.impl;

import com.google.inject.Inject;
import mapper.ProPositionMapper;
import models.ProPosition;
import service.PositionService;

import java.util.List;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/15 19:12
 */
public class PositionServiceImpl implements PositionService {

    @Inject
    private ProPositionMapper positionMapper;

    @Override
    public ProPosition queryById(String id) {
        return positionMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ProPosition> Positions() {
        return positionMapper.getPositions();
    }
}
