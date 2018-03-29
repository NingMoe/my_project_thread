package service.impl;

import com.google.inject.Inject;
import com.hht.utils.BeanIdCreater;
import com.hht.utils.StringUtils;
import constants.Constant;
import mapper.ProFileRelationMapper;
import models.ProFileRelation;
import models.ProUploadFile;
import service.FileRelationService;
import service.UploadFileService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengjie on 2017-03-08.
 */
public class FileRelationServiceImpl implements FileRelationService {

    @Inject
    private ProFileRelationMapper proFileRelationMapper;

    @Inject
    private UploadFileService uploadFileService;

    /**
     * 批量插入
     *
     * @return
     */
    public int addFileRelationBatch(List<ProFileRelation> list) {
        List<ProFileRelation> insertList=new ArrayList<ProFileRelation>();
        List<ProFileRelation> updateList=new ArrayList<ProFileRelation>();
        list.forEach(proFileRelation -> {
            long nowTime=System.currentTimeMillis();
            proFileRelation.setModifyTime(nowTime);
            proFileRelation.setTs(nowTime);
            proFileRelation.setDr(Constant.DR_NO);
            if(StringUtils.isEmpty(proFileRelation.getId())){
                proFileRelation.setId(BeanIdCreater.getInstance().getBeanIdCreater(proFileRelation.getClass().getName()).nextId());
                insertList.add(proFileRelation);
            }else{
                updateList.add(proFileRelation);
            }
        });
        int result=0;
        if(StringUtils.isNotEmpty(updateList)){
            Map param=new HashMap();
            param.put("ts",updateList.get(0).getTs());
            param.put("billId",updateList.get(0).getBillId());
            param.put("deleteList",updateList);
            result+=proFileRelationMapper.deleteBatch(param);
            result+=proFileRelationMapper.updateBatch(updateList);
        }
        if(StringUtils.isNotEmpty(insertList)){
            result+=proFileRelationMapper.addBatch(list);
        }
        return result;
    }
    /**
     * 批量删除
     *
     * @return
     */
    public int delFileRelationBatch(List<ProFileRelation> list) {
        int result=0;
        if(StringUtils.isNotEmpty(list)){
            Map param=new HashMap();
            param.put("ts",list.get(0).getTs());
            param.put("list",list);
            result= proFileRelationMapper.deleteBatchById(param);
        }
        return result;
    }
    public List<ProFileRelation> selectAll(ProFileRelation proFileRelation){
        List<ProFileRelation> proFileRelationList=proFileRelationMapper.selectAll(proFileRelation);
        if(StringUtils.isNotEmpty(proFileRelationList)){
            List<ProUploadFile> proUploadFileList=uploadFileService.selectByIds(proFileRelationList);
            Map<String,ProUploadFile> proUploadFileMap=new HashMap<>();
            proUploadFileList.forEach(proUploadFile -> {proUploadFileMap.put(proUploadFile.getId(),proUploadFile);});
            proFileRelationList.forEach(pro->{
                pro.setFilePath(proUploadFileMap.get(pro.getFileId()).getFilePath());
                pro.setUrl(proUploadFileMap.get(pro.getFileId()).getUrl());
                pro.setOriginName(proUploadFileMap.get(pro.getFileId()).getOriginName());
                pro.setSize(proUploadFileMap.get(pro.getFileId()).getSize());
            });
        }
        return proFileRelationList;
    }

}
