package controllers;

import com.google.common.io.Files;
import com.google.inject.Inject;
import com.hht.utils.BeanIdCreater;
import com.hht.view.ResultView;
import constants.Constant;
import models.ProUploadFile;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.UploadFileService;
import utils.OSSClientUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 文件上传Controller
 *
 * @version 1.0
 * @since 2017-04-10
 */
public class UploadController extends Controller {
    @Inject
    private UploadFileService uploadFileService;

    @Inject
    private OSSClientUtil clientUtil;


    /**
     * 文件上传(oss)
     *
     * @return
     */
    public Result uploadOSS() throws IOException {

        Map<String,Object> map = new HashMap();

        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart<File>> files = body.getFiles();

        MimeTypes mimeTypes = MimeTypes.getDefaultMimeTypes();
        List<ProUploadFile> uploadFileList=new ArrayList<ProUploadFile>();
        // 判断是否为空
        if(files != null & files.size()>0){

            files.forEach( fileFilePart -> {
                ProUploadFile proUploadFile=new ProUploadFile();
                String key = fileFilePart.getKey();
                String fileExtendName = fileFilePart.getFilename().substring(fileFilePart.getFilename().lastIndexOf("."),fileFilePart.getFilename().length());;
//                try {
//                    fileExtendName = mimeTypes.forName(fileFilePart.getContentType()).getExtension();
//                } catch (MimeTypeException e) {
//                    e.printStackTrace();
//                }
                String originName=fileFilePart.getFilename();
                proUploadFile.setOriginName(originName);
               // String fileName = key + originName.substring(0,fileFilePart.getFilename().lastIndexOf("."))+UUID.randomUUID().toString() + fileExtendName;
                String fileName = UUID.randomUUID().toString() + fileExtendName;
                fileName=clientUtil.uploadFile(fileFilePart.getFile(),fileName,fileFilePart.getContentType());
                proUploadFile.setFileName(fileName);
                String visitUrl=clientUtil.getUrl2(fileName);
                proUploadFile.setFilePath(fileName);
                proUploadFile.setId(BeanIdCreater.getInstance().getBeanIdCreater(proUploadFile.getClass().getName()).nextId());
                long nowTime=System.currentTimeMillis();
                proUploadFile.setCreateTime(nowTime);
                proUploadFile.setModifyTime(nowTime);
                proUploadFile.setSize((int)(fileFilePart.getFile().length()/1024+0.5));
                proUploadFile.setDr(Constant.DR_NO);
                proUploadFile.setFilePath(clientUtil.getFiledir()+fileName);
                proUploadFile.setUrl(visitUrl);
                uploadFileList.add(proUploadFile);
                map.put(key, proUploadFile);
            });
//            uploadFileService.addUploadFileBatch(uploadFileList);
        }

        return ok(ResultView.success(uploadFileList));
    }




}
