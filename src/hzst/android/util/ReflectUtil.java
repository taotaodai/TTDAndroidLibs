package hzst.android.util;

import java.lang.reflect.Method;

/**
 *
 * Created by wt on 2016/8/4.
 */
public class ReflectUtil {
    private static Method getGetMethod(Class objectClass, String fieldName) {
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        try {
            return objectClass.getMethod(sb.toString());
        } catch (Exception e) {
        }
        return null;
    }
    public static Object invokeGet(Object o, String fieldName) throws Exception{
        Method method = getGetMethod(o.getClass(), fieldName);

        return method.invoke(o, new Object[0]);
    }
}
