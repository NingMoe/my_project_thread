package service.impl;

import com.alibaba.fastjson.JSONObject;
import play.Configuration;
import service.CertificateService;
import utils.JdkHttpUtil;

import java.util.List;
import java.util.Map;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/8/7 17:19
 */
public class CertificateServiceImpl implements CertificateService {

    @Override
    public List<Map<String, Object>> queryListByCode(String employeeCode) {
        String url = Configuration.root().getString("certificate.url")+"/getCertificateByCode";
        String result= JdkHttpUtil.get(url,"employeeCode="+employeeCode);
        JSONObject jSONObject=JSONObject.parseObject(result);
        if (jSONObject.get("obj")!=null){
            return (List<Map<String, Object>>) jSONObject.get("obj");
        }
        return null;
    }
}
