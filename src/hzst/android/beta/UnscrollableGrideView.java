package hzst.android.beta;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class UnscrollableGrideView extends GridView{
	
	public UnscrollableGrideView(Context context) {
		super(context);
	}
	public UnscrollableGrideView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UnscrollableGrideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
		     
	
	@Override 
	public boolean onTouchEvent(MotionEvent event) { 
		//重写的onTouchEvent回调方法 
		switch(event.getAction()){ 
			case MotionEvent.ACTION_DOWN: 
				return super.onTouchEvent(event); 
			case MotionEvent.ACTION_MOVE: 
				break; 
			case MotionEvent.ACTION_UP: 
				return super.onTouchEvent(event); 
		} 
		//注意：返回值是false 
		return false; 
	} 
}
