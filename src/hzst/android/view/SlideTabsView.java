package hzst.android.view;

import hzst.android.R;
import hzst.android.util.PhoneUtil;
import hzst.android.view.event.MyOnPageChangeListener;
import hzst.android.view.event.MyOnPageChangeListener.OnPageChangeListener;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 滑动页签控件
 * 主要和ViewPager配合使用。
 * @author wt
 * 
 * @deprecated 已经废弃，请用{@link TabsPager}
 *
 */
@Deprecated
public class SlideTabsView extends LinearLayout{
	private Context context;
	private MyOnPageChangeListener pcListener;
	private LinearLayout llayTabs;
	private ImageView cursor;
	private List<TextView> tabs;
	private PhoneUtil phoneHelper;
	
	private int tabColorSelected;
	private int tabSize;
	private int textColor;
	private int textColorSelected;
	private int cursorRes;
	private boolean hasCursor;
	private boolean hasTabColor = false;
	
	public ImageView getCursor() {
		return cursor;
	}

	public void setCursor(ImageView cursor) {
		this.cursor = cursor;
	}

	public List<TextView> getTabs() {
		return tabs;
	}

	public void setTabs(List<TextView> tabs) {
		this.tabs = tabs;
	}

	public int getTabColorSelected() {
		return tabColorSelected;
	}

	public void setTabColorSelected(int tabColorSelected) {
		this.tabColorSelected = tabColorSelected;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getTextColorSelected() {
		return textColorSelected;
	}

	public void setTextColorSelected(int textColorSelected) {
		this.textColorSelected = textColorSelected;
	}

	public int getCursorRes() {
		return cursorRes;
	}

	public void setCursorRes(int cursorRes) {
		this.cursorRes = cursorRes;
	}

	public boolean isHasTabColor() {
		return hasTabColor;
	}


	public void setHasTabColor(boolean hasTabColor) {
		this.hasTabColor = hasTabColor;
	}

	
	public SlideTabsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		phoneHelper = new PhoneUtil(context);
		this.context = context;
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.SlideTabsView);
		
		tabSize = a.getInteger(R.styleable.SlideTabsView_tabSize, 0);
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
		
	public int getTabSize() {
		return tabSize;
	}

	public void setTabSize(int tabSize) {
		this.tabSize = tabSize;
	}

	public MyOnPageChangeListener getPcListener() {
		return pcListener;
	}
	/**
	 * $初始化控件$
	 * @param tabNames
	 * @param callback
	 * @param viewPager
	 */
	public void initTabsView(String[] tabNames,OnPageChangeListener onPageChangeListener,ViewPager viewPager){
		for (int i = 0; i < tabSize; i++) {
			TextView tv = tabs.get(i);
			tv.setText(tabNames[i]);
			tv.setOnClickListener(pcListener.new PagerClickListener(i, viewPager));
		}
		viewPager.setOnPageChangeListener(pcListener);
		pcListener.setOnPageChangeListener(onPageChangeListener);
		
	}
	
	private void initView(){
		View view = LayoutInflater.from(context).inflate(R.layout.view_tabs_view, null);
		llayTabs = (LinearLayout) view.findViewById(R.id.llay_tabs);
		cursor = (ImageView) view.findViewById(R.id.iv_cursor);
		if(!hasCursor){
			cursor.setVisibility(View.GONE);
		}
		cursor.setImageResource(cursorRes);
		
		/*
		 * 循环生成页签
		 */
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(phoneHelper.getWidth(), LayoutParams.FILL_PARENT);
		params.weight = 1;
		tabs = new ArrayList<TextView>();
		for (int i = 0; i < tabSize; i++) {
			TextView tv = new TextView(context);
			tv.setLayoutParams(params);
			tv.setGravity(Gravity.CENTER);			
			llayTabs.addView(tv);
			tabs.add(tv);
		}
		pcListener = new MyOnPageChangeListener(this,context);
		pcListener.initCursorPos();
		
		addView(view);
	}

}
