package hzst.android.util;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.List;
import java.util.Map;

/**
 * Created by wt on 2017/4/11.
 */
public class MobCookieManager {//转载请标明出处：http://blog.csdn.net/goldenfish1919/article/details/46890245

    private MobCookieManager() {
    }

    /**
     * 应用启动的时候调用，参考：{@link CookieManager#getInstance CookieManager.getInstance()}
     */
    public static void init(Context context) {
        CookieSyncManager.createInstance(context);
    }

    /**
     * 存储cookie
     *
     * @param headerFields
     */
    public static void setCookies(Map<String, List<String>> headerFields) {
        if (null == headerFields) {
            return;
        }
        List<String> cookies = headerFields.get("Set-Cookie");
        if (null == cookies) {
            return;
        }
        for (String cookie : cookies) {
            setCookie(cookie);
        }

    }
    private static void setCookie(String cookie) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie("cookie", cookie);
    }

    public static String getCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookie = cookieManager.getCookie("cookie");
        if (cookie != null) {
            return cookie;
        } else {
            return "";
        }
    }

}
