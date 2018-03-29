package constants;

import play.Configuration;

/**
 * @author panghui
 * @version 1.0
 * @since 2016/11/18
 */
public class Constant {

    // 海底捞companyId
    public static final String HDL_COMPANY_ID = "123";

    // 生成菜单编号前缀
    public static final String MENU_CODE = "menu";

    // 生成账单编号前缀
    public static final String BILL_CODE = "bill";
    //清台状态
    public static final String  RESULT_CODE="0";//;成功
    public static final String  RESULT_FLAGCODE="1";//;失败


    //请求结果
    public static final int RESULT_CODE_SUCCESS = 0;
    public static final int RESULT_CODE_FAIL = 1;


    // 操作结果   1-成功，0-失败
    public static final int RESULT_SUCCESS = 200;
    public static final int RESULT_FAIL = 500;
    public static final String SUCCESS_MESSAGE="success";

    public static final int CLOUD_RESULT_SUCCESS = 200;
    public static final int CLOUD_RESULT_FAIL = 500;


    /**查询类型*/
    public final static String SCHEDULE_TYPE_PAY = "PAY";           //支付方式
    public final static String SCHEDULE_TYPE_ACCOUNT = "ACCOUNT";   //员工
    public final static String SCHEDULE_TYPE_DISCOUNT = "DISCOUNT"; //折扣
    public final static String SCHEDULE_TYPE_TABLE = "TABLE";       //桌位
    public final static String SCHEDULE_TYPE_DISH_INFO = "DISH";    //菜品
    public final static String SCHEDULE_TYPE_DISH_TASTE = "TASTE";  //口味
    public final static String SCHEDULE_TYPE_DISH_COOK = "COOK";    //做法
    public static enum Method{
        POST("post"),
        GET("get");
        public String value;
        public String getValue(){
            return value;
        }
        private Method(String value){
            this.value=value;
        }
    }
    public final static String CLOUD_VERSION_URL="CLOUD_VERSION_URL";
    public final static String SCHEDULE_INFO_URL="SCHEDULE_INFO_URL";
    //删除标志  Y删除   N未删除
    public static final String DR_NO = "N";
    public static final String DR_YES = "Y";
    /**
     * 云端返回结果key
     */
    public static enum ResultKey{
        RESULT("result"),
        MESSAGE("message"),
        OBJ("obj");
        public String value;
        private ResultKey(String value){this.value=value;}
    }

    /**
     * 门店数据同步类型
     */
    public static enum BShopSynchDataType{
        ACCOUNT("account"),
        DISH("dish"),
        DISH_COOKING("dishCooking"),
        DISH_TASTE("dishTaste"),
        DISCOUNT("Discount"),
        PAY_TYPE("payType"),
        TABLE("table"),
        ALL("all");
        public String value;
        private BShopSynchDataType(String value){this.value=value;}
    }
    // 静态资源上传路径
    public static final String UPLOAD_ADDRESS = Configuration.root().getString("uploadFile.address");

    // 静态资源访问路径
    public static final String VISIT_ADDRESS = Configuration.root().getString("visit.address");

    public static enum CODEMESSAGE {
        SUCCESS(200, "操作成功"),
        ENABLESUCCESS(200, "启用成功"),
        DISABLESUCCESS(200, "停用成功"),
        SUCCESSWITHFAIL(400, "操作成功，其中存在总部停用，店铺的不能启用的数据数量为"),
        SUCCESSWITHFAIL2(400, "无关联桌台的服务费删除成功"),
        SHOPIDISNULL(400, "门店为空"),
        IDISNULL(400, "id为空"),
        NAMEISNULL(400, "名称为空"),
        NAMENUMLIMIT(400, "名称必须为数字，且长度限制为"),
        NAMELIMIT(400, "名称长度限制为"),
        ISSELF(400, "不能选择当前餐区为上级餐区"),
        TASTENAMELIMIT(400, "口味项长度限制为"),
        REMARKLIMIT(400, "备注长度限制为"),
        ISREPEAT(400, "名称重复"),
        HASCHILDREN(400, "请先删除下级餐区"),
        NOISREPEAT(400, "桌台编号重复"),
        NAMESENSITIVE(400, "名称中含有特殊字符"),
        REMARKSENSITIVE(400, "备注中含有特殊字符"),
        ENABLEISNULL(400, "启用状态为空"),
        CHARGEWAYISNULL(400, "收费方式为空"),
        EXTENDSREASONISNULL(400, "原因为空"),
        EXTENDSREASONTYPEISNULL(400, "原因类型为空"),
        TYPEISNULL(400, "原因分类为空"),
        ORDERTYPEISNULL(400, "顺序为空"),
        TASTENAMEISNULL(400, "口味名称不能为空"),
        ISDIG(400, "固定金额/比例只能输入数字"),
        TABLENOISNULL(400, "桌台编号为空"),
        TABLENAMEPREISNULL(400, "桌台名称前缀为空"),
        TABLENOBEGINISNULL(400, "桌台起始编号为空"),
        TABLENOENDISNULL(400, "桌台结束编号为空"),
        SEATNUMISNULL(400, "桌位数为空"),
        SEATNUMILIMIT(400, "桌位数只能为数字，且长度为"),
        MAXSEATNUMISNULL(400,"最大桌位数为空"),
        MAXSEATNUMLIMIT(400,"最大桌位数只能为数字，且长度为"),
        MINCONSUMPTIONISNULL(400,"最低消费额为空"),
        MINCONSUMPTIONLIMIT(400,"最低消费额只能为数字，且长度为"),
        TABLENOLIMIT(400, "编号必须为数字，且长度限制为"),
        SALESMODEISNULL(400, "营业模式为空"),
        SEATTYPEISNULL(400, "桌位类型为空"),
        TABLETYPEISNULL(400, "桌台类型为空"),
        DININGAREAISNULL(400, "所属餐区为空"),
        SERVICEFEEISNULL(400, "服务费为空"),
        HASTABLES(400, "此餐区下有正在使用的桌台，无法删除"),
        SERVICEHASTABLES(400, "此服务费下有正在使用的桌台，无法删除"),
        SAVEFAIL(500, "操作失败"),
        QUERYFAIL(500, "查询失败"),
        CLONEFAIL(500, "克隆失败");
        public int code;
        public String message;

        private CODEMESSAGE(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
