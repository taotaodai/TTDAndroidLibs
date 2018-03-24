package hzst.android.annotation;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import hzst.android.util.L;
/**
 * 标签解析器
 * @author wt
 *
 */
public class EMAnnotationParser {
    @SuppressWarnings("unchecked")
    public static void injectActivity(Activity activity) {
        if (null == activity) {
            return;
        }
        Class<Activity> activityClass = (Class<Activity>) activity.getClass();
        if (isEMLayoutBinder(activityClass)) {
            EMLayoutBinder layout = activityClass.getAnnotation(EMLayoutBinder.class);
            activity.setContentView(layout.value());
        }
        View decorView = activity.getWindow().getDecorView();

        initViews(activityClass.getDeclaredFields(), decorView, activity);

        initOnClick(activityClass.getMethods(), decorView, activity);
    }

    private static boolean isEMLayoutBinder(Class<?> c) {
        return c.isAnnotationPresent(EMLayoutBinder.class);
    }
    private static boolean isEMViewBinder(Field filed) {
        return filed.isAnnotationPresent(EMViewBinder.class);
    }
    private static boolean isEMOnClickBinder(Method method) {
        return method.isAnnotationPresent(EMOnClickBinder.class);
    }

    private static void initViews(Field[] fields, View view, Object object) {
        View view1;
        for (Field field : fields) {
            if(isEMViewBinder(field)) {
                EMViewBinder emView = field.getAnnotation(EMViewBinder.class);
                view1 = view.findViewById(emView.value());
                if(null != view1) {
                    try {
                        field.setAccessible(true);
                        field.set(object, view1);
                    } catch (Exception e) {
                        L.showLogInfo(L.TAG_EXCEPTION, e.toString());
                    }
                }
            }
        }
    }

    private static void initOnClick(Method[] allMethod, View root, Object object) {
        for (Method method : allMethod) {
            if (isEMOnClickBinder(method)) {
                EMOnClickBinder onClick = method.getAnnotation(EMOnClickBinder.class);
                MyOnClickListener click = new MyOnClickListener(method, object);
                int[] ids = onClick.value();
                for (int id : ids) {
                    root.findViewById(id).setOnClickListener(click);
                }
            }
        }
    }

    static class MyOnClickListener implements View.OnClickListener {
        private Method mMethod;
        private Object mReceiver;

        public MyOnClickListener(Method method, Object receiver) {
            mMethod = method;
            mReceiver = receiver;
        }

        @Override
        public void onClick(View v) {
            try {
                mMethod.setAccessible(true);
                mMethod.invoke(mReceiver, v);
            } catch (Exception e) {
                L.showLogInfo(L.TAG_EXCEPTION, e.toString());
            }
        }
    }
}
