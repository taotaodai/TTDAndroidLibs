package hzst.android.view;


import hzst.android.R;
import hzst.android.util.L;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 交替圆环进度条
 * @author Administrator
 *
 */
public class AlternatelyProgressBar extends View{
	/**
	 * 第一圈的颜色
	 */
	private int mFirstColor;
	/**
	 * 第二圈的颜色
	 */
	private int mSecondColor;
	/**
	 * 圈的宽度
	 */
	private int mCircleWidth;
	/**
	 * 画笔
	 */
	private Paint mPaint;
	/**
	 * 当前进度
	 */
	private int mProgress;

	/**
	 * 速度
	 */
	private int mSpeed;

	/**
	 * 是否应该开始下一个
	 */
	private boolean isNext = false;

	public AlternatelyProgressBar(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public AlternatelyProgressBar(Context context)
	{
		this(context, null);
	}

	/**
	 * 必要的初始化，获得一些自定义的值
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public AlternatelyProgressBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AlternatelyProgressBar, defStyle, 0);
		
		mFirstColor = a.getColor(R.styleable.AlternatelyProgressBar_firstColor, Color.GREEN);
		mSecondColor = a.getColor(R.styleable.AlternatelyProgressBar_secondColor, Color.RED);
		mCircleWidth = a.getDimensionPixelSize(R.styleable.AlternatelyProgressBar_circleWidth, (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
		mSpeed = a.getInt(R.styleable.AlternatelyProgressBar_speed, 20);

		a.recycle();
		mPaint = new Paint();
		// 绘图线程
		new Thread()
		{
			public void run()
			{
				while (true)
				{
					mProgress++;
					if (mProgress == 360)
					{
						mProgress = 0;
						if (!isNext)
							isNext = true;
						else
							isNext = false;
					}
					postInvalidate();
					try
					{
						Thread.sleep(mSpeed);
					} catch (InterruptedException e)
					{
						L.showLogInfo(L.TAG_EXCEPTION, e.toString());
					}
				}
			};
		}.start();

	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{

		int centre = getWidth() / 2; // 获取圆心的x坐标
		int radius = centre - mCircleWidth / 2;// 半径
		mPaint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
		mPaint.setAntiAlias(true); // 消除锯齿
		mPaint.setStyle(Paint.Style.STROKE); // 设置空心
		RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
		
		/*
		 * 交替圆环
		 */
		if (!isNext)
		{// 第一颜色的圈完整，第二颜色跑
			mPaint.setColor(mFirstColor); // 设置圆环的颜色
			canvas.drawCircle(centre, centre, radius, mPaint); // 画出圆环
			mPaint.setColor(mSecondColor); // 设置圆环的颜色
			canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
		} else
		{
			mPaint.setColor(mSecondColor); // 设置圆环的颜色
			canvas.drawCircle(centre, centre, radius, mPaint); // 画出圆环
			mPaint.setColor(mFirstColor); // 设置圆环的颜色
			canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
		}

	}

	public int getProgress() {
		return mProgress;
	}

	public void setProgress(int progress) {
		this.mProgress = progress;
	}
	
	

}
