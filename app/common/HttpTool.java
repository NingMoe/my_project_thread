package common;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import play.mvc.Result;

public class HttpTool {

    private static final String URL = "http://uat.api.i-select.cn/invitation";
    private static final String APP_KEY = "hidilao-uat";
    private static final String SECRET_KEY = "8080640F-7225-4C75-B8FC-61B9CF5757C7";


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


    public void addUserOAuth(String username, String password) {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        org.apache.http.auth.Credentials credentials = new org.apache.http.auth.UsernamePasswordCredentials(username, password);
        credsProvider.setCredentials(org.apache.http.auth.AuthScope.ANY, credentials);
        this.context.setCredentialsProvider(credsProvider);
    }

    public Result send() throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL);
        httpPost.setHeader("Authorization",getHeader());
        List<Map<String,Object>> Candidates = new ArrayList<>();
        Map<String,Object> Candidate = new HashMap<>();
        Candidate.put("ExternalId","99887766");
        Candidate.put("Name","王东旭");
        Candidate.put("Age","33");
        Candidate.put("Gender","男");
        Candidates.add(Candidate);
        Map<String,Object> Candidate1 = new HashMap<>();
        Candidate1.put("ExternalId","66778899");
        Candidate1.put("Name","王东旭");
        Candidate1.put("Age","33");
        Candidate1.put("Gender","男");
        Candidates.add(Candidate1);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("SessionId", "5096");
        jsonParam.put("Candidates", Candidates);
        StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        System.out.println(EntityUtils.toString(httpPost.getEntity(),"utf-8"));
        HttpResponse response = httpClient.execute(httpPost, context);
        if(response.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = response.getEntity();
            String s = EntityUtils.toString(he, "UTF-8");
        }
        return null;
    }
}