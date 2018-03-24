package hzst.android.form.info;

import android.text.InputType;
import android.widget.EditText;

import hzst.android.form.entity.EditViewOwn;

/**
 * 可编辑控件(包括：{@link #FMEditText} {@link #FMSelectView} 等)绑定的数据
 * @author wt
 *
 */
public class EditViewInfo extends BaseViewInfo{
	private static final long serialVersionUID = 1L;

	protected String inputType;
	protected int maxEms;
	private EditViewOwn own;

	public static final String FIELD_INPUT_TYPE = "inputType";

	public static final String FIELD_UNIT = "unit";
	public static final String INPUT_NUMBER = "Number";
	public static final String INPUT_TEXT = "String";
	public static final String INPUT_DECIMAL = "Decimal";


	public EditViewOwn getOwn() {
		return own;
	}

	public void setOwn(EditViewOwn own) {
		this.own = own;
	}

	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public int getMaxEms() {
		return maxEms;
	}
	public void setMaxEms(int maxEms) {
		this.maxEms = maxEms;
	}
	
	public static void setInputType(EditText et,String inputType){
		if(INPUT_NUMBER.equals(inputType)){
			et.setInputType(InputType.TYPE_CLASS_NUMBER);
		}else if(INPUT_DECIMAL.equals(inputType)){
			et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
		}
		
	}
	
}
