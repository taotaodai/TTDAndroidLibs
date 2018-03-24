package hzst.android.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfoManager<T> {
	private SharedPreferences preferences;
	private SharedPreferencesUtil preferencesUtil;
	private T user;
	
	public UserInfoManager(Context context) {
		preferences = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
		preferencesUtil = new SharedPreferencesUtil(context, preferences);
	}
	
	public void saveUserInfo(T user){
		try {
			preferencesUtil.writeObj(user, "userInfo");
		} catch (Throwable e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public T getUserInfo(){
		if(user == null){
			user = (T) preferencesUtil.readObj("userInfo");
		}
		return user;
	}

}
