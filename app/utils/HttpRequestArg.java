package utils;

import common.Constant;

import java.util.HashMap;
import java.util.Map;


public class HttpRequestArg {
	private String url;
	private String method= Constant.Method.POST.value;
	private Map header=new HashMap();
	private Map<String,Object> argMap=new HashMap<String,Object>();
	private String paramStr;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Map<String, Object> getArgMap() {
		return argMap;
	}
	public void setArgMap(Map<String, Object> argMap) {
		this.argMap = argMap;
	}
	public String getParamStr() {
		return paramStr;
	}
	public void setParamStr(String paramStr) {
		this.paramStr = paramStr;
	}

	public Map getHeader() {
		return header;
	}

	public void setHeader(Map header) {
		this.header = header;
	}
}
