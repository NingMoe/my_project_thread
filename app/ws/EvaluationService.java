package ws;

import com.google.inject.ImplementedBy;
import models.ProEliteTestRecord;
import org.apache.commons.codec.binary.Base64;
import ws.Impl.EvaluationServiceImpl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/19 09:55
 */
@ImplementedBy(EvaluationServiceImpl.class)
public interface EvaluationService {
    //用户名
    public static final String APP_KEY = "hidilao-prod";
    //密码
    public static final String SECRET_KEY = "C01C3273-54FE-4A75-BE44-2B777AD85834";

    public static final String SESSION_ID = "7199";
    //报告唯一标识
    public static final String REPORTUID = "B45A2967-CA14-4124-BF23-1F8C631DB9F5";
    //常模
    public static final String[] NORMIDS =  new String[]{"529","527","526","528"};

    String sendTestPerson(List<Map<String,Object>> list) throws IOException;

    String getTestRecord(List<ProEliteTestRecord> list) throws IOException;

    public List<ProEliteTestRecord> getTestEnd(List<ProEliteTestRecord> list) throws IOException;
}
