package hzst.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;
/**
 * 自动换行的RadioGroup
 * 再也不用担心RadioButton不能多行单选了
 * @author Administrator
 *
 */
public class FlowRadioGroup extends RadioGroup {
    
    public FlowRadioGroup(Context context) {
        super(context);
    }
 
    public FlowRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();
        int x = 0;
        int y = 0;
        int row = 0;

        for (int index = 0; index < childCount; index++) {
            final View child = getChildAt(index);
            
            if (child.getVisibility() != View.GONE) {
                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                // 此处增加onlayout中的换行判断，用于计算所需的高度
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                x += width;
                y = row * height + height;
                if (x > maxWidth) {
                    x = width;
                    row++;
                    y = row * height + height;
                }
            }
        }
 
        // 设置容器所需的宽度和高度
        setMeasuredDimension(maxWidth, y);
 
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        int maxWidth = r - l;
        int x = 0;
        int y = 0;
        int row = 0;
        for (int i = 0; i < childCount; i++) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != View.GONE) {
            	LayoutParams params = (LayoutParams) child.getLayoutParams();
            	int layoutWidth = params.width;
            	int width = 0;
            	int height = 0;
            	if(layoutWidth > 0){
            		width = layoutWidth;
            	}else if(layoutWidth == LayoutParams.MATCH_PARENT){
            		width = maxWidth - x;
            		if(width == 0){
            			width = maxWidth;
            		}
            	}else if(layoutWidth == LayoutParams.WRAP_CONTENT){
            		width = child.getMeasuredWidth();
            	}
//            	width = child.getMeasuredWidth();
            	height = child.getMeasuredHeight();
                x += width;
                y = row * height + height;
                if (x > maxWidth) {
                    x = width;
                    row++;
                    y = row * height + height;
                }
                child.layout(x - width, y - height, x, y);
                System.out.println();
            }
        }
    }
}