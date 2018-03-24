package hzst.android.view;


import hzst.android.R;
import hzst.android.util.BackgroundUtil;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 顶部导航栏
 *
 * @author Administrator
 */
public class TopNavigationBar extends LinearLayout {
    private View view;
    private RelativeLayout rlayLeft;
    private RelativeLayout rlayRight;
    private TextView tvTitle;
    private ImageView ivLeft;
    private ImageView ivRight;
    private TextView tvRight;

    private TypedArray array;
    private int leftBtnType;
    private int rightBtnType;
    private int keynoteColor;
    private Drawable backgroundRes = null;
    private int leftPressColor;
    private int rightPressColor;
    private String title;

    private Context context;
    private Activity activity;

    private OnClickLeftBtnListener onClickLeftBtnListener;
    private OnClickRightBtnListener onClickRightBtnListener;

    /*
     * 左侧按钮的类型
     */
    private static final int BTN_TYPE_NONE = -1;
    private static final int BTN_TYPE_BACK = 0;
    private static final int BTN_TYPE_CROSS = 1;
//	private static final int BTN_TYPE_MORE = 2;

    private static final int RGB_INCREMENT = 15;//RGB加深量(按钮按下的颜色)
    private static final int[] leftImg = {R.drawable.ic_return, R.drawable.btn_close};
    private static final int[] rightImg = {R.drawable.ic_add, R.drawable.ic_search, R.drawable.ic_more};

    public void setTitle(String title) {
        tvTitle.setText(title);
        postInvalidate();
    }

    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
        postInvalidate();
    }

    public void setTitleColor(int color) {
        tvTitle.setTextColor(context.getResources().getColor(color));
    }

    public TextView getRightTextView() {
        return tvRight;
    }

    public void setOnClickLeftBtnListener(
            OnClickLeftBtnListener onClickLeftBtnListener) {
        this.onClickLeftBtnListener = onClickLeftBtnListener;
    }

    public void setOnClickRightBtnListener(
            OnClickRightBtnListener onClickRightBtnListener) {
        this.onClickRightBtnListener = onClickRightBtnListener;
        bindRightOnClick();
    }

    public TopNavigationBar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        array = context.obtainStyledAttributes(attrs, R.styleable.TopNavigationBar);
        this.context = context;
        this.activity = (Activity) context;

        initData();
        initView();
        initEvents();

        array.recycle();

    }

    private void initData() {
        title = array.getString(R.styleable.TopNavigationBar_title_top);
        leftBtnType = array.getInt(R.styleable.TopNavigationBar_btn_left, BTN_TYPE_NONE);
        rightBtnType = array.getInt(R.styleable.TopNavigationBar_btn_right, BTN_TYPE_NONE);
        keynoteColor = array.getResourceId(R.styleable.TopNavigationBar_keynote_color, R.color.bg_keynote);
        backgroundRes = array.getDrawable(R.styleable.TopNavigationBar_background_res);
        leftPressColor = array.getResourceId(R.styleable.TopNavigationBar_left_press_color, R.color.bg_keynote);
        rightPressColor = array.getResourceId(R.styleable.TopNavigationBar_right_press_color, R.color.bg_keynote);
//		keynoteColor = array.getColor(R.styleable.TopNavigationBar_keynote_color, R.color.bg_keynote);

    }

    private void initView() {
        view = inflate(context, R.layout.custom_top_navigation, null);
        if (backgroundRes == null) {
            view.setBackgroundColor(context.getResources().getColor(keynoteColor));
        }else {
            view.setBackgroundDrawable(backgroundRes);
        }


        rlayLeft = (RelativeLayout) view.findViewById(R.id.rlay_left);
        rlayRight = (RelativeLayout) view.findViewById(R.id.rlay_right);
        tvTitle = (TextView) view.findViewById(R.id.tv_title_top);
        tvTitle.setText(title);
        ivLeft = (ImageView) view.findViewById(R.id.iv_top_left);
        ivRight = (ImageView) view.findViewById(R.id.iv_top_right);
        tvRight = (TextView) view.findViewById(R.id.tv_top_right);

        addView(view);
    }

    private void initEvents() {
        /*
         * 左侧按钮事件
		 */
        if (leftBtnType == BTN_TYPE_NONE) {
            rlayLeft.setVisibility(View.INVISIBLE);
        } else {
            rlayLeft.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    switch (leftBtnType) {
                        case BTN_TYPE_BACK:
                        case BTN_TYPE_CROSS:
                            if (onClickLeftBtnListener != null) {
                                onClickLeftBtnListener.clickLeftBtn();
                            }
                            activity.finish();
                            break;
                        default:
                            if (onClickLeftBtnListener != null) {
                                onClickLeftBtnListener.clickLeftBtn();
                            }
                            break;

                    }
                }
            });
            ivLeft.setImageResource(leftImg[leftBtnType]);
        }
        /*
         * 右侧按钮事件
		 */
        if (rightBtnType == BTN_TYPE_NONE) {
            rlayRight.setVisibility(View.INVISIBLE);
        } else {
            bindRightOnClick();
            ivRight.setImageResource(rightImg[rightBtnType]);
        }
        initButtons();
    }

    private void initButtons() {
        BackgroundUtil backgroundUtil = new BackgroundUtil(context);
        if (backgroundRes == null) {
            backgroundUtil.setPressColor(rlayLeft, keynoteColor, RGB_INCREMENT, 0);
            backgroundUtil.setPressColor(rlayRight, keynoteColor, RGB_INCREMENT, 0);
        } else {
            backgroundUtil.setPressColorWithoutDefalut(rlayLeft, leftPressColor, 0);
            backgroundUtil.setPressColorWithoutDefalut(rlayRight, rightPressColor, 0);
        }

    }

    public void setKeynoteColor(int keynoteColor) {
        this.keynoteColor = keynoteColor;
        if (backgroundRes == null) {
            view.setBackgroundColor(context.getResources().getColor(keynoteColor));
        } else {
            view.setBackgroundDrawable(backgroundRes);
        }

        initButtons();
    }

    private void bindRightOnClick() {
        rlayRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onClickRightBtnListener != null) {
                    onClickRightBtnListener.clickRightBtn();
                }
            }
        });
    }

    public void setLeftBtnRes(int resId) {
        ivLeft.setImageResource(resId);
        rlayLeft.setVisibility(View.VISIBLE);
        rlayLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onClickLeftBtnListener != null) {
                    onClickLeftBtnListener.clickLeftBtn();
                }
            }
        });
    }

    public void setLeftBtnVisible(boolean visible){
        rlayLeft.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setRightBtnRes(int resId) {
        ivRight.setImageResource(resId);
        rlayRight.setVisibility(View.VISIBLE);
        rlayRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onClickRightBtnListener != null) {
                    onClickRightBtnListener.clickRightBtn();
                }
            }
        });
    }

    /**
     * 只有设置文本的方法，其他的设置可以直接getRightTextView
     * 获取控件进行设置。
     *
     * @param text
     */
    public void setRightBtnText(String text) {
        showRightBtn();
        tvRight.setText(text);
    }

    public void hideRightBtn() {
        rlayRight.setVisibility(View.INVISIBLE);
    }

    public void showRightBtn() {
        rlayRight.setVisibility(View.VISIBLE);
    }

    public interface OnClickLeftBtnListener {
        void clickLeftBtn();
    }

    public interface OnClickRightBtnListener {
        void clickRightBtn();
    }

}
