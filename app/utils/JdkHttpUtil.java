package utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class JdkHttpUtil {
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 * @throws Exception
	 */
	public static String get(String url, String param){
		InputStream in = null;
		ByteArrayOutputStream bain=null;
		String result=null;
		try {
			String urlName = url + "?" + param;
			URL realUrl = new URL(urlName);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = conn.getHeaderFields();
			// 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// System.out.println(key + "--->" + map.get(key));
			// }
			in = conn.getInputStream();
			bain=new ByteArrayOutputStream();
			byte[] temp=new byte[1024];
			int len=0;
			while((len=in.read(temp))!=-1){
				bain.write(temp,0,len);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("发送GET请求出现异常！" + e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		byte [] b=bain.toByteArray();
		try {
			result= new String(b,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求体。
	 * @return URL所代表远程资源的响应
	 */
	public static String post(String url, String param) {
		PrintWriter out = null;
		InputStream in = null;
		ByteArrayOutputStream bain=null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", "application/json");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			in = conn.getInputStream();
			bain=new ByteArrayOutputStream();
			byte[] temp=new byte[1024];
			int len=0;
			while((len=in.read(temp))!=-1){
				bain.write(temp,0,len);
			}
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		byte [] b=bain.toByteArray();
		String result=null;
		try {
			result= new String(b,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	//把map表单转换为url参数形式
	public static String getUrlParamsByMap (Map<String, Object> map) {
		if (map == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		try {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String value = URLEncoder.encode((String) entry.getValue(), "utf-8");
				sb.append(entry.getKey() + "=" + value);
				sb.append("&");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		String s = sb.toString();
		if (s.endsWith("&")) {
			s = org.apache.commons.lang.StringUtils.substringBeforeLast(s, "&");
		}
		return s;
	}
	public static void main(String args[]) throws Exception {
		String json="{\"appId\": \"10003\",\"userId\": \"0100000064540445\",\"redEnvelopeActivityId\": \"100019,5003,5002,5001\"}";


		System.out.println(post("http://172.16.149.57:7777/CouponCodeApp/rs/external/couponcore/activitYlist",json));
	}
}