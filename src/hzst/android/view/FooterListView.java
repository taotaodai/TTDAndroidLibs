package hzst.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import hzst.android.R;
import hzst.android.util.L;
import hzst.android.view.swipemenulistview.SwipeMenuListView;

/**
 * 滑到底部显示footerView的ListView
 * 主要用于分页
 * @author wt
 * 
 */
public class FooterListView extends SwipeMenuListView implements OnScrollListener{
	private View footer;
	private TextView tvFooterHint;
	
	private int pageSize;//每页显示的条数
	private int itemCount;//当前数据的总条数
	private boolean footerEnable = true;
	
	private IMyListView callback;
	private OnListStateChangedListener onListStateChangedListener;
	private OnSlideToBottomListener onSlideToBottomListener;
	private static final int DOT_SIZE = 3;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			tvFooterHint.setText(msg.obj.toString());
		}
	};
	
	public FooterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.FooterListView);
		pageSize = a.getInteger(R.styleable.FooterListView_pageSize, 0);
		a.recycle();
		
		LayoutInflater inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.view_pullup_footer, null);
		tvFooterHint = (TextView) footer.findViewById(R.id.tv_hint);
		
		showIsLoading();
		
	}
	
	public OnListStateChangedListener getOnListStateChangedListener() {
		return onListStateChangedListener;
	}

	public void setOnListStateChangedListener(
			OnListStateChangedListener onListStateChangedListener) {
		this.onListStateChangedListener = onListStateChangedListener;
		setOnScrollListener(this);
	}

	public void setOnSlideToBottomListener(OnSlideToBottomListener onSlideToBottomListener) {
		this.onSlideToBottomListener = onSlideToBottomListener;
		setOnScrollListener(this);
	}

	public boolean isFooterEnable() {
		return footerEnable;
	}

	public void setFooterEnable(boolean footerEnable) {
		this.footerEnable = footerEnable;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void initListView(IMyListView callback){
		this.callback = callback;
		setOnScrollListener(this);
	}
	
	private void showIsLoading(){
		new Thread(){

			@Override
			public void run() {
				int dotIndex = 0;
				while(true){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						L.showLogInfo(L.TAG_EXCEPTION, e.toString());
					}
					StringBuffer sb = new StringBuffer("");

					for (int j = 0; j < dotIndex; j++) {
						sb.append(".");
					}
					if(dotIndex <= DOT_SIZE){
						dotIndex ++;
					}
					if(dotIndex > DOT_SIZE){
						dotIndex = 0;
					}

					Message msg = Message.obtain();
					msg.obj = sb.toString();
					handler.sendMessage(msg);
				}
				
			}
			
		}.start();
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(!footerEnable){
			return;
		}
		// 当不滚动时  
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			// 判断是否滚动到底部  
			if (view.getLastVisiblePosition() == view.getCount() - 1 ) {
				if(callback != null && getFooterViewsCount() > 0){
					callback.doAfterAddFooter();
				}
				if(onListStateChangedListener != null && getFooterViewsCount() > 0){
					onListStateChangedListener.doAfterAddFooter();
				}
				if(onSlideToBottomListener != null && getFooterViewsCount() > 0){
					onSlideToBottomListener.arriveBottom();
				}
			}
		}
	}
	
	/**
	 * 在setAdapter(adapter)和notifyDataSetChanged()之后请调用该方法来显示或者隐藏FooterView
	 */
	public void loadFooterView(boolean isRefresh){
		int count = getAdapter().getCount() - getFooterViewsCount();
		if(count == itemCount && !isRefresh){
			removeFooterView();
			return;
		}
		itemCount = count;
		try {
			if (count < pageSize || count % pageSize != 0 || !footerEnable) {
				removeFooterView();
			} else {
				if (getFooterViewsCount() == 0) {
					addFooterView(footer);
				}
			}
		} catch (ArithmeticException e) {
			removeFooterView();
			L.showLogInfo(L.TAG_EXCEPTION, "请传入每页数量setPageSize()");
		}
	}
	/**
	 * 重置数据。当进行刷新、搜索等操作时调用。
	 */
	public void resetData(){
		itemCount = 0;
	}
	
	public void removeFooterView() {
		try {
			removeFooterView(footer);
		} catch (Exception e) {
		}
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	
	@Deprecated
	public interface IMyListView{
		void doAfterAddFooter();
	}
	@Deprecated
	public interface OnListStateChangedListener{
		//当滑动到底部时调用，一般用于分页
		void doAfterAddFooter();
	}

	public interface OnSlideToBottomListener{
		void arriveBottom();
	}

}
