package common;

import play.Configuration;

/**
 * <b>常量</b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/14 17:08
 */
public class Constant {

    //成功
    public static final Integer INTER_SUCCESS = 1;

    //失败
    public static final Integer INTER_FAIL = 0;

    // 对应外系统的成功
    public static final Integer SUCCESS = 0;

    // 对应外系统的失败
    public static final Integer FAIL = -1;

    //参数为空
    public static final Integer PARAM_NULL = -3;

    //金鹰池人数超出
    public static final Integer OVER_TOP = 2;

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


    public static final String REQ_SUCCESS = "请求成功";

    public static final String REQ_FAIL = "请求成功，数据为空";




    //费用测评节点
    public static final String COST_NODE_TEST ="01";
    //费用大堂班
    public static final String COST_NODE_LOBBY ="02";
    //费用经理班通过
    public static final String COST_NODE_MANAGER_PASS ="03";
    //费用经理班未通过
    public static final String COST_NODE_MANAGER_NOPASS ="04";


    public static final int RESULT_SUCCESS = 200;
    public static final int RESULT_WAIT_FOR_DO = 300;
    public static final int RESULT_FAIL = 500;
    public static final int RESULT_VALID_FAIL = 400;
    public static final String SUCCESS_MESSAGE="success";
    public static final String FAIL_MESSAGE="fail";


    //考试系统私钥
    public static final String EVALUATIONSECURET="sfsfwe89s7fs6df987se";

    public final static String CONSUMERSEC="ba58fd73c71db697ab5e";

    //测评系统私钥
    public static final String TESTSECURET="jlskjfdwoud2gl4jlsdf";

    //静态资源访问路径
    public static final String STATICADDRESS = Configuration.root().getString("static.address");
    //host
    public static final String VISITADDRESS = Configuration.root().getString("visit.address") + STATICADDRESS;
    //文件上传总路径
    public static final String UPLOADADDRESS = Configuration.root().getString("uploadFile.address");
    //模板、导出错误访问路径
    public static final String DOWNLOADADDRESS = VISITADDRESS + "/download";
    //图片访问路径
    public static final String RESADDRESS = VISITADDRESS;
    //excel上传的本地路径
    public static final String EXCELADDRESS = Configuration.root().getString("excel.address");

    public static final String DESTACCESSKEY = "LTAI2I65qtzofZLM";
    public static final String DESTSECRETKEY = "v7V2M3vTxEdg94BHCq3azTMGJG90oM";
    public static final String BUCKETNAME = "c2b";
    public static final  String SYSTEMID="SQwZZbNhPGPPNuasF";
}
