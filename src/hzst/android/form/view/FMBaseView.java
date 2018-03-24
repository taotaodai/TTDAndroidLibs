package hzst.android.form.view;

import java.util.List;

import hzst.android.form.ViewCreator;
import hzst.android.form.info.BaseViewInfo;
import hzst.android.form.info.BaseViewInfo.SubmitValue;
import hzst.android.util.PhoneUtil;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
/**
 * 表单中控件的基类。
 * 因为{@link LinearLayout}局限性太大，不会所有的控件都继承它。
 * @author wt
 *
 */
public class FMBaseView extends LinearLayout{
	protected Context context;
	
	public PhoneUtil phoneUtil;
	public float density;

	protected OnViewDataChangeListener onViewDataChangeListener;
	protected List<SubmitValue> submitValues;
	protected BaseViewInfo info;
	protected ViewCreator viewCreator;
	
	public FMBaseView(Context context) {
		super(context);
		this.context = context;
		initBaseData();
	}

	public FMBaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initBaseData();
	}

	private void initBaseData(){
		phoneUtil = new PhoneUtil(context);
		density = phoneUtil.getDensity();
	}
	
	public BaseViewInfo getInfo() {
		return info;
	}

	public void setOnViewDataChangeListener(OnViewDataChangeListener onViewDataChangeListener) {
		this.onViewDataChangeListener = onViewDataChangeListener;
	}

	public void setInfo(BaseViewInfo info,ViewCreator viewCreator) {
		this.info = info;
		this.submitValues = info.getSubmitValues();
		this.viewCreator = viewCreator;
	}
	
	protected void initView(){}
	
	public void showView(){}
	
	public void setContent(CharSequence content){}

	public void initOwn(){}

	public Object getContent(){
		return null;
	}

	public void updateViews(){};

	public interface OnViewDataChangeListener{
		void dateChange();
	}

}
