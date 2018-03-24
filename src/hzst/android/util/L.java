package hzst.android.util;

import android.util.Log;

public class L {
	public static final String TAG_NET_REQUEST = "NET_REQUEST";//发起网络请求
	public static final String TAG_SOCKET = "SOCKET";//
	public static final String TAG_NET_RESPONSE = "NET_RESPONSE";//服务器响应
	public static final String TAG_EXCEPTION = "EXCEPTION";//异常
	public static final String TAG_CRASH = "CRASH";
	public static final String TAG_OTHER_INFO = "OTHER_INFO";

	/** Debug输出Log日志 **/
	public static void showLogInfo(String tag, String log) {
		StackTraceElement element = new Exception().getStackTrace()[1];//获取调用方法的信息
		String className = element.getClassName();
		int lineNumber = element.getLineNumber();
		Log.i(tag, className+"---"+lineNumber+"行");//便于跟踪，类名和行号
		if(log != null){
			Log.i(tag, log);
		}
	}
}

