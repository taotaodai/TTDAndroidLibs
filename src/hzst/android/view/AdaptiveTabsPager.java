package hzst.android.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import hzst.android.R;
import hzst.android.adapter.MyFragmentAdapter;
/**
 * 左右滑动的页签控件，和{@link TabsPager}的主要区别是使用了安卓原生的{@link PagerTabStrip}适合页签较多的情况下使用。
 * 使用时必须调用{@link #setFragmentList(List)} {@link #setTabsData(String[], int, int, int, int)}
 * @author wt
 *
 */
public class AdaptiveTabsPager extends BaseView{
	private ViewPager vp;
	private PagerTabStrip pagerTabStrip;
	
	private OnPageSelectedListener onPageSelectedListener;
	private FragmentPagerAdapter pagerAdapter;
	private int currentPage;
	private List<CharSequence> titleList = new ArrayList<CharSequence>();
	private List<Fragment> fragmentList;
	private String[] titles;
	
	private int tabIndicatorColor;
	private int tabBackgroundColor;
	private int titleColorLight;
	private int titleColor;
	
	public AdaptiveTabsPager(Context context) {
		super(context);
		initView();
	}

	public AdaptiveTabsPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	protected void initView() {
		View view = LayoutInflater.from(context).inflate(R.layout.view_adaptive_tabspager, this);
		vp = (ViewPager) view.findViewById(R.id.vp);
		pagerTabStrip = (PagerTabStrip) view.findViewById(R.id.pagertabstrip);
	}
	
	public void showView() {
		//设置下划线的颜色
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(tabIndicatorColor)); 
		//设置背景的颜色
		pagerTabStrip.setBackgroundColor(getResources().getColor(tabBackgroundColor));
		
		initTabs();
	}
	/**
	 * 
	 * @param titles 页签标题
	 * @param tabIndicatorColor 下划线颜色
	 * @param tabBackgroundColor 背景色
	 * @param titleColorLight 文字高亮(当前页签)颜色
	 * @param titleColor 文字(非当前页签)颜色
	 */
	public void setTabsData(String[] titles,int tabIndicatorColor,int tabBackgroundColor,
			int titleColorLight,int titleColor){
		
		this.titles = titles;
		this.tabIndicatorColor = tabIndicatorColor;
		this.tabBackgroundColor = tabBackgroundColor;
		this.titleColorLight = titleColorLight;
		this.titleColor = titleColor;
		
		vp.setOffscreenPageLimit(titles.length - 1);//设置预加载页数
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int index) {
				if(onPageSelectedListener != null){
					onPageSelectedListener.onPageSelected(index);
					
				}
				currentPage = index;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
	}
	
	private void initTabs(){
		titleList.clear();
		for (int i = 0; i < titles.length; i++) {
			if(i == currentPage){
				titleList.add(createTitle(titles[i], titleColorLight));
			}else{
				titleList.add(createTitle(titles[i], titleColor));
			}
		}
		if(pagerAdapter == null){
			pagerAdapter = new MyFragmentAdapter(((FragmentActivity)context).getSupportFragmentManager(),fragmentList,titleList);
			vp.setAdapter(pagerAdapter);
		}else{
			pagerAdapter.notifyDataSetChanged();
		}
	}
	
	private CharSequence createTitle(String title,int colorRes){
		SpannableStringBuilder ssb = new SpannableStringBuilder(title);
		
		ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(colorRes));// 字体颜色设置为绿色
		ssb.setSpan(fcs, 0, ssb.length(),
		Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置字体颜色
		return ssb;
	}
	
	public void setFragmentList(List<Fragment> fragmentList) {
		this.fragmentList = fragmentList;
	}

	public void setOnPageSelectedListener(
			OnPageSelectedListener onPageSelectedListener) {
		this.onPageSelectedListener = onPageSelectedListener;
	}
	/**
	 * 对外接口，当页面切换时使用。
	 *
	 */
	public interface OnPageSelectedListener{
		void onPageSelected(int index);
	}
	
}
