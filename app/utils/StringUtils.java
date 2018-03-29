package utils;

import com.hht.utils.BeanIdCreater;
import com.hht.utils.IDGenerater;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.hht.utils.StringUtils.isListEmpty;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/6/10 11:27
 */
public class StringUtils {

    public static String objToString(Object o) {
        return o == null ? "" : o.toString();
    }

    public static String getTableId(String tableName) {
        IDGenerater idGenerater = new IDGenerater();
        return idGenerater.nextId();
    }

    public static String longToDate(long time, String s) {
        DateFormat df = new SimpleDateFormat(s);
        return df.format(new Date(time));
    }

//    public static void main(String[] args) {
//        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String str = "1507161600000";
//        Date currdate = new Date(Long.valueOf(str));
//        String s = format1.format(currdate);
//        System.out.println("现在的日期是：" + s);
//        Calendar ca = Calendar.getInstance();
//        ca.setTime(currdate);
//        ca.add(Calendar.DATE, 15);// num为增加的天数，可以改变的
//        currdate = ca.getTime();
//        String enddate = format1.format(currdate);
//        System.out.println("增加天数以后的日期：" + enddate);
//    }

    /**
     * 判断对象空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj instanceof List) {
            return isListEmpty((List) obj);
        }
        return obj == null || "".equals(obj) || "null".equals(obj);
    }
}
