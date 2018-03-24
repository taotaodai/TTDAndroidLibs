package hzst.android.form.info;

import hzst.android.entity.BaseUser;
import hzst.android.form.view.FMBaseView;
import hzst.android.util.JsonDataParser;
import hzst.android.util.L;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;

/**
 * 表单中控件绑定数据的基类
 * @author wt
 *
 */
public class BaseViewInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	protected String viewType;
	protected String name = "";
	protected boolean mustFill;
	protected boolean visible = true;
	protected boolean readOnly = false;
	protected boolean special;
	protected boolean isNeedTitle = true;
	protected int marginType = MARGIN_PL_LINE;
	protected int index;
	protected OnHasSpecialListener specialListener;
	protected BaseUser user;
	/**
	 * /由于表单控件所需数据五花八门，即使是同一类型的控件，它们所需要的数据可能也不太一样。
	 * 这里用json数据保存控件相关的所有数据，以便需要的时候获取。
	 */
	protected JSONArray allViewData;
	
	protected String submitInfo;//SubmitValue的json
	protected List<SubmitValue> submitValues;
	protected String specialValue;//SpecialValue的json
	protected List<SpecialValue> specialValues;
	protected List<BaseViewInfo> slaveTableInfos;//从表数据
	protected Map<String, String> parameters;//从表相关的任务数据

	protected String listInfo;

	/*
	 * json中的字段
 	*/
	public static final String FIELD_TYPE = "viewType";//控件类型
	public static final String FIELD_NAME = "name";//字段对应的名称(单元格的标题)
	public static final String FIELD_SUBMIT_ITEMS = "submitData";//需要显示在控件上的数据/用户操作后需要增删改操作的数据
	public static final String FIELD_MUST_FILL = "mustFill";//是否必填(1/0)
	public static final String FIELD_READONLY = "ReadOnly";//是否只读(1/0)
	public static final String FIELD_SPECIAL = "special";//是否有特殊操作(1/0)
	public static final String FIELD_LIST_ITEMS = "selectableData";//下拉框、多选框等可选控件的选项数据
	public static final String FIELD_ACCESSORY_LIST = "accessoryList";//附件
	public static final String FIELD_ALL_VIEW_DATA = "allViewData";//控件的所有相关数据
	public static final String FIELD_OWN = "own";//控件特有的参数
	public static final String FIELD_MARGIN_TYPE = "margin";//控件之间分割线类型
	public static final String FIELD_ISNEED_TITLE = "isNeedTitle";//是否创建标题

	/*
	 * 返回服务器字段值的类型
	 */
	public static final String VALUE_TYPE_TEXT = "Text";
	public static final String VALUE_TYPE_EDIT = "Edit";
	public static final String VALUE_TYPE_HIDE = "hide";//隐藏的
	public static final String VALUE_TYPE_SV = "SelectedValue";
	public static final String VALUE_TYPE_ST = "SelectedText";

	/**
	 * 提交字段名
	 */
	public static final String SUBMIT_FIELD = "field";//数据对应字段
	public static final String SUBMIT_VALUE = "value";//字段值
	public static final String SUBMIT_TYPE = "valueType";//数据类型。一般在生成控件时用到，提交数据时不会用到
	/**
	 * 非必要
	 */
	public static final String SUBMIT_JOINT = "joint";//数据拼接方式

	public static final String JOINT_VERTICAL_BAR = "verticalBar";
	public static final String JOINT_SEMICOLON = "semicolon";
	public static final String JOINT_COMMA = "comma";

	/**
	 * 分割线
	 */
	public static final int MARGIN_NONE = 0;//无分割线
	public static final int MARGIN_PL_LINE = 1;//有左内边距的分割线
	public static final int MARGIN_LAYER_CAKE = 2;//夹心饼
	public static final int MARGIN_LINE = 3;
	public static final int MARGIN_CAKE = 4;
	public static final int MARGIN_CAKE_TOP = 5;
	public static final int MARGIN_CAKE_BOTTOM = 6;

	/**
	 * 控件横向占的比例
	 */
	public static final int LAND_HOLD_FULL = 0;
	public static final int LAND_HOLD_HALF = 1;

	public JSONArray getAllViewData() {
		return allViewData;
	}

	public void setAllViewData(String json) {
		if(!TextUtils.isEmpty(json)){
			try {
				this.allViewData = new JSONArray(json);
			} catch (JSONException e) {
				L.showLogInfo(L.TAG_EXCEPTION,e.toString());
			}
		}
	}


	public int getMarginType() {
		return marginType;
	}

	public void setMarginType(int marginType) {
		this.marginType = marginType;
	}

	public String getViewType() {
		return viewType;
	}
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public boolean isMustFill() {
		return mustFill;
	}
	public void setMustFill(boolean mustFill) {
		this.mustFill = mustFill;
	}
	
	public String getSubmitInfo() {
		return submitInfo;
	}
	public void setSubmitInfo(String submitInfo) {
		this.submitInfo = submitInfo;
		parseSubmitInfo();
	}
	public String getListInfo() {
		return listInfo;
	}
	public void setListInfo(String listInfo) {
		this.listInfo = listInfo;
	}

	public boolean isNeedTitle() {
		return isNeedTitle;
	}

	public void setIsNeedTitle(boolean isNeedTitle) {
		this.isNeedTitle = isNeedTitle;
	}

	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public OnHasSpecialListener getSpecialListener() {
		return specialListener;
	}
	public List<SubmitValue> getSubmitValues() {
		return submitValues;
	}
	public void setSubmitValues(List<SubmitValue> submitValues) {
		this.submitValues = submitValues;
	}
	public void setSpecialListener(OnHasSpecialListener specialListener) {
		this.specialListener = specialListener;
	}
	
	public Map<String, String> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public String getSpecialValue() {
		return specialValue;
	}
	public void setSpecialValue(String specialValue) {
		this.specialValue = specialValue;
		parseSpecialValue();
	}
	public List<SpecialValue> getSpecialValues() {
		return specialValues;
	}
	public void setSpecialValues(List<SpecialValue> specialValues) {
		this.specialValues = specialValues;
	}

	public BaseUser getUser() {
		return user;
	}

	public void setUser(BaseUser user) {
		this.user = user;
	}

	public List<BaseViewInfo> getSlaveTableInfos() {
		return slaveTableInfos;
	}
	public void setSlaveTableInfos(List<BaseViewInfo> slaveTableInfos) {
		this.slaveTableInfos = slaveTableInfos;
	}

	protected void parseSubmitInfo(){
		submitValues = new ArrayList<>();
		JsonDataParser.getBeanList(submitInfo, SubmitValue[].class, submitValues);
	}
	
	private void parseSpecialValue(){
		specialValues = new ArrayList<>();
		try {
			JSONArray array = new JSONArray(specialValue);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				SpecialValue value = new SpecialValue();
				value.setFieldName(obj.getString("field"));
				value.setValue(obj.getString("value"));
				specialValues.add(value);
			}
		} catch (JSONException e) {
//			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
	}

	/**
	 * 控件和服务器进行交互的主要数据，每个实例对应数据库中的一个字段。
	 *
	 */
	public class SubmitValue implements Serializable{
		private static final long serialVersionUID = 1L;
		
		protected String valueType;
		protected String field;
		protected String value = "";
		protected String joint = "";
		
		public String getValueType() {
			return valueType;
		}
		public void setValueType(String valueType) {
			this.valueType = valueType;
		}
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}

		public String getJoint() {
			return joint;
		}

		public void setJoint(String joint) {
			this.joint = joint;
		}

		public SubmitValue(){}

		public SubmitValue(String field,String value,String valueType){
			this.valueType = valueType;
			this.field = field;
			this.value = value;
		}

		/**
		 *
		 * @param field
		 * @param value
		 * @param valueType
		 * @param joint 数据拼接方式
		 */
		public SubmitValue(String field,String value,String valueType,String joint){
			this.valueType = valueType;
			this.field = field;
			this.value = value;
			this.joint = joint;
		}
	}
	/**
	 * 特殊事件的数据{@link #specialValues}
	 *
	 */
	public class SpecialValue implements Serializable{
		private static final long serialVersionUID = 1L;
		
		private String fieldName;
		private String value = "";
		
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	/**
	 * 特殊事件回调接口
	 *
	 */
	public interface OnHasSpecialListener{
		void doSpecial(Bundle bundle);
	}

}
