package utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/15 0015.
 */
public class BeanToMapUtils {

    /**
     * 把bean转化为map
     *
     * @param bean
     */
    public static Map<String, Object> fromBeanToMap(Object bean) {
        Map<String, Object> result = null;
        try {
            if (bean != null) {
                result = new HashMap<>();
                Class cls = bean.getClass();
                Field[] fields = cls.getDeclaredFields();
                if (fields != null) {
                    for (int i = 0; i < fields.length; i++) {
                        Field f = fields[i];
                        f.setAccessible(true);
                        result.put(f.getName(), f.get(bean));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
