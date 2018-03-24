package hzst.android.view;

import hzst.android.util.PhoneUtil;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
/**
 * 类库中大多自定义控件的基类。
 * 当子类控件设置行为方式、绑定参数等完毕后，必须调用{@link #showView()}来显示控件。
 * @author wt
 *
 */
abstract class BaseView extends LinearLayout{
	protected Context context;
	
	public PhoneUtil phoneUtil;
	public float density;
	
	
	public BaseView(Context context) {
		super(context);
		this.context = context;
		initBaseData();
	}
	
	public BaseView(Context context, AttributeSet attrs) {
		super(context,attrs);
		this.context = context;
		initBaseData();
	}
	/*
	 * 初始化一些常用的工具类
	 */
	private void initBaseData(){
		phoneUtil = new PhoneUtil(context);
		density = phoneUtil.getDensity();
	}
	
	protected abstract void initView();
	
	public void showView(){}

}
