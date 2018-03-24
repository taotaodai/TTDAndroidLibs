package hzst.android.form.info;

import hzst.android.util.JsonDataParser;
import hzst.android.util.L;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 表格控件{@link hzst.android.form.view.FMFormView}绑定数据
 * @author wt
 *
 */
public class FormViewInfo extends BaseViewInfo{
	private static final long serialVersionUID = 1L;
	
	private List<List<FormSubmitValue>> formValues;
	private boolean fissionable;
	private FRF frf;
	private SubmitValue frfData;

	public static final String FIELD_FISSIONABLE = "fissionable";//表单可分裂。即添加一个兄弟表单。
	public static final String FIELD_FRF = "frf";//表单之间的关系，一般用于表单和表单的计算。
	/**
	 * {@link #FIELD_SUBMIT_ITEMS} 中的字段-------------------------------------------------------
	 */
	public static final String SUBMIT_BROAD = "broadName";//表格标题
	public static final String SUBMIT_ITEM = "itemName";//单元格标题
	public static final String SUBMIT_INPUT_TYPE = "inputType";//输入值的类型
	public static final String SUBMIT_ISFISSION = "isFission";
	/**
	 * 单元格联动操作
	 */
	/**
	 * 数学表达式，用于表格中计算
	 */
	public static final String SUBMIT_MATH_EXPRESSION = "expression";

	/**
	 * --------------------------------------------------------------------------------------------
	 */

	public static final String INPUT_NUM = "num";//整数
	public static final String INPUT_TEXT = "text";

	public FormViewInfo(){}
	public FormViewInfo(String submitInfo) {
		setSubmitInfo(submitInfo);
		parseSubmitInfo();
	}

	public boolean isFissionable() {
		return fissionable;
	}

	public void setFissionable(boolean fissionable) {
		this.fissionable = fissionable;
	}

	public void setFrfData(SubmitValue frfData) {
		this.frfData = frfData;
	}


	@Override
	public void parseSubmitInfo() {
		super.parseSubmitInfo();
		
		formValues = new ArrayList<>();
		String temp = "";
		List<FormSubmitValue> list = null;
		try {
			JSONArray array = new JSONArray(submitInfo);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				FormSubmitValue value = new FormSubmitValue();
				BaseViewInfo.SubmitValue bSv = submitValues.get(i);
				value.setField(bSv.getField());
				value.setValue(bSv.getValue());
				value.setValueType(bSv.getValueType());
				value.setJoint(bSv.getJoint());
				value.setBroadName(obj.getString(SUBMIT_BROAD));
				value.setItemName(obj.getString(SUBMIT_ITEM));
				value.setInputType(obj.getString(SUBMIT_INPUT_TYPE));
				/**
				 * 非必要参数
				 */
				value.setExpression(JsonDataParser.parseJsonElement(obj, SUBMIT_MATH_EXPRESSION));
				value.setIsFission("1".equals(JsonDataParser.parseJsonElement(obj, SUBMIT_ISFISSION)));

				submitValues.set(i,value);

				/*
				 * 相同broadName
				 */
				if("".equals(temp) || !value.getBroadName().equals(temp)){
					if(list != null){
						formValues.add(list);
					}
					list = new ArrayList<>();
					temp = value.getBroadName();
				}
				list.add(value);
			}
			if(list.size() > 0){
				formValues.add(list);
			}
		} catch (JSONException e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}

	}

	@Override
	public List<SubmitValue> getSubmitValues() {
		if(frfData != null){
			super.getSubmitValues().add(frfData);
		}
		return super.getSubmitValues();
	}

	@Override
	public void setSubmitInfo(String submitInfo) {
		this.submitInfo = submitInfo;
	}


	public List<List<FormSubmitValue>> getFormValues() {
		return formValues;
	}

	public void setFormValues(List<List<FormSubmitValue>> formValues) {
		this.formValues = formValues;
	}

	public FRF getFrf() {
		return frf;
	}

	public void setFrf(FRF frf) {
		this.frf = frf;
	}

	public class FormSubmitValue extends SubmitValue implements Serializable{
		private static final long serialVersionUID = 1L;
		/**
		 * 控件依赖的参数
		 */
		private String broadName = "";
		private String itemName = "";
		private String inputType = FormViewInfo.INPUT_TEXT;//默认输入类型

		private String expression;
		/**
		 * 其他参数
		 */
		private int formPosition;//小表格下标
		private int cellPosition;//单元格下标
		private boolean isFission = false;//是否是原表格分裂出来的。用于判断能否删除。

		public FormSubmitValue(){}
		public FormSubmitValue(String fieldName,String value,String valueType,String broadName,String itemName){
			this.field = fieldName;
			this.value = value;
			this.valueType = valueType;
			this.broadName = broadName;
			this.itemName = itemName;
		}

		public FormSubmitValue(String fieldName,String value,String valueType,String broadName,String itemName,boolean isFission){
			this.field = fieldName;
			this.value = value;
			this.valueType = valueType;
			this.broadName = broadName;
			this.itemName = itemName;
			this.isFission = isFission;
		}

		public FormSubmitValue(String fieldName,String value,String valueType,String broadName,String itemName,
							   String inputType){
			this.field = fieldName;
			this.value = value;
			this.valueType = valueType;
			this.broadName = broadName;
			this.itemName = itemName;
			this.inputType = inputType;
		}

		public FormSubmitValue(String fieldName,String value,String valueType,String broadName,String itemName,
							   String inputType,String expression){
			this.field = fieldName;
			this.value = value;
			this.valueType = valueType;
			this.broadName = broadName;
			this.itemName = itemName;
			this.inputType = inputType;
			this.expression = expression;
		}
		public String getBroadName() {
			return broadName;
		}
		public void setBroadName(String broadName) {
			this.broadName = broadName;
		}
		public String getItemName() {
			return itemName;
		}
		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public String getInputType() {
			return inputType;
		}

		public void setInputType(String inputType) {
			this.inputType = inputType;
		}

		public int getFormPosition() {
			return formPosition;
		}

		public void setFormPosition(int formPosition) {
			this.formPosition = formPosition;
		}

		public int getCellPosition() {
			return cellPosition;
		}

		public void setCellPosition(int cellPosition) {
			this.cellPosition = cellPosition;
		}

		public String getExpression() {
			return expression;
		}

		public void setExpression(String expression) {
			this.expression = expression;
		}

		public boolean isFission() {
			return isFission;
		}

		public void setIsFission(boolean isFission) {
			this.isFission = isFission;
		}
	}

	public class FRF implements Serializable{
		public static final String CALCULATE_SUM = "sum";

		private String field;
		private String targetName;
		private int targetIndex;
		private String calculateType;

		public FRF(String field,String targetName,int targetIndex,String calculateType){
			this.field = field;
			this.targetName = targetName;//单元格的名称
			this.targetIndex = targetIndex;//控件所在表格中的下标
			this.calculateType = calculateType;
		}
		public String getTargetName() {
			return targetName;
		}

		public void setTargetName(String targetName) {
			this.targetName = targetName;
		}

		public int getTargetIndex() {
			return targetIndex;
		}

		public void setTargetIndex(int targetIndex) {
			this.targetIndex = targetIndex;
		}

		public String getCalculateType() {
			return calculateType;
		}

		public void setCalculateType(String calculateType) {
			this.calculateType = calculateType;
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}
	}
	
}
