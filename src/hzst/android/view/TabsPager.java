package hzst.android.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hzst.android.R;
import hzst.android.adapter.MyFragmentAdapter;
import hzst.android.util.PhoneUtil;

/**
 * 左右滑动页签，页签从左到右按顺序排列，页签数量较多时不适合使用。请使用{@link AdaptiveTabsPager}
 * 使用时必须调用 {@link #setFragmentList(List)} {@link #setTabNames(String[])}
 * @author wt
 * 
 */
public class TabsPager extends BaseView{
	private View view;
	private ViewPager vp;
	private LinearLayout llayTabs;
	private ImageView cursor;
	private List<TextView> tabs;
	private List<Fragment> fragmentList;
	private MyOnPageChangeListener pcListener;
	private MyFragmentAdapter adapter;
	
	private int tabColorSelected;
	private int tabSize;
	private int textSize = 16;
	private int textColor;
	private int textColorSelected;
	private int cursorRes;
	private boolean hasCursor;
	private boolean hasTabColor = false;
	private String[] tabNames;
	
	public TabsPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.SlideTabsView);
		
//		tabSize = a.getInteger(R.styleable.SlideTabsView_tabSize, 0);
		textColor = a.getResourceId(R.styleable.SlideTabsView_text_color, R.color.bg_gray2);
		textColorSelected = a.getColor(R.styleable.SlideTabsView_text_color_selected, R.color.bg_yellow);
		cursorRes = a.getResourceId(R.styleable.SlideTabsView_cursor_res, R.drawable.bar_yellow);
		hasCursor = a.getBoolean(R.styleable.SlideTabsView_has_cusor, true);
		tabColorSelected = a.getResourceId(R.styleable.SlideTabsView_tab_color, -1);
		if(tabColorSelected != -1){
			hasTabColor = true;
		}
		a.recycle();
		
		initView();
	}
	
	public ImageView getCursor() {
		return cursor;
	}

	public void setCursor(ImageView cursor) {
		this.cursor = cursor;
	}

	public void setTabs(List<TextView> tabs) {
		this.tabs = tabs;
	}

	public void setTabColorSelected(int tabColorSelected) {
		this.tabColorSelected = tabColorSelected;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public void setTextColorSelected(int textColorSelected) {
		this.textColorSelected = textColorSelected;
	}

	public void setCursorRes(int cursorRes) {
		this.cursorRes = cursorRes;
	}
	public void setTabNames(String[] tabNames) {
		this.tabNames = tabNames;
		tabSize = tabNames.length;
	}

	public void setHasTabColor(boolean hasTabColor) {
		this.hasTabColor = hasTabColor;
	}
	
	public void setTabSize(int tabSize) {
		this.tabSize = tabSize;
	}
	
	public void setOnPageSelectListener(MyOnPageSelectListener onPageSelectListener){
		pcListener.setMyOnPageSelectListener(onPageSelectListener);
	}
	
	public void setFragmentList(List<Fragment> fragmentList) {
		this.fragmentList = fragmentList;
	}

	/**
	 * 重新加载(刷新)Fragment
	 * @param index
	 */
	public void reLoadPage(int index){
		adapter.fragmentsUpdateFlag[index] = true;
		adapter.setFragmentList(fragmentList);
	}
	public void reLoadPage(){
		for (int i = 0;i < fragmentList.size(); i++){
			adapter.fragmentsUpdateFlag[i] = true;
		}
		adapter.setFragmentList(fragmentList);
	}

	@Override
	public void showView() {
		/*
		 * 循环生成页签
		 */
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(phoneUtil.getWidth(), LayoutParams.MATCH_PARENT);
		params.weight = 1;
		tabs = new ArrayList<TextView>();
		for (int i = 0; i < tabSize; i++) {
			TextView tv = new TextView(context);
			tv.setLayoutParams(params);
			tv.setGravity(Gravity.CENTER);
			tv.setTextSize(textSize);
			llayTabs.addView(tv);
			tabs.add(tv);
		}
		pcListener = new MyOnPageChangeListener(context);
		pcListener.initCursorPos();
		
		
		/*
		 * 添加页签的标题，并添加点击事件
		 */
		for (int i = 0; i < tabSize; i++) {
			TextView tv = tabs.get(i);
			tv.setText(tabNames[i]);
			tv.setOnClickListener(new PagerClickListener(i, vp));
		}
		/*
		 * 初始化ViewPager，并适配数据
		 */
		vp.setOnPageChangeListener(pcListener);
		vp.setOffscreenPageLimit(tabNames.length - 1);
		if(adapter == null){
			adapter = new MyFragmentAdapter(((FragmentActivity)context).getSupportFragmentManager(), fragmentList);
		}else{
			adapter.notifyDataSetChanged();
		}

		vp.setAdapter(adapter);
		vp.setCurrentItem(0);

//		cursor.setImageResource(cursorRes);
		cursor.setBackgroundColor(context.getResources().getColor(textColorSelected));
		PhoneUtil phoneUtil = new PhoneUtil(context);
		LinearLayout.LayoutParams paramsCursor = new LinearLayout.LayoutParams(phoneUtil.getWidth()/tabSize, (int) (phoneUtil.getDensity()*2));
		cursor.setLayoutParams(paramsCursor);

		// 获取滚动条的宽度
		bmpWidth = BitmapFactory.decodeResource(getResources(), cursorRes).getWidth();
		//为了获取屏幕宽度，新建一个DisplayMetrics对象
		DisplayMetrics displayMetrics = new DisplayMetrics();
		//将当前窗口的一些信息放在DisplayMetrics类中
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		//得到屏幕的宽度
		int screenW = displayMetrics.widthPixels;
		//计算出滚动条初始的偏移量
		offset = (screenW / tabSize - bmpWidth) / 2;
		//计算出切换一个界面时，滚动条的位移量
		one = offset * 2 + bmpWidth;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		//将滚动条的初始位置设置成与左边界间隔一个offset
		cursor.setImageMatrix(matrix);
	}
	
	@Override
	protected void initView(){
		view = LayoutInflater.from(context).inflate(R.layout.view_tabs_view, this);
		vp = (ViewPager) view.findViewById(R.id.vp);
		llayTabs = (LinearLayout) view.findViewById(R.id.llay_tabs);
		cursor = (ImageView) view.findViewById(R.id.iv_cursor);
		if(!hasCursor){
			cursor.setVisibility(View.GONE);
		}
	}
	/**
	 * 页签切换事件监听，主要是进行滑块的偏移量计算、设置动画、字体颜色等外观上的操作。
	 *
	 */
	private int offset;
	private int bmpWidth;
	private int one;
	private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
		private Context context;
		private MyOnPageSelectListener myOnPageSelectListener;
		public int currentIndex = 0;

		public MyOnPageChangeListener(Context context) {
			this.context = context;
			
			if(hasTabColor){
				tabs.get(0).setBackgroundColor(context.getResources().getColor(tabColorSelected));
			}else{
				tabs.get(0).setTextColor(context.getResources().getColor(textColorSelected));
			}
		}

		public void setMyOnPageSelectListener(
				MyOnPageSelectListener myOnPageSelectListener) {
			this.myOnPageSelectListener = myOnPageSelectListener;
		}

		@Override
		public void onPageSelected(int index) {
//			one = offset * 2 + bmpWidth;// 移动一页的偏移量,比如1->2,或者2->3
			Animation animation = null;
			for (int i = 0; i < tabSize; i++) {

				if (index == i) {
					for (int j = 0; j < tabSize; j++) {
						if (currentIndex != index) {
							if (currentIndex == j && currentIndex == 0) {
								animation = new TranslateAnimation(offset, i * one,
										0, 0);
							}
							if (currentIndex == j && currentIndex > 0) {
								animation = new TranslateAnimation(j * one,
										i * one, 0, 0);
							}
						}
					}
				}
			}
			if(hasTabColor){
				tabs.get(currentIndex).setBackgroundColor(context.getResources().getColor(R.color.bg_transparent));
				tabs.get(index).setBackgroundColor(context.getResources().getColor(tabColorSelected));
			}else{
				tabs.get(currentIndex).setTextColor(context.getResources().getColor(textColor));	
				tabs.get(index).setTextColor(context.getResources().getColor(textColorSelected));
			}
			currentIndex = index;

			animation.setFillAfter(true);// true表示图片停在动画结束位置
			animation.setDuration(300); // 设置动画时间为300毫秒
			if (cursor != null) {
				cursor.startAnimation(animation);// 开始动画
			}
			/*
			 * 当页面切换时还需要进行的其他操作
			 */
			if(myOnPageSelectListener != null){
				myOnPageSelectListener.pageSelected(index);
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		// 初始化指示器的位置,就是下面那个移动条一开始放的地方
		public void initCursorPos() {
			// cursor = (ImageView) findViewById(R.id.iv_cursor);
			bmpWidth = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.bar_yellow).getWidth();// 获取图片宽度
			DisplayMetrics dm = new DisplayMetrics();
			((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
			int screenW = dm.widthPixels;// 获取分辨率宽度
			offset = (screenW / tabSize - bmpWidth) / 2;// 计算偏移量
			Matrix matrix = new Matrix();
			matrix.postTranslate(offset, 0);
			cursor.setImageMatrix(matrix);// 设置动画初始位置
		}
	}
	/**
	 * 设置点击事件,点击上面文字切换页面的
	 *
	 */
	private class PagerClickListener implements OnClickListener {
		private int index = 0;
		private ViewPager vp;

		public PagerClickListener(int i, ViewPager vp) {
			index = i;
			this.vp = vp;
		}

		@Override
		public void onClick(View arg0) {
			vp.setCurrentItem(index);
		}
	}
	/**
	 * 提供给外部的接口，当页面切换时使用。
	 *
	 */
	public interface MyOnPageSelectListener{
		void pageSelected(int index);
	}
}
