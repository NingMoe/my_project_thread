package utils;

import com.hht.utils.AssertUtil;
import common.Constant;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by WuJiaWen on 2017/3/9.
 */
public class ValidBatchUtils {

    /**
     * 批量校验非空参数
     * */
    public static void isNotEmpty(Map map,String ... params){
        Arrays.asList(params).stream().forEach(param -> {
            AssertUtil.assertEmpty(map.get(param), Constant.RESULT_VALID_FAIL,param+"不能为空！");
        });
    }
}
