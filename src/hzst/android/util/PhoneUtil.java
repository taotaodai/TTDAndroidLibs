package hzst.android.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 获取手机相关的参数，以及硬件、系统应用的调用。
 * 
 * @author wt
 *
 */
public class PhoneUtil {
	private Display display;
	private Context context;
	private TelephonyManager telephonyManager;
	
    /** 没有网络 */
    public static final int NETWORKTYPE_INVALID = 0;
    /** wap网络 */
    public static final int NETWORKTYPE_WAP = 1;
    /** 2G网络 */
    public static final int NETWORKTYPE_2G = 2;
    /** 3G和3G以上网络，或统称为快速网络 */
    public static final int NETWORKTYPE_3G = 3;
    /** wifi网络 */
    public static final int NETWORKTYPE_WIFI = 4;
	
	public PhoneUtil(Context context) {
		this.context = context;

		telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  

		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	/**
	 * 
	 * @return 设备密度
	 */
	public float getDensity() {
		DisplayMetrics metric = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);

		return metric.density;
	}

	@SuppressWarnings("deprecation")
	public int getWidth() {
		return display.getWidth();
	}

	@SuppressWarnings("deprecation")
	public int getHeight() {
		return display.getHeight();
	}
	
	public boolean hasInternetConnected(){
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo network = manager.getActiveNetworkInfo();
			if (network != null && network.isConnectedOrConnecting()) {
				return true;
			}
		}
		Toast.makeText(context, "请检查网络是否正常", Toast.LENGTH_SHORT).show();	
		return false;
	}
	
	/**
	 * 打开网络设置对话框
	 */
	public void openWirelessSet() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setTitle("网络设置")
				.setMessage("检查网络")
				.setPositiveButton("网络设置",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								Intent intent = new Intent(
										Settings.ACTION_WIFI_SETTINGS);
								context.startActivity(intent);
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});
		dialogBuilder.show();
	}
	/**
	 * 获取设备编号
	 * @return
	 */
	public String getDeviceId(){
		return telephonyManager.getDeviceId();
	}
	
	/**
	 * 打开软键盘
	 */
	public void openKeybord(EditText mEditText)
	{
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	/**
	 * 关闭键盘
	 */
	public void closeInput() {
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
			inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	public void closeKeybord(){
        View view = ((Activity) context).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
	}
	
    /**
     * 手机是否设置了静音
     * @return
     */
    public boolean isAudioSilent(){
    	AudioManager mAudioManager = (AudioManager)context.getSystemService(Activity.AUDIO_SERVICE); 
    	if(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
    		return true;
    	}
    	return false;
    }
	
	/**
	 * 获取网络状态，wifi,wap,2g,3g.
	 *
	 * @return int 网络状态
	 */
	@SuppressWarnings("deprecation")
	public int getNetWorkType() {
		int mNetWorkType = 0;
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			String type = networkInfo.getTypeName();

			if (type.equalsIgnoreCase("WIFI")) {
				mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
            	String proxyHost = android.net.Proxy.getDefaultHost();

            	mNetWorkType = TextUtils.isEmpty(proxyHost)
            			? (isFastMobileNetwork() ? NETWORKTYPE_3G : NETWORKTYPE_2G)
            			: NETWORKTYPE_WAP;
            }
		} else {
			mNetWorkType = NETWORKTYPE_INVALID;
		}

		return mNetWorkType;
	}
	@Deprecated
	public boolean isRssiGood(){
		switch (getNetWorkType()) {
		case NETWORKTYPE_INVALID:
			Toast.makeText(context, "当前网络不可用，请检查", Toast.LENGTH_SHORT);
			return false;
		}
		return true;
	}
	
	/**
     * 检测已连接网络
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();

    }
    
	/**
	 * 判断是否有网络连接(WiFi或移动数据)，若没有，则弹出网络设置对话框，返回false
	 */
	public boolean validateInternet() {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			openWirelessSet();
			return false;
		} else {
			NetworkInfo[] info = manager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		openWirelessSet();
		return false;
	}
    
	private boolean isFastMobileNetwork() {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
	        case TelephonyManager.NETWORK_TYPE_1xRTT:
	            return false; // ~ 50-100 kbps
	        case TelephonyManager.NETWORK_TYPE_CDMA:
	            return false; // ~ 14-64 kbps
	        case TelephonyManager.NETWORK_TYPE_EDGE:
	            return false; // ~ 50-100 kbps
	        case TelephonyManager.NETWORK_TYPE_EVDO_0:
	            return true; // ~ 400-1000 kbps
	        case TelephonyManager.NETWORK_TYPE_EVDO_A:
	            return true; // ~ 600-1400 kbps
	        case TelephonyManager.NETWORK_TYPE_GPRS:
	            return false; // ~ 100 kbps
	        case TelephonyManager.NETWORK_TYPE_HSDPA:
	            return true; // ~ 2-14 Mbps
	        case TelephonyManager.NETWORK_TYPE_HSPA:
	            return true; // ~ 700-1700 kbps
	        case TelephonyManager.NETWORK_TYPE_HSUPA:
	            return true; // ~ 1-23 Mbps
	        case TelephonyManager.NETWORK_TYPE_UMTS:
	            return true; // ~ 400-7000 kbps
	        case TelephonyManager.NETWORK_TYPE_EHRPD:
	            return true; // ~ 1-2 Mbps
	        case TelephonyManager.NETWORK_TYPE_EVDO_B:
	            return true; // ~ 5 Mbps
	        case TelephonyManager.NETWORK_TYPE_HSPAP:
	            return true; // ~ 10-20 Mbps
	        case TelephonyManager.NETWORK_TYPE_IDEN:
	            return false; // ~25 kbps
	        case TelephonyManager.NETWORK_TYPE_LTE:
	            return true; // ~ 10+ Mbps
	        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
	            return false;
	        default:
	            return false;
		}
	}
}
