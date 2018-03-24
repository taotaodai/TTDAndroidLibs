package hzst.android.form.view;

import android.content.Context;
import android.text.TextUtils;

import hzst.android.form.entity.DateViewOwn;
import hzst.android.form.info.DateViewInfo;
import hzst.android.util.DateOrTimeUtil;
import hzst.android.view.MyDateView;
import hzst.android.view.MyDateView.OnDateTimeChangedListener;

public class FMDateView extends FMBaseView{
	private DateViewInfo info;
	private MyDateView view;
	private OnDateChangedListener onDateChangedListener;
	private long minDate;
	private long maxDate;

	public FMDateView(Context context) {
		super(context);
	}

	public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
		this.onDateChangedListener = onDateChangedListener;
	}

	public void setMinDate(long minDate) {
		this.minDate = minDate;
		view.setMinDate(minDate);
	}

	public void setMaxDate(long maxDate) {
		this.maxDate = maxDate;
		view.setMaxDate(maxDate);
	}

	@Override
	protected void initView() {
		
	}
	public String getDate(){
		return view.getDate();
	}
	@Override
	public void showView() {
		info = (DateViewInfo)super.info;
		view = new MyDateView(context);
		view.setOnDateTimeChangedListener(new OnDateTimeChangedListener() {

			@Override
			public void dateTimeChanged(String time) {
				viewCreator.setSubmitValue(time, info);
				if(onDateChangedListener != null){
					onDateChangedListener.dateChanged(time);
				}
//				viewCreator.updateViewInfo(info, true);
			}
		});

		String value = info.getSubmitValues().get(0).getValue();//DateView只有一个value
//		if(!TextUtils.isEmpty(value)){
			view.setDate(value);
//		}else {
//			view.setDate(DateOrTimeUtil.getCurrentDate(info.getDateFormat()));
//		}

		switch (info.getDateFormat()){
			case DateOrTimeUtil.DATE_MODE_YMDHM_2:
				view.setTimeEnable(true);
				break;
			default:
				view.setTimeEnable(false);
				break;
		}

		DateViewOwn own = info.getOwn();
		if(own != null){
			minDate = own.getMinDate();
			maxDate = own.getMaxDate();
			if(minDate > 0 && maxDate > 0 && maxDate > minDate){
				view.setMinDate(minDate);
				view.setMaxDate(maxDate);
			}
		}

		if(info.isReadOnly()){
			view.setEnable(false);
		}

		addView(view);
	}

	public interface OnDateChangedListener{
		void dateChanged(String date);
	}

}
