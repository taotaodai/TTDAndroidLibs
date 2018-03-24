package hzst.android.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import hzst.android.R;
/**
 * 加载等待对话框
 * @author Administrator
 *
 */
public class LoadingDialog extends Dialog{
	private Context context;
	
	private View rootView;
	private TextView tvHint;
	
	public static final int LAYOUT_GENERAL = 1;
	public static final int LAYOUT_FLOWER = 2;
	
	public LoadingDialog(final Context context) {
		super(context,R.style.dialog_transparency);
		this.context = context;
	}
	/**
	 * 生成不同风格的View
	 * @param layoutType
	 */
	public void setLayout(int layoutType){
		LayoutInflater inflater = LayoutInflater.from(context);

		switch (layoutType) {
		case LAYOUT_GENERAL:
			rootView = inflater.inflate(R.layout.view_dialog_loading, null);
			ImageView ivNormal = (ImageView) rootView.findViewById(R.id.iv_loading);
			Animation animNormal = AnimationUtils.loadAnimation(context, R.anim.anim_loading_rotation);
			ivNormal.startAnimation(animNormal);
			break;
		case LAYOUT_FLOWER:
			rootView = inflater.inflate(R.layout.view_dialog_loading_flower, null);
			ImageView ivFlower = (ImageView) rootView.findViewById(R.id.iv_loading);
			Animation animFlower = AnimationUtils.loadAnimation(context, R.anim.anim_loading_rotation);
			// 使用ImageView显示动画  
			ivFlower.startAnimation(animFlower);
			break;
		}
		initView();
	}
	
	private void initView(){
		tvHint = (TextView) rootView.findViewById(R.id.tv_hint);
		setContentView(rootView);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		
		setOnKeyListener(new DialogInterface.OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK){
					((Activity)context).finish();
					return true;
				}
				return false;
			}
		});
	}
	
	public void setHint(String hint){
		tvHint.setText(hint);
	}

}
