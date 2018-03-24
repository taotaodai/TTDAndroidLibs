package hzst.android.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.WindowManager;

/**
 * 对控件进行颜色、透明度、形状等外观上的一些操作。
 * @author wt
 *
 */
public class BackgroundUtil {
	
	private Context context;
	private static final int INCREMENT = 15;//色值的默认增量
	
	public BackgroundUtil(Context context) {
		this.context = context;
	}
	
	/**
	 * 添加控件按下时的背景色
	 * @param view
	 * @param colorRes
	 * @param increment
	 */
	public void setPressColor(View view,int colorRes,int increment,float radius){
		view.setBackground(addStateDrawable(colorRes, processColorDepth(colorRes, increment), -1,radius));
	}
	
	public void setPressColor(View view,int colorRes,float radius){
		view.setBackground(addStateDrawable(colorRes, processColorDepth(colorRes, INCREMENT), -1, radius));
	}
	/**
	 * 一般用于没有默认背景色的控件(以父控件的背景色为基准色)
	 */
	public void setPressColorWithoutDefalut(View view,int colorRes,float radius){
		view.setBackground(addStateDrawable(-1, processColorDepth(colorRes, INCREMENT), -1,radius));
	}
	public void setPressColorWithoutDefalut(View view,int colorRes,int increment,float radius){
		view.setBackground(addStateDrawable(-1, processColorDepth(colorRes, INCREMENT), -1,radius));
	}
	/**
	 * 设置屏幕背景透明度
	 */
	public void setWindowAlpha(float alpha){
		WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
		lp.alpha = alpha;
		((Activity)context).getWindow().setAttributes(lp);
	}

	/**
	 * 
	 * @param colorRes
	 * @param increment 颜色深浅变化值，负数为变浅
	 */
	private int processColorDepth(int colorRes,int increment){
		int color = context.getResources().getColor(colorRes);
		String hexRGB = "";
		String hex = Integer.toHexString(color);
		
		/*
		 * 取六位RGB码
		 */
		if(hex.length() == 6){
			hexRGB = hex;
		}else if(hex.length() == 8){
			hexRGB = hex.substring(2);
		}else{
			return color;
		}
		/*
		 * 将默认背景色的RGB码转化成十六进制字符串，
		 * 分别把R、G、B的值减去加深量，最后重新拼成十六进制字符串并转换成十进制
		 */
		StringBuffer sb = new StringBuffer("ff");
		for (int i = 0; i < 3; i++) {
			int temp = Integer.parseInt(hexRGB.substring(i*2,(i+1)*2), 16);
			if(temp >= increment){
				temp -= increment;
			}
			String tempHex = Integer.toHexString(temp);
			/*
			 * 在一位十六进制数前补0变成两位
			 */
			if(tempHex.length() == 1){
				tempHex = "0"+tempHex;
			}
			sb.append(tempHex);
		}
		
		return (int) Long.parseLong(sb.toString(), 16);
	}
	
	/**
	 * 按下抬起改变背景色
	 * @param normalRes
	 * @param pressedColor
	 * @param idFocused
	 * @return
	 */
	private StateListDrawable addStateDrawable(int normalRes, int pressedColor, int idFocused,float radius) {
		StateListDrawable sd = new StateListDrawable();  
//		Drawable normal = normalRes == -1 ? null : context.getResources().getDrawable(normalRes);    
//		Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
		Drawable focus = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);
//		ColorDrawable pressed = new ColorDrawable(pressedColor);
		
		GradientDrawable normal = new GradientDrawable();
		normal.setColor(normalRes == -1 ? Color.TRANSPARENT : context.getResources().getColor(normalRes));
		normal.setCornerRadius(radius);
		GradientDrawable pressed = new GradientDrawable();
		pressed.setColor(pressedColor);
		pressed.setCornerRadius(radius);
		
		//注意该处的顺序，只要有一个状态与之相配，背景就会被换掉    
		//所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了    
		sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);    
		sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);    
		sd.addState(new int[]{android.R.attr.state_focused}, focus);    
		sd.addState(new int[]{android.R.attr.state_pressed}, pressed);    
		sd.addState(new int[]{android.R.attr.state_enabled}, normal);    
		sd.addState(new int[]{}, normal);    
		
//		sd.addState(View.PRESSED_ENABLED_STATE_SET, pressed);    
//		sd.addState(View.ENABLED_FOCUSED_STATE_SET, focus);    
//		sd.addState(View.ENABLED_STATE_SET, normal);    
//		sd.addState(View.FOCUSED_STATE_SET, focus);    
//		sd.addState(View.EMPTY_STATE_SET, normal);    

		return sd;
	}
	
}
