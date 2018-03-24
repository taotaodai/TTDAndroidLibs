package hzst.android.view.event;


import hzst.android.R;
import hzst.android.view.SlideTabsView;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 除了页卡监听，还有初始化移动条的位置、选项卡点击事件。
 * 
 * @author wt 2015-1-12
 *
 */
@Deprecated
public class MyOnPageChangeListener implements OnPageChangeListener {
	private Context context;
	private SlideTabsView slideTabsView;
	private int pageCount;
	private int offset;
	private int bmpWidth;
	private ImageView cursor;
	private OnPageChangeListener onPageChangeListener;
	private List<TextView> tabs;
	public int currentIndex = 0;
	private int textColor;
	private int textColorSelected;
	private int tabColorSelected;

	public MyOnPageChangeListener() {
	}

	public MyOnPageChangeListener(int pageCount) {
		this.pageCount = pageCount;
	}

	public MyOnPageChangeListener(SlideTabsView slideTabsView,Context context) {
		this.slideTabsView = slideTabsView;
		this.cursor = slideTabsView.getCursor();
		this.tabs = slideTabsView.getTabs();
		this.context = context;
		this.textColor = slideTabsView.getTextColor();
		this.textColorSelected = slideTabsView.getTextColorSelected();
		this.tabColorSelected = slideTabsView.getTabColorSelected();
		this.pageCount = slideTabsView.getTabSize();
		
		if(slideTabsView.isHasTabColor()){
			tabs.get(0).setBackgroundColor(context.getResources().getColor(tabColorSelected));
		}else{
			tabs.get(0).setTextColor(context.getResources().getColor(textColorSelected));
		}
		
	}

	public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
		this.onPageChangeListener = onPageChangeListener;
	}

	@Override
	public void onPageSelected(int index) {
		int one = offset * 2 + bmpWidth;// 移动一页的偏移量,比如1->2,或者2->3
		Animation animation = null;
		for (int i = 0; i < pageCount; i++) {

			if (index == i) {
				for (int j = 0; j < pageCount; j++) {
					// System.out.println("滑动-当前页"+currentIndex+"去到页-"+index+"---");
					if (currentIndex != index) {
						// System.out.println("当前页"+currIndex+"---");
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
		if(slideTabsView.isHasTabColor()){
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
				
		if(onPageChangeListener != null){
			onPageChangeListener.doOnPageSelected(index);
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	// 初始化指示器的位置,就是下面那个移动条一开始放的地方
	public void initCursorPos() {
		// cursor = (ImageView) findViewById(R.id.iv_cursor);
		bmpWidth = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bar_yellow).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / pageCount - bmpWidth) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		// System.out.println(matrix+"---");
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	// 设置点击事件,点击上面文字切换页面的
	public class PagerClickListener implements OnClickListener {
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
	@Deprecated
	public interface IOnPageChange{
		void doOnPageSelected(int index);
	}
	public interface OnPageChangeListener{
		void doOnPageSelected(int index);
	}

}
