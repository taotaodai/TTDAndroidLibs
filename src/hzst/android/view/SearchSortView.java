package hzst.android.view;

import hzst.android.R;
import hzst.android.adapter.CommonAdapter;
import hzst.android.util.BackgroundUtil;
import hzst.android.util.L;
import hzst.android.util.PhoneUtil;
import hzst.android.view.MyDateView.OnDateTimeChangedListener;
import hzst.android.view.viewdata.SearchSortInfo;
import hzst.android.view.viewdata.SearchSortInfo.SearchInfo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
/**
 * 用于搜索排序前获取高级查询关键字和排序关键字
 * @author Administrator
 *
 */
public class SearchSortView extends BaseView{
	private Context context;
	private SearchSortInfo searchSortInfo;
	private LinearLayout llaySearchSort;
	private PhoneUtil phoneUtil;
	
	/*
	 * 搜索排序操作页面控件
	 */
//	private LinearLayout llaySearch;
	private LinearLayout llaySearchValues;
	private ListView lvSort;
	private TextView tvConfirm;
	private List<View> searchValueViews;
	private OnSearchListener onSearchListener;
	private OnSortListener onSortListener;
	
	private PopupWindow pop;
	
	private static final int MAX_VISIBLE_ITEM = 8;

	public SearchSortView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public SearchSortView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public void setOnSearchListener(OnSearchListener onSearchListener) {
		this.onSearchListener = onSearchListener;
	}

	public void setOnSortListener(OnSortListener onSortListener) {
		this.onSortListener = onSortListener;
	}

	public SearchSortInfo getSearchSortInfo() {
		return searchSortInfo;
	}

	public void setSearchSortInfo(SearchSortInfo searchSortInfo) {
		this.searchSortInfo = searchSortInfo;
	}

	@Override
	protected void initView(){
		phoneUtil = new PhoneUtil(context);
		View view = LayoutInflater.from(context).inflate(R.layout.view_search_sort_skin, this);
		llaySearchSort = (LinearLayout) view.findViewById(R.id.llay_search_sort);
		BackgroundUtil backgroundUtil = new BackgroundUtil(context);
		backgroundUtil.setPressColor(llaySearchSort, R.color.bg_white, 0);
		llaySearchSort.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSearchSortView();
			}
		});
	}

	/**
	 * 请在点击事件或其他事件中调用该方法
	 */
	@Override
	public void showView() {
		showSearchSortView();
	}

	@SuppressWarnings("deprecation")
	public void showSearchSortView(){
		if(searchSortInfo != null){
			if(pop == null){
				View view = LayoutInflater.from(context).inflate(R.layout.view_search_sort_operate, null);
//				llaySearch =  (LinearLayout) view.findViewById(R.id.llay_search);
				llaySearchValues = (LinearLayout) view.findViewById(R.id.llay_search_value);
				lvSort = (ListView) view.findViewById(R.id.lv_sort);
				tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
				tvConfirm.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						pop.dismiss();
						if(onSearchListener != null){
							onSearchListener.search(searchSortInfo.getSearchValues());
						}
					}
				});
				TextView tvReset = (TextView) view.findViewById(R.id.tv_reset);
				tvReset.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						clearSearchValues();
					}
				});
				BackgroundUtil backgroundUtil = new BackgroundUtil(context);
				backgroundUtil.setPressColor(tvConfirm, R.color.bg_blue_deep,0);
				backgroundUtil.setPressColor(tvReset, R.color.bg_orange,0);
				/**
				 * 生成排序列表
				 */
				searchValueViews = new ArrayList<View>();
				List<String> sortValues = searchSortInfo.getSortValues();
				if(sortValues.size() > 0){
					lvSort.setAdapter(new CommonAdapter<String>(context, sortValues, R.layout.adapter_sort_value) {

						@Override
						public void convert(ViewHolder helper, String item,
								int position) {
							helper.setText(R.id.tv_sort_value, item);
						}
					});
					lvSort.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							if(onSortListener != null){
								onSortListener.sort(searchSortInfo.getSortValues().get(position));
							}
						}
					});
				}else{
					lvSort.setVisibility(View.GONE);
				}
				
				/**
				 * 生成搜索列表
				 */
				List<SearchInfo> searchValues = searchSortInfo.getSearchValues();
				if(searchValues.size() > 0){
					for (int i = 0; i < searchValues.size(); i++) {
						SearchInfo searchInfo = searchValues.get(i);
						
						String searchName = searchInfo.getSearchName();
						final List<String> selectableValues = searchInfo.getSelectableValues();
						
						switch (searchInfo.getType()) {
						case SearchInfo.TYPE_SELECT:
							View viewSelect = LayoutInflater.from(context).inflate(R.layout.adapter_search_value_select, null);
							TextView tvName1 = (TextView) viewSelect.findViewById(R.id.tv_search_name);
							final TextView tvValue = (TextView) viewSelect.findViewById(R.id.tv_select_value);
							tvValue.setTag(i);
							searchValueViews.add(tvValue);
							tvName1.setText(searchName);
							tvName1.setTag(i);
							monitorKeyChanged(tvName1);
							LinearLayout llaySelect = (LinearLayout) viewSelect.findViewById(R.id.llay_select);
							llaySelect.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									showSelectView(selectableValues, tvValue);
								}
							});
							llaySearchValues.addView(viewSelect);
							break;
						case SearchInfo.TYPE_INPUT:
							View viewInput = LayoutInflater.from(context).inflate(R.layout.adapter_search_value, null);
							TextView tvName2 = (TextView) viewInput.findViewById(R.id.tv_name);
							EditText etValue = (EditText) viewInput.findViewById(R.id.et_value);
							etValue.setPadding((int) (phoneUtil.getDensity()*5), 0, 0, 0);
							etValue.setText(searchInfo.getInputValue());
							searchValueViews.add(etValue);
							etValue.setTag(i);
							monitorKeyChanged(etValue);
//							etValue.setTextIsSelectable(true);
							tvName2.setText(searchName);
							llaySearchValues.addView(viewInput);
							break;
						case SearchInfo.TYPE_DATE:
							View viewDate = LayoutInflater.from(context).inflate(R.layout.adapter_search_value_date, null);
							final MyDateView dateView = (MyDateView) viewDate.findViewById(R.id.mdv_value);
							searchValueViews.add(dateView);
							TextView tvNameDate = (TextView) viewDate.findViewById(R.id.tv_search_name);
							tvNameDate.setText(searchName);
							dateView.setTag(i);
							dateView.setTimeEnable(false);
							dateView.setOnDateTimeChangedListener(new OnDateTimeChangedListener() {
								
								@Override
								public void dateTimeChanged(String time) {
									searchSortInfo.getSearchValues().get(Integer.valueOf(dateView.getTag().toString())).setInputValue(time);
								}
							});
							llaySearchValues.addView(viewDate);
							break;
						}
					}
				}
				
				int height;
				if(sortValues.size() > MAX_VISIBLE_ITEM || searchValues.size() > MAX_VISIBLE_ITEM){
					height = phoneUtil.getHeight()*2/3;
				}else{
					height = LayoutParams.WRAP_CONTENT;
				}
				int width;
//				if((sortValues.size() > 0 && searchValues.size() == 0) || (sortValues.size() == 0 && searchValues.size() > 0)){
//					width = LayoutParams.WRAP_CONTENT;
//				}else{
//					width = (int) (phoneUtil.getWidth()-10*phoneUtil.getDensity());
//				}
				width = (int) (phoneUtil.getWidth()-10*phoneUtil.getDensity());
				pop = new PopupWindow(view,width,height,false);
				pop.setFocusable(true);
				pop.setBackgroundDrawable(new BitmapDrawable());
				pop.setOutsideTouchable(true);
				pop.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
				pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
				pop.setAnimationStyle(R.style.PopupAnimation);

				pop.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss() {
						WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();  
						lp.alpha = 1f;
						((Activity)context).getWindow().setAttributes(lp); 
//						pop.dismiss();
					}
				});
			}
			
			WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
			lp.alpha = 0.7f;
			((Activity)context).getWindow().setAttributes(lp);
//			pop.showAsDropDown(this);
			pop.showAtLocation(this, Gravity.CENTER, 0, 0);
			
		}
	}
	/**
	 * 显示可选搜索项对话框
	 * @param selectableValues
	 * @param tvValue
	 */
	private void showSelectView(final List<String> selectableValues,final TextView tvValue){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setItems((String[]) selectableValues.toArray(), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String selectValue = selectableValues.get(which);
				tvValue.setText(selectValue);
				searchSortInfo.getSearchValues().get(Integer.valueOf(tvValue.getTag().toString())).setInputValue(selectValue);
			}
		});
		builder.create().show();
	}
	/**
	 * 监听关键字的文本框的文本变化事件
	 * @param tv
	 */
	private void monitorKeyChanged(final TextView tv){
		tv.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				searchSortInfo.getSearchValues().get(Integer.valueOf(tv.getTag().toString())).setInputValue(tv.getText().toString());
			}
		});
	}
	/**
	 * 清空搜索关键字
	 */
	public void clearSearchValues(){
		try {
			List<SearchInfo> searchInfos = searchSortInfo.getSearchValues();
			for (int i = 0; i < searchInfos.size(); i++) {
				searchInfos.get(i).setInputValue("");
				View viewInput = searchValueViews.get(i);
				if(viewInput instanceof TextView){
					((TextView) viewInput).setText("");
				}
				if(viewInput instanceof MyDateView){
					((MyDateView)viewInput).setDate("");
				}
			}
		} catch (Exception e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
	}
	
	public interface OnSearchListener{
		void search(List<SearchInfo> searchValues);
	}

	public interface OnSortListener{
		void sort(String key);
	}
}
