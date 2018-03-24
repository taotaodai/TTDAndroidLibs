package hzst.android.util;

import hzst.android.BaseApplication;
import hzst.android.R;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
/**
 * 服务器地址设置
 * 使用时请先在{@link Application} 中调用{@link #initHost(SharedPreferences, String)}
 * 并且Application需要继承{@link BaseApplication}
 * @author wt
 *
 */
public class ServerAddressManager {
	private static SharedPreferences preferences;
	private static String defaultHost;
	private static ServerAddressManager instance;
	
	private EditText etIp;
	
	/**
	 * 首次使用{@link ServerAddressManager}时调用该方法获取实例
	 * @param application
	 * @return
	 */
    public static ServerAddressManager getInstance(Application application){
    	if(instance == null){
    		instance = new ServerAddressManager();
    		
        	if(!(application instanceof BaseApplication)){
        		Toast.makeText(application, "请让你的Application继承BaseApplication", Toast.LENGTH_SHORT).show();
        	}
    	}
    	return instance;
    }
    
    public static ServerAddressManager getInstance(){
    	if(instance == null){
    		instance = new ServerAddressManager();
    	}
    	return instance;
    }
    /**
     * 进入应用时，初始化服务器地址
     * @param preferences
     * @param defaultHost
     * @return
     */
	public String initHost(SharedPreferences preferences,String defaultHost){
		ServerAddressManager.preferences = preferences;
		ServerAddressManager.defaultHost = defaultHost;
		
    	String ip = preferences.getString("host", "");
    	if("".equals(ip)){
    		ip = defaultHost;
    	}
    	BaseApplication.HOST = ip;
    	return ip;
    }
    /**
     * 打开设置服务器地址窗口
     * @param context
     */
    public void openHostSetting(final Context context){
    	BackgroundUtil backgroundUtil = new BackgroundUtil(context);
    	PhoneUtil phoneUtil = new PhoneUtil(context);
		View view = LayoutInflater.from(context).inflate(R.layout.view_host_setting, null);
		etIp = (EditText) view.findViewById(R.id.et_ip);
		etIp.setText(initHost(preferences, defaultHost));
		RelativeLayout rlayReset = (RelativeLayout) view.findViewById(R.id.rlay_reset);
		backgroundUtil.setPressColor(rlayReset, R.color.bg_keynote, phoneUtil.getDensity()*5);
		rlayReset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etIp.setText(defaultHost);
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("设置服务器地址");
		builder.setView(view);
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String host = etIp.getText().toString();
				Editor editor = preferences.edit();
				editor.putString("host", host);
				editor.commit();
				BaseApplication.HOST = host;
				Toast.makeText(context, "设置成功", Toast.LENGTH_SHORT).show();
			}
		});
		builder.create().show();
    }
}
