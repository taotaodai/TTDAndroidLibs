package hzst.android;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import java.util.LinkedList;
import java.util.List;

import hzst.android.util.L;
/**
 * 
 * @author wt
 *
 */
public class BaseApplication extends Application{
	public static String HOST;
	public SharedPreferences preferences;
    protected static List<Activity> mList = new LinkedList<Activity>();
    protected static BaseApplication instance;
	
    @Override
    public void onCreate() {
    	super.onCreate();
    }
    
    public synchronized static BaseApplication getInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance; 
    }
    public void addActivity(Activity activity) {
        mList.add(activity); 
    }
    public void exit() {
        try { 
            for (Activity activity : mList) {
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) {
        	L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        } finally {
            System.exit(0); 
        }
    }

    /**
     * 在子类{@link #onCreate()}中主动调用，用来初始化数据
     */
    protected void initBaseData(){
    	preferences = getSharedPreferences(getApplicationContext().getPackageName(), Activity.MODE_PRIVATE);
    }

}	
