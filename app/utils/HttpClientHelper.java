package utils;

import com.hht.exception.HhtRuntimeException;
import common.Constant;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpClientHelper {
	private static Logger logger = LoggerFactory.getLogger(HttpClientHelper.class);

	private static CloseableHttpResponse createRequest(HttpRequestArg arg,CloseableHttpClient httpclient) throws IOException {
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		StringEntity paramEntity=null;
		String url=arg.getUrl();
		CloseableHttpResponse response=null;
		if(null!=arg.getHeader()&&arg.getHeader().size()>0){

		}
		if(StringUtils.isEmpty(arg.getParamStr())){
			Map<String,Object> argMap=arg.getArgMap();

			for(String key:argMap.keySet()){
				nvps.add(new BasicNameValuePair(key, String.valueOf(argMap.get(key))));
			}
			if(arg.getMethod().equals(Constant.Method.POST.value)){

				paramEntity=new UrlEncodedFormEntity(nvps, "UTF-8");
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(paramEntity);
				response=httpclient.execute(httpPost);
			}else{
				String getParams = EntityUtils.toString(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
				if(!StringUtils.isEmpty(getParams)){
					if(url.lastIndexOf("?")>0){
						url=url+"&"+getParams;
					}else{
						url=url+"?"+getParams;
					}
				}

				HttpGet httpGet=new HttpGet(url);
				response=httpclient.execute(httpGet);
			}

		}else{
			if(arg.getMethod().equals(Constant.Method.POST.value)){
				paramEntity=new StringEntity(arg.getParamStr(), "UTF-8");
				paramEntity.setContentEncoding("UTF-8");
				paramEntity.setContentType("application/json");
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(paramEntity);
				response=httpclient.execute(httpPost);
			}else{
				if(StringUtils.isNotEmpty(arg.getParamStr())){
					String encodeJsonStr=java.net.URLEncoder.encode(arg.getParamStr(),"UTF-8");
					if(url.lastIndexOf("?")>0){
						url=url+"&"+encodeJsonStr;
					}else{
						url=url+"?"+encodeJsonStr;
					}

				}
				HttpGet httpGet=new HttpGet(url);
				response=httpclient.execute(httpGet);
			}

		}

		return response;

	}
	public static String doHttpWork(HttpRequestArg arg) {

		HttpEntity entity=null;
		String url=arg.getUrl();
		HttpRequestBase httpRequest=null;		
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		
		StringEntity paramEntity=null;
		
		try(CloseableHttpClient httpclient = HttpClients.createDefault();
			CloseableHttpResponse response=createRequest(arg,httpclient);) {

			entity = response.getEntity();
			String entityStr=EntityUtils.toString(entity);
			return entityStr;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HhtRuntimeException(Constant.RESULT_FAIL,e.getCause().toString());
		}finally{
			if(null!=entity){
				try {
					EntityUtils.consume(entity);
				} catch (IOException e) {
					logger.error("",e);
				}
			}
		}
	}

	/**
	 * @param  url  访问路径
	 * @param method  POST/GET/DELETE
	 * @param param Json参数
	 * */
	public static String requestByJson(String url,String method,String param){
		HttpRequestArg arg=new HttpRequestArg();
		arg.setUrl(url);
		arg.setMethod(method);
		arg.setParamStr(param);
		Map map=new HashMap();
		map.put("Content-Type","application/json;charset=utf-8");
		arg.setHeader(map);
		return doHttpWork(arg);
	}
	/**
	 * @param  url  访问路径
	 * @param method  POST/GET/DELETE
	 * @param param Json参数
	 * */
	public static String requestByJsonAndToken(String url,String method,String param,String token){
		HttpRequestArg arg=new HttpRequestArg();
		arg.setUrl(url);
		arg.setMethod(method);
		arg.setParamStr(param);
		Map map=new HashMap();
		map.put("Token",token);
		map.put("Content-Type","application/json;charset=utf-8");
		arg.setHeader(map);
		return doHttpWork(arg);
	}
	/**
	 * @param  url  访问路径
	 * @param method  POST/GET/DELETE
	 * @param param 参数
	 * */
	public static String requestByForm(String url,String method,Map<String,Object> param) {
		HttpRequestArg arg=new HttpRequestArg();
		arg.setUrl(url);
		arg.setMethod(method);
		arg.setArgMap(param);
		return doHttpWork(arg);
	}


	/**
	 * @Description 调用海底捞HTTP请求
	 *
	 * @param url
	 * @param para
	 * @return
	 * @LastModifiedDate：<date>
	 * @author chen_linxiang
	 * @EditHistory：<修改内容><修改人>
	 */
	public static String httpHDLSend(String url, String method, String para) {
		String responseResult = null;
		CloseableHttpClient httpClient = null;
		HttpPost httpPost = null;
		try {
			httpClient = HttpClients.createDefault();
			httpPost = new HttpPost(url);
			httpPost.setEntity(new StringEntity(para == null ? "" : para, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			System.out.print(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && null != httpEntity) {
				responseResult = EntityUtils.toString(httpEntity, "UTF-8");
			}
		} catch (Exception e) {
			if (httpPost != null) {
				httpPost.abort();
			}
			play.Logger.error("网络请求异常:", e);
		} finally {
			if (null != httpClient) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		return responseResult;
	}


}
