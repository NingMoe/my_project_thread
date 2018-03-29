package controllers;

import com.google.inject.Inject;
import com.hht.exception.HhtRuntimeException;
import com.hht.utils.JsonUtil;
import common.Constant;
import org.apache.commons.lang3.StringUtils;
import play.data.FormFactory;
import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import utils.GZipUtils;

import java.util.Map;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 17/7/19 10:17
 */
public class ProBaseController extends Controller{

    @Inject
    protected FormFactory formFactory;
    @Inject
    private MessagesApi messagesApi;

    protected Messages messages;


    public Messages getMessages() {

        //Lang lang=Http.Context.current().lang();
        //Lang lang= lang();
        Lang lang= null;
        try {
            lang = lang();
        } catch (Exception e) {
            lang=Lang.forCode("zh-CN");
        }
        this.messages = new Messages(lang,messagesApi);
        return this.messages;
    }

    protected Map<String,Object> getDecoderParam() {
        Map<String, Object> map = null;
        try {
            Map<String,String> data = formFactory.form().bindFromRequest().get().getData();
            String requestMessage = data.get("requestMessage");
            if (!StringUtils.isEmpty(requestMessage)) {
                requestMessage = GZipUtils.decoderStr(requestMessage);
                map = JsonUtil.jsonToMap(requestMessage);
            }
        }catch(Exception e){
            throw new HhtRuntimeException(Constant.RESULT_FAIL,"解密失败");
        }
        return map;

    }
}
