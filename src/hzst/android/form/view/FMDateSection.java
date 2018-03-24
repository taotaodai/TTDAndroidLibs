package hzst.android.form.view;

import hzst.android.R;
import hzst.android.form.ViewCreator;
import hzst.android.form.info.BaseViewInfo;
import hzst.android.form.info.BaseViewInfo.SubmitValue;
import hzst.android.form.info.DateSectionViewInfo;
import hzst.android.util.DateOrTimeUtil;
import hzst.android.util.L;
import hzst.android.view.MyDateView;
import hzst.android.view.MyDateView.OnDateTimeChangedListener;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
/**
 * 时间段选择
 * @author wt
 *
 */
public class FMDateSection extends FMBaseView{
	private Context context;
	private List<SubmitValue> submitValues;
	
	private MyDateView mdvSatart;
	private MyDateView mdvEnd;
//	private Spinner spSatart;
//	private Spinner spEnd;
	private EditText etDays;
	private TextView tvTo;
	private TextView tvDay1,tvDay2;
	private LinearLayout llayTotal;
	
	private ViewCreator viewCreator;
	private DateSectionViewInfo info;
	private boolean isReadOnly;
	
	public FMDateSection(Context context) {
		super(context);
		this.context = context;
		initView();
	}
	
	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	protected void initView(){
		View view = LayoutInflater.from(context).inflate(R.layout.view_datesection, this);
		
		mdvSatart = (MyDateView) view.findViewById(R.id.mdv_start);
		mdvEnd = (MyDateView) view.findViewById(R.id.mdv_end);
//		mdvSatart.setTimeEnable(false);
//		mdvEnd.setTimeEnable(false);
//		spSatart = (Spinner) view.findViewById(R.id.sp_start);
//		spEnd = (Spinner) view.findViewById(R.id.sp_end);
		etDays = (EditText) view.findViewById(R.id.et_days);
		tvTo = (TextView) view.findViewById(R.id.tv_to);
		tvDay1 = (TextView) view.findViewById(R.id.tv_day1);
		tvDay2 = (TextView) view.findViewById(R.id.tv_day2);
		llayTotal = (LinearLayout) view.findViewById(R.id.llay_total);

	}
	public void showView(final ViewCreator viewCreator){
		info = (DateSectionViewInfo)super.info;
		submitValues = info.getSubmitValues();

		this.viewCreator = viewCreator;
		if(isReadOnly){
			mdvSatart.setEnable(false);
			mdvEnd.setEnable(false);
//			spSatart.setEnabled(false);
//			spEnd.setEnabled(false);
			etDays.setEnabled(false);
			tvTo.setEnabled(false);
			tvDay1.setEnabled(false);
			tvDay2.setEnabled(false);
		}

		String startTime = submitValues.get(0).getValue();
		String endTime = submitValues.get(1).getValue();
		if(!TextUtils.isEmpty(startTime)){
			mdvSatart.setDate(startTime);
		}
		if(!TextUtils.isEmpty(endTime)){
			mdvEnd.setDate(endTime);
		}
		switch (info.getDateFormat()){

			case DateOrTimeUtil.DATE_MODE_YMDHM_2:
				mdvSatart.setTimeEnable(true);
				mdvEnd.setTimeEnable(true);
				bindDateListener(mdvSatart, 0);
				bindDateListener(mdvEnd, 1);
				bindStatistics();
				break;
			case DateOrTimeUtil.DATE_MODE_YMD_2:
				mdvSatart.setTimeEnable(false);
				mdvEnd.setTimeEnable(false);
				bindDateListener(mdvSatart, 0);
				bindDateListener(mdvEnd, 1);
				break;
		}

		String statistics = info.getStatisticsType();
		if(TextUtils.isEmpty(statistics)){
			llayTotal.setVisibility(View.GONE);
		}else {
			switch (statistics){
				case DateSectionViewInfo.STATISTICS_HOUR:
					tvDay2.setText("小时");
					break;
				case DateSectionViewInfo.STATISTICS_DAY:
					tvDay2.setText("天");
					break;
			}
		}
	}

	private void bindStatistics(){
		etDays.setText(submitValues.get(2).getValue());
		etDays.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				viewCreator.setSubmitValue(s.toString(), info,2);
			}
		});
	}
	private void bindDateListener(MyDateView mdv,final int index){
		mdv.setOnDateTimeChangedListener(new OnDateTimeChangedListener() {
			
			@Override
			public void dateTimeChanged(String time) {
				
				viewCreator.setSubmitValue(time, info,index);
//				viewCreator.updateViewInfo(info, true);
			}
		});
	}
	private void bindAM$PMListener(Spinner sp,final int index){
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				viewCreator.setSubmitValue(String.valueOf(arg2), info,index);
//				viewCreator.updateViewInfo(info, true);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	}
	
	public interface OnDateChangeListener{
		void dateChanged();
	}

}
