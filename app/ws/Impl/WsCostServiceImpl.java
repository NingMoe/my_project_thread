package ws.Impl;

import com.google.inject.Inject;
import com.hht.utils.PropertiesUtil;
import common.ConfigConstant;
import common.Constant;
import models.*;
import models.vo.ProEmployeeVo;
import org.apache.commons.io.IOUtils;
import org.dom4j.DocumentException;
import play.Configuration;
import service.EliteTrainCostService;
import service.EmployeeService;
import ws.WsCostService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static ws.Impl.GraduationServiceImpl.XMLReader;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/8/8 15:20
 */
public class WsCostServiceImpl implements WsCostService {

    @Inject
    private EmployeeService employeeService;

    @Inject
    private EliteTrainCostService costService;

    public void sendCostResult(List<ProEliteClassPerson> list, ProEliteClass eliteClass, String costNode) throws IOException, DocumentException {
        ProEliteTrainCost cost = costService.getProEliteTrainCosts();
        List<ProEmployeeVo> managers = new ArrayList<>();
        Set<String> shopIds = new HashSet<>();
        for (ProEliteClassPerson shopElite : list) {
            shopIds.add(shopElite.getShopId());
        }
        managers = employeeService.queryListByShopIds(shopIds);
        for (ProEliteClassPerson shopElite : list) {
            for (ProEmployeeVo manager : managers) {
                if (shopElite.getShopId().equals(manager.getShopId())) {
                    if (Constant.COST_NODE_MANAGER_NOPASS.equals(costNode)) {
                        String header = getHeader(shopElite, eliteClass, Constant.COST_NODE_MANAGER_PASS, cost);
                        sendGraduation(header);
                        String mHeader = getMangerHeader(shopElite, eliteClass, Constant.COST_NODE_MANAGER_PASS, cost,manager);
                        sendGraduation(mHeader);
                    }
                    String employeeHeader = getHeader(shopElite, eliteClass, costNode, cost);
                    sendGraduation(employeeHeader);
                    String managerHeader = getMangerHeader(shopElite, eliteClass, costNode, cost,manager);
                    sendGraduation(managerHeader);
                }
            }
        }
    }

    public void sendTestCostResult(List<ProShopElite> list) throws IOException, DocumentException {
        ProEliteTrainCost cost = costService.getProEliteTrainCosts();
        List<ProEmployeeVo> managers = new ArrayList<>();
        Set<String> shopIds = new HashSet<>();
        for (ProShopElite shopElite : list) {
            shopIds.add(shopElite.getShopId());
        }
        managers = employeeService.queryListByShopIds(shopIds);
        for (ProShopElite shopElite : list) {
            for (ProEmployeeVo manager : managers) {
                if (shopElite.getShopId().equals(manager.getShopId())) {
                    String employeeHeader = getTestHeader(shopElite, cost);
                    sendGraduation(employeeHeader);
                    String managerHeader = getTestMangerHeader(shopElite, cost,manager);
                    sendGraduation(managerHeader);
                }
            }
        }
    }

    private String getHeader(ProEliteClassPerson shopElite, ProEliteClass eliteClass, String costNode, ProEliteTrainCost cost) {
        StringBuilder soapHeader = new StringBuilder();
        soapHeader.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>");
        soapHeader.append("<soapenv:Header/>");
        soapHeader.append("<soapenv:Body>");
        soapHeader.append("<EmployeeTraningDeduction>");
        soapHeader.append("<Emplid>" + shopElite.getEmployeeCode() + "</Emplid>");
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        soapHeader.append("<Date>" + df.format(new Date()) + "</Date>");
        BigDecimal costPrice;
        String text = "";
        if (Constant.COST_NODE_MANAGER_NOPASS.equals(costNode)) {
            costPrice = shopElite.getEmployeeAmerce();
            text = eliteClass.getTerm() + "后备店经理店员罚款 ";
        } else if (Constant.COST_NODE_LOBBY.equals(costNode)) {
            costPrice = shopElite.getEmployeeCost();
            text = eliteClass.getTerm() + "后备大堂班  店员缴纳";
        } else if (Constant.COST_NODE_MANAGER_PASS.equals(costNode)) {
            costPrice = shopElite.getEmployeeCost();
            text = eliteClass.getTerm() + "后备店经理班店员缴纳";
        } else {
            costPrice = cost.getEvaluatingEmployeeCost();
            text = "评测收费店员缴纳";
        }

        soapHeader.append("<Amount>" + costPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "</Amount>");
        soapHeader.append("<Comments>" + text + "</Comments>");
        soapHeader.append("</EmployeeTraningDeduction>");
        soapHeader.append("</soapenv:Body>");
        soapHeader.append("</soapenv:Envelope>");
        return soapHeader.toString();
    }

    private String getMangerHeader(ProEliteClassPerson vo, ProEliteClass eliteClass, String costNode, ProEliteTrainCost cost, ProEmployeeVo manager) {
        StringBuilder soapHeader = new StringBuilder();
        soapHeader.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>");
        soapHeader.append("<soapenv:Header/>");
        soapHeader.append("<soapenv:Body>");
        soapHeader.append("<EmployeeTraningDeduction>");
        soapHeader.append("<Emplid>" + manager.getEmployeeCode() + "</Emplid>");
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        soapHeader.append("<Date>" + df.format(new Date()) + "</Date>");
        BigDecimal costPrice;
        String text = "";
        if (Constant.COST_NODE_MANAGER_NOPASS.equals(costNode)) {
            costPrice = vo.getManagerAmerce();
            text = eliteClass.getTerm() + "后备店经理班门店经理罚款";
        } else if (Constant.COST_NODE_LOBBY.equals(costNode)) {
            costPrice = vo.getManagerCost();
            text = eliteClass.getTerm() + "后备大堂班 门店经理缴纳";
        } else if (Constant.COST_NODE_MANAGER_PASS.equals(costNode)) {
            costPrice = vo.getManagerCost();
            text = eliteClass.getTerm() + "后备店经理班 门店经理缴纳";
        } else {
            costPrice = cost.getEvaluatingManagerCost();
            text = "评测收费门店经理缴纳";
        }
        soapHeader.append("<Amount>" + costPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "</Amount>");
        soapHeader.append("<Comments>" + text + "</Comments>");
        soapHeader.append("</EmployeeTraningDeduction>");
        soapHeader.append("</soapenv:Body>");
        soapHeader.append("</soapenv:Envelope>");
        return soapHeader.toString();
    }

    public String sendGraduation(String soapHeader) throws IOException, DocumentException {
        // 拼装soap请求报文
        String sb = new String();
        String url = Configuration.root().getString("cost_result.url");
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setDefaultUseCaches(false);
//        conn.setRequestProperty("Host", "172.21.107.8:8010");
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        conn.setRequestProperty("Content-Length", String.valueOf(soapHeader.length()));
        conn.setRequestProperty("SOAPAction", "DEDUCTION.v1");
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

    private String getTestHeader(ProShopElite shopElite, ProEliteTrainCost cost) {
        StringBuilder soapHeader = new StringBuilder();
        soapHeader.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>");
        soapHeader.append("<soapenv:Header/>");
        soapHeader.append("<soapenv:Body>");
        soapHeader.append("<EmployeeTraningDeduction>");
        soapHeader.append("<Emplid>" + shopElite.getEmployeeCode() + "</Emplid>");
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        soapHeader.append("<Date>" + df.format(new Date()) + "</Date>");
        BigDecimal costPrice;
        String text = "";
        costPrice = cost.getEvaluatingEmployeeCost();
        text = "评测收费店员缴纳";

        soapHeader.append("<Amount>" + costPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "</Amount>");
        soapHeader.append("<Comments>" + text + "</Comments>");
        soapHeader.append("</EmployeeTraningDeduction>");
        soapHeader.append("</soapenv:Body>");
        soapHeader.append("</soapenv:Envelope>");
        return soapHeader.toString();
    }

    private String getTestMangerHeader(ProShopElite vo, ProEliteTrainCost cost, ProEmployeeVo manager) {
        StringBuilder soapHeader = new StringBuilder();
        soapHeader.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>");
        soapHeader.append("<soapenv:Header/>");
        soapHeader.append("<soapenv:Body>");
        soapHeader.append("<EmployeeTraningDeduction>");
        soapHeader.append("<Emplid>" + manager.getEmployeeCode() + "</Emplid>");
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        soapHeader.append("<Date>" + df.format(new Date()) + "</Date>");
        BigDecimal costPrice;
        String text = "";
        costPrice = cost.getEvaluatingManagerCost();
        text = "评测收费门店经理缴纳";
        soapHeader.append("<Amount>" + costPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "</Amount>");
        soapHeader.append("<Comments>" + text + "</Comments>");
        soapHeader.append("</EmployeeTraningDeduction>");
        soapHeader.append("</soapenv:Body>");
        soapHeader.append("</soapenv:Envelope>");
        return soapHeader.toString();
    }
}
