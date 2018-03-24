package hzst.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.CheckBox;

import hzst.android.R;

/**
 * Created by wt on 2017/6/20.
 */
public class MyCheckBox extends CheckBox {
    private int mDrawableSize;// xml文件中设置的大小
    private Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;

    public MyCheckBox(Context context) {
        this(context, null, 0);
    }

    public MyCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyCheckBox);

        mDrawableSize = a.getDimensionPixelSize(R.styleable.MyCheckBox_drawableSizeCb, 50);
        drawableTop = a.getDrawable(R.styleable.MyCheckBox_drawableTopCb);
        drawableRight = a.getDrawable(R.styleable.MyCheckBox_drawableBottomCb);
        drawableBottom = a.getDrawable(R.styleable.MyCheckBox_drawableRightCb);
        drawableLeft = a.getDrawable(R.styleable.MyCheckBox_drawableLeftCb);

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

    public void setDrawableTop(Drawable drawableTop) {
        this.drawableTop = drawableTop;
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft,drawableTop,drawableRight,drawableBottom);
        invalidate();
    }
}
