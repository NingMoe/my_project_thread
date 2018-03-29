package utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.hht.exception.HhtRuntimeException;
import com.hht.utils.PropertiesUtil;
import constants.Constant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import play.mvc.Http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * 阿里云 OSS文件类
 *
 */
@Repository
public class OSSClientUtil {

    Log log = LogFactory.getLog(OSSClientUtil.class);
    private String endpoint = PropertiesUtil.getValueByKey("endpoint", "oss");
    // accessKey
    private String accessKeyId = PropertiesUtil.getValueByKey("accessKeyId", "oss");
    private String accessKeySecret = PropertiesUtil.getValueByKey("accessKeySecret", "oss");
    //空间
    private String bucketName = PropertiesUtil.getValueByKey("bucketName", "oss");
    //文件存储目录
    private String filedir = PropertiesUtil.getValueByKey("filedir", "oss");

    private OSSClient ossClient;
    MimeTypes mimeTypes;


    public OSSClientUtil() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        mimeTypes = MimeTypes.getDefaultMimeTypes();
    }

    /**
     * 初始化
     */
    public void init() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }


    public String uploadFile(File file, String name, String contentType) {
        try {
            InputStream inputStream = new FileInputStream(file);
            this.uploadFileOss(inputStream, name, contentType);
            return name;
        } catch (Exception e) {
            throw new HhtRuntimeException(Constant.RESULT_FAIL, "文件上传失败");
        }
    }
    public String uploadFile(Http.MultipartFormData.FilePart<File> fileFilePart) {
        try {
            String fileExtendName =fileFilePart.getFilename().substring(fileFilePart.getFilename().lastIndexOf("."),fileFilePart.getFilename().length());
//            try {
//                fileExtendName = mimeTypes.forName(fileFilePart.getContentType()).getExtension();
//
//            } catch (MimeTypeException e) {
//                e.printStackTrace();
//            }
            String key = fileFilePart.getKey();
            String originName=fileFilePart.getFilename();
            String fileName = key + originName.substring(0,fileFilePart.getFilename().lastIndexOf("."))+ UUID.randomUUID().toString()+fileExtendName;
            InputStream inputStream = new FileInputStream(fileFilePart.getFile());
            this.uploadFileOss(inputStream, fileName, fileFilePart.getContentType());
            return fileName;
        } catch (Exception e) {
            throw new HhtRuntimeException(Constant.RESULT_FAIL, "文件上传失败");
        }
    }
    /**
     * 获得图片路径
     *
     * @param fileUrl
     * @return
     */
    public String getImgUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return this.getUrl(this.filedir + split[split.length - 1]);
        }
        return null;
    }

    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFileOss(InputStream instream, String fileName, String contentType) {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(contentType);
            //objectMetadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            PutObjectResult putResult = ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }
    public String getUrl2(String key) {
        String url="http://"+bucketName+"."+endpoint+"/"+filedir+key;
        return url;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getFiledir() {
        return filedir;
    }

    public void setFiledir(String filedir) {
        this.filedir = filedir;
    }
}