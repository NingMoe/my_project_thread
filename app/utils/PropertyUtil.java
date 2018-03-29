package utils;

import com.hht.utils.PropertiesUtil;

/**
 * Created by WuJiaWen on 2017/3/11.
 */
public class PropertyUtil {

    private static PropertyUtil propertyUtil = new PropertyUtil();


    private PropertyUtil(){}
    public static PropertyUtil getInstance(){
        return propertyUtil;
    }

    private String shopId;

    private String company;

    public  String getShopId(){
        if(shopId == null){
            shopId = PropertiesUtil.getProperty("serinfo").getProperty("shopId");;
        }
        return shopId;
    }

    public  String getCompanyId(){
        if(shopId == null){
            shopId = PropertiesUtil.getProperty("serinfo").getProperty("companyId");;
        }
        return shopId;
    }
}
