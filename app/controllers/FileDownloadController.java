package controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.inject.Inject;
import com.hht.view.ResultView;
import common.Constant;
import org.slf4j.LoggerFactory;
import play.Configuration;
import play.data.FormFactory;
import play.mvc.Result;
import utils.PoiExcelUtil;
import utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static play.mvc.Results.ok;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/8/3 20:03
 */
public class FileDownloadController {

    org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


    @Inject
    private FormFactory formFactory;

    @Inject
    private Configuration configuration;

    /**
     * 导出订单Excel表格
     *
     * @return
     */
    public Result getOrderListExecl() {
        Map<String, Object> map = formFactory.form().bindFromRequest().get().getData();
        String order_status = (String) map.get("order_status");
        if (map.containsKey("pay_type_id")) {
            if (StringUtils.isEmpty(map.get("pay_type_id"))) {
                map.put("pay_type_id", null);
            } else {
                List<String> pay_type_id = new ArrayList<>();
                String payTypeId = map.get("pay_type_id").toString();
                String[] split = payTypeId.split(",");
                for (String s : split) {
                    pay_type_id.add(s);
                }
                map.put("pay_type_id", pay_type_id);
            }
        }
        //需要输出的参数
        List<Map<String, Object>> list = new ArrayList<>();
//                list=  orderService.getOrderLsitCSV(map);
        String filePath = Constant.EXCELADDRESS + File.separator + "download";
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String fileName = new Date().getTime() + ".xlsx";
        String downloadOrderExcel = configuration.getString("downloadOrderExcel");
        String[] split = downloadOrderExcel.split(",");
        JSONObject jsonObject = new JSONObject();
        try {
            PoiExcelUtil.writeXlsx(filePath + File.separator + fileName, split, JSON.toJSONString(list));
            //上传OSS服务器EXCEL
            // 需要上传的本地文件，确保该文件存在
            String endpoint = Constant.UPLOADADDRESS;
            //创建地址
            String path = Constant.STATICADDRESS + "/download/" + fileName;
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType("Text");
            // 创建OSSClient实例
            OSSClient ossClient = new OSSClient(endpoint, Constant.DESTACCESSKEY, Constant.DESTSECRETKEY);
            // 上传文件
            ossClient.putObject(Constant.BUCKETNAME, path, new File(filePath + File.separator + fileName), meta);
            // 关闭client
            ossClient.shutdown();
            jsonObject.put("file_url", Constant.VISITADDRESS + "/download/" + fileName);
        } catch (IOException e) {
            logger.error("生成文件出错：{}", e);
            e.printStackTrace();
            jsonObject.put("file_url", "");
        }
        return ok(ResultView.success("成功", jsonObject));
    }
}
