package hzst.android;

import hzst.android.util.PhoneUtil;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
/**
 * Activity基类
 * @author Administrator
 *
 */
public abstract class BaseActivity extends FragmentActivity {
	protected Context mContext = null;
	
	protected PhoneUtil phoneUtil;
	protected float density;// 设备密度
	protected int width;//设备屏幕宽度，广告栏可以用该宽度
	protected int height;
	
	protected WindowManager.LayoutParams windowParams;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		
		phoneUtil = new PhoneUtil(this);
		density = phoneUtil.getDensity();
		width = phoneUtil.getWidth();
		windowParams = getWindow().getAttributes();
		
	}
		
	protected abstract void initBaseData();
	protected abstract void initView();
	protected abstract void initEvents();

}
