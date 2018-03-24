package hzst.android.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 收集未捕捉到的Exception
 * 1-储存到本地；
 * 2-发送到服务器，只提供对外接口。
 * @author wt 2015-5-20
 *
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private Map<String, String> infos = new HashMap<String, String>();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private OnCrashListener callBack; 
    private String exString = "";
    private String crashPath;

    private static CrashHandler INSTANCE;
    
    private CrashHandler() {

    }

    public synchronized static CrashHandler getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CrashHandler();
        return INSTANCE;
    }
    /**
     * 
     * @param context
     * @param crashPath 崩溃日志保存路径，必须是全路径
     */
    public void init(Context context,String crashPath,OnCrashListener callBack) {
        mContext = context;
        this.crashPath = crashPath;
        this.callBack = callBack;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handlerException(ex) && mDefaultHandler != null) {
        	
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            	L.showLogInfo(L.TAG_EXCEPTION, e.toString());
            }
            callBack.exitApp();
        }
    }

    private boolean handlerException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "网络繁忙", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        collectDeviceInfo(mContext);
        getExString(ex);
        saveCrashInfo2File();
        
        callBack.sendLogToServer(exString);
        
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
        	L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            	L.showLogInfo(L.TAG_EXCEPTION, e.toString());
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\r\n");
        }

        sb.append(exString);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(crashPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(crashPath + fileName, true);
                String newLine = System.getProperty("line.separator");
                fos.write(sb.toString().getBytes());
                fos.write(newLine.getBytes());
                fos.write("\r\n".getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
        	L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        return null;
    }
    
    /**
     * 获取崩溃日志信息
     * @param ex
     */
    private void getExString(Throwable ex){
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        exString = writer.toString();
        L.showLogInfo(L.TAG_EXCEPTION, exString);
    }
    
    public interface OnCrashListener{
    	void exitApp();
    	void sendLogToServer(String exString);
    }
}
