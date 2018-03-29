package service.impl;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import service.WeinxinUtilService;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
/**
 * Created by MR.GANG on 2017/7/30.
 */
public class WeinxinUtilServiceImpl implements WeinxinUtilService {
    private static String domain = "http://wxlottery.haidilao.com";
    @Override
    public String sendWX(String systemId, String userHrId, String msgContent) throws Exception {
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
    @Override
    public String sendAttrWX(String systemId, String userHrId, String msgContent) throws Exception {
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

    @Override
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
}
