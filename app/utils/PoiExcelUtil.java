package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * poi 处理 excel
 *
 * @author zhujp
 * @version 1.0
 * @date 2016/12/27
 */
public class PoiExcelUtil {


    /**
     * 获取excel的工作簿
     *
     * @param filePath
     * @return
     */
    public static Workbook getWorkbook(String filePath){
        FileInputStream fis =null;
        Workbook wookbook = null;
        try {
            fis = new FileInputStream(filePath);
            if (filePath.endsWith(".xls")){
                wookbook = new HSSFWorkbook(fis);//得到工作簿
            }else{
                wookbook = new XSSFWorkbook(fis);//得到工作簿
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wookbook;
    }


    /**
     * 获取excel的数据，sheet表头要一致
     *
     * @param filePath 文件路径
     * @param cellNames 表头列名
     * @return
     */
    public static List<Map<String,Object>> getDataListByExcel(String filePath,String[] cellNames){
        List<Map<String,Object>> userList = new ArrayList<>();
        Map<String,Object> map;
        Workbook wookbook = getWorkbook(filePath);
        int totalSheetNum = wookbook.getNumberOfSheets();
        for(int s=0;s<totalSheetNum;s++){
            //得到一个工作表
            Sheet sheet = wookbook.getSheetAt(s);
            int totalRowNum = sheet.getLastRowNum();
            if(totalRowNum==0){
                continue;
            }
            //表头列名获取相应的列
            Row rowHead = sheet.getRow(0);
            Map<String,Integer> headMap = new HashMap<>();
            if(rowHead.getPhysicalNumberOfCells() != cellNames.length)
            {
                throw new RuntimeException("文件列名不正确，请上传正确的Excel文件！");
            }
            int flag = 0;
            while (flag<cellNames.length){
                Cell cell = rowHead.getCell(flag);
                for(int i=0;i<cellNames.length;i++){
                    String cellName = cellNames[i];
                    if((cellName.split("&")[1]).equals(getRightTypeCell(cell).toString().trim())){
                        headMap.put(cellName, flag);
                    }
                }
                flag++;
            }
            if(flag != headMap.size()){
                throw new RuntimeException("文件列名不正确，请上传正确的Excel文件！");
            }
            //获取对应列的数据
            for(int r=1;r<=totalRowNum;r++){
                map = new HashMap<>();
                Row row = sheet.getRow(r);
                Iterator<Map.Entry<String, Integer>> it = headMap.entrySet().iterator();
                while (it.hasNext()){
                    Map.Entry<String, Integer> entry = it.next();
                    Object value = getRightTypeCell(row.getCell(entry.getValue()));
                    map.put(entry.getKey().split("&")[0],value);
                }
                userList.add(map);
            }
        }
        return userList;
    }

    /**
     *
     * @param cell 一个单元格的对象
     * @return 返回该单元格相应的类型的值
     */
    public static Object getRightTypeCell(Cell cell){
        Object object = null;
        if(cell == null){
            return "";
        }
        switch(cell.getCellType())
        {
            case Cell.CELL_TYPE_STRING :
            {
                object=cell.getStringCellValue();
                break;
            }
            case Cell.CELL_TYPE_NUMERIC :
            {
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                object = new DecimalFormat("#").format(cell.getNumericCellValue());
                break;
            }
            case Cell.CELL_TYPE_FORMULA :
            {
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                object = new DecimalFormat("#").format(cell.getNumericCellValue());
                break;
            }
            case Cell.CELL_TYPE_BLANK :
            {
                cell.setCellType(Cell.CELL_TYPE_BLANK);
                object=cell.getStringCellValue();
                break;
            }
        }
        return object;
    }

    /**
     * 生成xlsx 文件，
     *
     * @param fileName  文件路径
     * @param headNames 表头
     * @param jsonData  数据
     * @throws IOException
     */
    public static void writeXlsx(String fileName,String[] headNames,String jsonData) throws IOException {
        String datePattern="yyyy年MM月dd日";//默认日期格式

        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);//缓存
        workbook.setCompressTempFiles(true);
        //创建sheet
        SXSSFSheet sheet = null;
        int rowIndex = 0;
        JSONArray jsonArray = JSONArray.parseArray(jsonData);
        for (Object obj : jsonArray) {
            //如果数据超过了，则在第二页显示
            if((rowIndex+65535)%65535==0){
                sheet = (SXSSFSheet) workbook.createSheet();
                //创建表头
                SXSSFRow headerRow  = (SXSSFRow) sheet.createRow(0);//表头 rowIndex=0
                for(int i=0;i<headNames.length;i++){
                    headerRow.createCell(i).setCellValue(headNames[i].split("&")[1]);
                }
                rowIndex=1;
            }
            //写数据
            JSONObject json = (JSONObject) JSONObject.toJSON(obj);
            SXSSFRow dataRow = (SXSSFRow) sheet.createRow(rowIndex);
            for(int i=0;i<headNames.length;i++){
                SXSSFCell newCell = (SXSSFCell) dataRow.createCell(i);
                Object o = json.get(headNames[i].split("&")[0]);
                String cellValue = "";
                if(o==null) {
                    cellValue = "";
                }else if(o instanceof Date) {
                    cellValue = new SimpleDateFormat(datePattern).format(o);
                }else if(o instanceof Float || o instanceof Double) {
                    cellValue = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                }else{
                    cellValue = o.toString();
                }
                newCell.setCellValue(cellValue);
            }
            rowIndex++;
        }
        FileOutputStream fout = new FileOutputStream(fileName);
        workbook.write(fout);
        fout.close();
    }

    public static void main(String[] args) {
        String filePath = "E:\\userInfo.xlsx";
        String[] cellNames = new String[]{"userName&姓名","userSex&性别","userPhone&手机号","userDept&部门","userPost&岗位","userNumber&工号","userEmail&邮箱"};
        System.out.println(JSON.toJSONString(getDataListByExcel(filePath,cellNames)));
    }
}
