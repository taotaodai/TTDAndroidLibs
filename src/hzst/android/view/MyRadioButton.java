package hzst.android.view;

import hzst.android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;


/**
 * 为了解决RadioButton中的 drawableTop、drawableRight、drawableBottom、drawableLeft
 * 图片大小无法设置的问题。新增drawableSize属性，设置宽高
 * 
 * @author wt
 *
 */
public class MyRadioButton extends RadioButton {

	private int mDrawableSize;// xml文件中设置的大小

	public MyRadioButton(Context context) {
		this(context, null, 0);
	}

	public MyRadioButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MyRadioButton);
		
		mDrawableSize = a.getDimensionPixelSize(R.styleable.MyRadioButton_drawableSizeRb, 50);
		drawableTop = a.getDrawable(R.styleable.MyRadioButton_drawableTop);
		drawableRight = a.getDrawable(R.styleable.MyRadioButton_drawableBottom);
		drawableBottom = a.getDrawable(R.styleable.MyRadioButton_drawableRight);
		drawableLeft = a.getDrawable(R.styleable.MyRadioButton_drawableLeft);

		a.recycle();

		setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop,
				drawableRight, drawableBottom);

	}

	public void setCompoundDrawablesWithIntrinsicBounds(Drawable left,
			Drawable top, Drawable right, Drawable bottom) {

		if (left != null) {
			left.setBounds(0, 0, mDrawableSize, mDrawableSize);
		}
		if (right != null) {
			right.setBounds(0, 0, mDrawableSize, mDrawableSize);
		}
		if (top != null) {
			top.setBounds(0, 0, mDrawableSize, mDrawableSize);
		}
		if (bottom != null) {
			bottom.setBounds(0, 0, mDrawableSize, mDrawableSize);
		}
		setCompoundDrawables(left, top, right, bottom);
	}

}
