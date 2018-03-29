package ws.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import models.ProEliteTestRecord;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import play.Configuration;
import ws.EvaluationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/19 09:56
 */
public class EvaluationServiceImpl implements EvaluationService {


    HttpClientContext context = HttpClientContext.create();


    /**
     * 构造Basic Auth认证头信息
     *
     * @return
     */
    private String getHeader() {
        String auth = APP_KEY + ":" + SECRET_KEY;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        return authHeader;
    }

    public String sendTestPerson(List<Map<String, Object>> list) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(Configuration.root().getString("test.url") + "invitation");
        httpPost.setHeader("Authorization", getHeader());
        List<Map<String, Object>> Candidates = new ArrayList<>();
        for (Map<String, Object> objectMap : list) {
            Map<String, Object> Candidate = new HashMap<>();
            Candidate.put("ExternalId", objectMap.get("ExternalId"));
            Candidate.put("Name", objectMap.get("Name"));
            Candidate.put("Age", objectMap.get("Age"));
            Candidate.put("Gender", objectMap.get("Gender"));
            Candidate.put("Email", objectMap.get("Email"));
            Candidate.put("Mobile", objectMap.get("Mobile"));
            Object[] objects = new Object[4];
            Map<String, Object> map = new HashMap<>();
            map.put("name", "门店");
            map.put("value", objectMap.get("shopName"));
            objects[0] = map;
            Map<String, Object> map1 = new HashMap<>();
            map1.put("name", "岗位");
            map1.put("value", objectMap.get("position"));
            objects[1] = map1;
            Map<String, Object> map2 = new HashMap<>();
            map2.put("name", "教育程度");
            map2.put("value", objectMap.get("culture"));
            objects[2] = map2;
            Map<String, Object> map3 = new HashMap<>();
            map3.put("name", "工作经验");
            String workTime;
            if (objectMap.get("firstTime") == null) {
                workTime = objectMap.get("lastTime") == null ? null : objectMap.get("lastTime").toString();
            } else {
                workTime = objectMap.get("firstTime").toString();
            }
            map3.put("value", workTime);
            objects[3] = map3;
            Candidate.put("ExtraInfo", objects);
            Candidates.add(Candidate);
        }
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("SessionId", SESSION_ID);
        jsonParam.put("Candidates", Candidates);
        StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost, context);
        if (response.getStatusLine().getStatusCode() == 200) {
            return "SUCESS";
        }
        return "FAIL";
    }

    public String getTestRecord(List<ProEliteTestRecord> list) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(Configuration.root().getString("test.url") + "report");
        httpPost.setHeader("Authorization", getHeader());
        List<Map<String, Object>> Reports = new ArrayList<>();
        Map<String, Object> report = new HashMap<>();
        report.put("ReportUid", REPORTUID);
        report.put("NormIds", NORMIDS);
        String[] RespondentUids = new String[list.size()];
        int i = 0;
        for (ProEliteTestRecord record : list) {
            RespondentUids[i] = record.getTestOnceId();
            i++;
        }
        report.put("RespondentUids", RespondentUids);
        Reports.add(report);
        JSONObject jsonParam = new JSONObject();
        //TODO
        jsonParam.put("SessionId", SESSION_ID);
        jsonParam.put("Reports", Reports);
        StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost, context);
        if (response.getStatusLine().getStatusCode() == 200) {
            return "SUCESS";
        }
        return "FAIL";
    }

    public List<ProEliteTestRecord> getTestEnd(List<ProEliteTestRecord> list) throws IOException {
        BufferedReader in = null;
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(Configuration.root().getString("test.url") + "candidate-finish-status");
        httpPost.setHeader("Authorization", getHeader());
        String[] RespondentUids = new String[list.size()];
        int i = 0;
        for (ProEliteTestRecord record : list) {
            RespondentUids[i] = record.getTestOnceId();
            i++;
        }
        JSONObject jsonParam = new JSONObject();
        //TODO
        jsonParam.put("Candidates", RespondentUids);
        StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost, context);
        in = new BufferedReader(new InputStreamReader(response.getEntity()
                .getContent()));
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
        while ((line = in.readLine()) != null) {
            sb.append(line + NL);
        }
        in.close();
        String content = sb.toString();
        JSONObject object = JSON.parseObject(content);
        List<Map<String, Object>> mapList = (List<Map<String, Object>>) object.get("candidateFinishStatus");
        List<ProEliteTestRecord> results = new ArrayList<>();
        for (Map map : mapList) {
            if (map.get("endTime") != null) {
                for (ProEliteTestRecord testRecord : list) {
                    if (testRecord.getTestOnceId().equals(map.get("respondentId"))) {
                        testRecord.setTestEndTime(map.get("endTime").toString());
                        testRecord.setModifyTime(System.currentTimeMillis());
                        testRecord.setModifierId("systemEndTask");
                        results.add(testRecord);
                    }
                }
            }
        }

        return results;
    }
}
