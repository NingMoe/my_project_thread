package ws.Impl;


import java.io.IOException;
import java.net.URL;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.hht.utils.PropertiesUtil;
import common.ConfigConstant;
import models.ProEliteClass;
import models.ProEliteClassPerson;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import play.Configuration;
import vo.ProEliteClassPersonVo;
import ws.GraduationService;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/7/31 14:51
 */
public class GraduationServiceImpl implements GraduationService {


    public void sendGraduationResult(List<ProEliteClassPerson> list, ProEliteClass eliteClass) throws IOException, DocumentException {

        for (ProEliteClassPerson classPerson : list) {
            String header = getHeader(classPerson, eliteClass);
            sendGraduation(header);
        }
    }

    private String getHeader(ProEliteClassPerson classPerson, ProEliteClass eliteClass) {
        StringBuilder soapHeader = new StringBuilder();
        soapHeader.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>");
        soapHeader.append("<soapenv:Header/>");
        soapHeader.append("<soapenv:Body>");
        soapHeader.append("<EmployeeTraningInfo>");
        soapHeader.append("<Emplid>" + classPerson.getEmployeeCode() + "</Emplid>");
        soapHeader.append("<TrainingCourse>" + eliteClass.getTerm() + "</TrainingCourse>");
        soapHeader.append("<TrainingPlace>" + eliteClass.getTrainPlace() + "</TrainingPlace>");
        soapHeader.append("<TrainingType>" + "02" + "</TrainingType>");
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        soapHeader.append("<BeginDate>" + df.format(new Date(eliteClass.getBeginTime())) + "</BeginDate>");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(eliteClass.getBeginTime()));
        c.add(Calendar.DAY_OF_MONTH, eliteClass.getTrainDays());

        soapHeader.append("<EndDate>" + df.format(c.getTime()) + "</EndDate>");
        soapHeader.append("<TrainerID>" + " " + "</TrainerID>");
        String name = eliteClass.getTeacherName();
        if (eliteClass.getAssistantTeacherName() != null && !"".equals(eliteClass.getAssistantTeacherName())) {
            name = name + "," + eliteClass.getAssistantTeacherName();
        }
        soapHeader.append("<TrainerName>" + name + "</TrainerName>");
        soapHeader.append("<CourseName>" + " " + "</CourseName>");
        soapHeader.append("<ClassHour>" + " " + "</ClassHour>");
        soapHeader.append("<TheoryScore>" + " " + "</TheoryScore>");
        soapHeader.append("<PracticeScore>" + " " + "</PracticeScore>");
        String classType = "";
        if ("100".equals(eliteClass.getClassType())){
            classType = "A";
        }else
        if ("200".equals(eliteClass.getClassType())){
            classType = "B";
        }
        soapHeader.append("<CertificateLvl>" + classType + "</CertificateLvl>");
        String isPass = "";
        if("200".equals(classPerson.getResult())){
            isPass="N";
        }else if("300".equals(classPerson.getResult())){
            isPass="Y";
        }
        soapHeader.append("<WhetherPass>" + isPass + "</WhetherPass>");
        soapHeader.append("<ReceivedHonorary>" + " " + "</ReceivedHonorary>");
        soapHeader.append("<ComprehensiveEvaluation>" + " " + "</ComprehensiveEvaluation>");
        soapHeader.append("<ExaminerEvaluation>" + " " + "</ExaminerEvaluation>");
        soapHeader.append("<Comment>" + " " + "</Comment>");
        soapHeader.append("<EntryPerson>" + " " + "</EntryPerson>");
        soapHeader.append("<EntryPersonName>" + " " + "</EntryPersonName>");
        soapHeader.append("</EmployeeTraningInfo>");
        soapHeader.append("</soapenv:Body>");
        soapHeader.append("</soapenv:Envelope>");
        return soapHeader.toString();
    }


    public String sendGraduation(String soapHeader) throws IOException, DocumentException {
        // 拼装soap请求报文
        String sb = new String();
        String url = Configuration.root().getString("graduation_result.url");
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setDefaultUseCaches(false);
//        conn.setRequestProperty("Host", "172.21.107.8:8010");
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        conn.setRequestProperty("Content-Length", String.valueOf(soapHeader.length()));
        conn.setRequestProperty("SOAPAction", "ADD.v1");
        conn.setRequestMethod("POST");
        // 定义输出流
        OutputStream output = conn.getOutputStream();
        if (null != soapHeader) {
            byte[] b = soapHeader.toString().getBytes("utf-8");
            // 发送soap请求报文
            output.write(b, 0, b.length);
        }
        output.flush();
        output.close();
        // 定义输入流，获取soap响应报文
        InputStream input = conn.getInputStream();
        sb = IOUtils.toString(input, "UTF-8");
        System.out.println(sb);
        input.close();
        return XMLReader(sb);
    }

    public static String XMLReader(String sb) throws DocumentException {
        Document document = DocumentHelper.parseText(sb);
        Element node = document.getRootElement();
        System.out.println(node.toString());
        return listNodes(node);
    }

    public static String listNodes(Element node) {
        // 获取当前节点的所有属性节点
        if (!(node.getTextTrim().equals(""))) {
            if (node.getName().equals("StatusCode")) {
                System.out.println(node.getName()+"........."+node.getStringValue());
            }
            if (node.getName().equals("Message")) {
                System.out.println(node.getName()+"........."+node.getStringValue());
            }
        }
        // 当前节点下面子节点迭代器
        Iterator<Element> it = node.elementIterator();
        // 遍历
        while (it.hasNext()) {
            // 获取某个子节点对象
            Element e = it.next();
            // 对子节点进行遍历
            listNodes(e);
        }
        return "";
    }

}

