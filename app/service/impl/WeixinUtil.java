package service.impl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

public class WeixinUtil {

	private static String domain = "http://wxlottery.haidilao.com";
	//private static String domain = "http://localhost:51801";

	/**
	 *
	 * @param systemId
	 *            系统标识 催办系统为noBpb8UK9OXRuXfid
	 * @param userHrId
	 *            数据库中的login_name hz_sgb
	 * @param msgContent
	 *            发送消息内容
	 * @return 返回字符串true 就是发送成功，其他为失败，且是失败原因
	 * @throws Exception
	 */

	public String sendWX(String systemId, String userHrId, String msgContent)
			throws Exception {
		msgContent = URLEncoder.encode(msgContent, "UTF-8");
		String url = domain+"/Common/Web/Handler/SendMsg.ashx";
		String query = "systemId=" + systemId + "&userHrId=" + userHrId
				+ "&msgContent=" + msgContent;
		URL restURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setAllowUserInteraction(false);
		PrintStream ps = new PrintStream(conn.getOutputStream());
		// query= URLEncoder.encode(query, "UTF-8");
		ps.print(query);
		ps.close();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line, resultStr = "";
		while (null != (line = bReader.readLine())) {
			resultStr += line;
		}
		bReader.close();
		System.out.println(resultStr);
		// 返回字符串为true表示成功,其他为失败原因
		String returnValue = xmlElements(resultStr);
		System.out.println(returnValue);
		return returnValue;
	}

	/**
	 *
	 * @param systemId
	 *            系统标识 催办系统为noBpb8UK9OXRuXfid
	 * @param userTel
	 *            数据库中的login_name hz_sgb
	 * @param msgContent
	 *            发送消息内容
	 * @return 返回字符串true 就是发送成功，其他为失败，且是失败原因
	 * @throws Exception
	 */

	public String sendWX(String systemId, String uName, String userTel,
						 String msgContent) throws Exception {
		msgContent = URLEncoder.encode(msgContent, "UTF-8");
		uName = URLEncoder.encode(uName, "UTF-8");
		String url = domain+"/Common/Web/Handler/SendTelMsg.ashx";
		String query = "systemId=" + systemId + "&userName=" + uName
				+ "&userTel=" + userTel + "&msgContent=" + msgContent;
		URL restURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setAllowUserInteraction(false);
		PrintStream ps = new PrintStream(conn.getOutputStream());
		// query= URLEncoder.encode(query, "UTF-8");
		ps.print(query);
		ps.close();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line, resultStr = "";
		while (null != (line = bReader.readLine())) {
			resultStr += line;
		}
		bReader.close();
		System.out.println(resultStr);
		// 返回字符串为true表示成功,其他为失败原因
		String returnValue = xmlElements(resultStr);
		System.out.println(returnValue);
		return returnValue;
	}


	/**
	 *
	 * @param systemId
	 *            系统标识 催办系统为noBpb8UK9OXRuXfid
	 * @param userHrId
	 *            数据库中的login_name hz_sgb
	 * @param msgContent
	 *            发送图文消息
	 * @return 返回字符串true 就是发送成功，其他为失败，且是失败原因
	 * @throws Exception
	 */

	public String sendAttrWX(String systemId, String userHrId,
							 String msgContent) throws Exception {
		msgContent = URLEncoder.encode(msgContent, "UTF-8");
		String url = domain+"/Common/Web/Handler/SendMmsMsg.ashx";
		String query = "systemId=" + systemId + "&userHrId=" + userHrId
				+ "&msgContent=" + msgContent;
		URL restURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setAllowUserInteraction(false);
		PrintStream ps = new PrintStream(conn.getOutputStream());
		// query= URLEncoder.encode(query, "UTF-8");
		ps.print(query);
		ps.close();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line, resultStr = "";
		while (null != (line = bReader.readLine())) {
			resultStr += line;
		}
		bReader.close();
		System.out.println(resultStr);
		// 返回字符串为true表示成功,其他为失败原因
		String returnValue = xmlElements(resultStr);
		System.out.println(returnValue);
		return returnValue;
	}

	/**
	 * 解析微信反馈的xml字符串
	 *
	 * @param xmlDoc
	 * @return
	 */
	public String xmlElements(String xmlDoc) {
		String returnValue = "";
		// 创建一个新的字符串
		StringReader read = new StringReader(xmlDoc);
		// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(read);
		// 创建一个新的SAXBuilder
		SAXBuilder sb = new SAXBuilder();
		try {
			// 通过输入源构造一个Document
			Document doc = sb.build(source);
			// 取的根元素
			Element root = doc.getRootElement();
			// 得到根元素所有子元素的集合
			List<Element> jiedian = root.getChildren();
			// 获得XML中的命名空间（XML中未定义可不写）
			String res = jiedian.get(0).getText();
			String errorMess = jiedian.get(1).getText();
			if ("false".equals(res)) {
				returnValue = errorMess;
			} else {
				returnValue = "true";
				// returnValue="true@@lix8ewmsaomiao";
			}

		} catch (JDOMException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return returnValue;
	}

//	public static void main(String[] agrs) {
//		String userHrId = "18613861900";
//		String msgContent = "{'title':'选徒弟并培养汇报学习情况4','description':'点击本消息可查看明细...','tourl':'','appid':'hdlapp025','state':'hhtmms','urlparam':'parm1=value1*parm2=value2*parm3=value3','picurl':''}";
//		// try {
//		// userHrId=MD5Util.getEncryptedPwd(userHrId);
//		// msgContent=MD5Util.getEncryptedPwd(msgContent);
//		// } catch (NoSuchAlgorithmException e1) {
//		// TODO Auto-generated catch block
//		// e1.printStackTrace();
//		// } catch (UnsupportedEncodingException e1) {
//		// TODO Auto-generated catch block
//		// e1.printStackTrace();
//		// }
//
//		WeixinUtil wx = new WeixinUtil();
//		try {
//			// msgContent=URLEncoder.encode(msgContent, "UTF-8");
////			 String rs = wx.sendWX("noBpb8UK9OXRuXfid", userHrId,
////			 msgContent);
////			String rs = wx.sendAttrWX("noBpb8UK9OXRuXfid", "Hwang2017",
////					msgContent);// 工资消息测试
//			 String rs = wx.sendWX("noBpb8UK9OXRuXfid", "王东旭", userHrId,
//			 msgContent);
////			String rs = wx.sendWX("noBpb8UK9OXRuXfid", "王刚", "18513196120",
////			 msgContent);
////			String rs = wx.sendAttrWX("noBpb8UK9OXRuXfid", userHrId,
////					msgContent);// 工资消息测试
//			System.out.println(rs);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
