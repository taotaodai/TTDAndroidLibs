package hzst.android.form.info;

import hzst.android.util.L;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 可选控件(包括:{@link hzst.android.form.view.FMCheckGroup} {@link hzst.android.form.view.FMSpinner})绑定的数据
 * @author wt
 *
 */
public class SelectableViewInfo extends BaseViewInfo{ 
	
	private static final long serialVersionUID = 1L;
	private List<ListItem> listItems;
	private List<String> listText;//显示的值
	private List<String> listValue;//字段对应的值
	
	private int position;//当前选中项的下标
	private boolean isMultiple;

	public static final String FEILD_TEXT = "text";
	public static final String FEILD_VALUE = "value";
	public static final String FEILD_ISCHECKED = "isChecked";
	public static final String FIELD_ISMULTIPLE = "isMultiple";

	
	public List<ListItem> getListItems() {
		return listItems;
	}
	public void setListItems(List<ListItem> listItems) {
		this.listItems = listItems;
	}
	
	@Override
	public void setListInfo(String listInfo) {
		super.setListInfo(listInfo);
		parseListItems();
	}

	public boolean isMultiple() {
		return isMultiple;
	}

	public void setIsMultiple(boolean isMultiple) {
		this.isMultiple = isMultiple;
	}

	public List<String> getListText() {
		return listText;
	}
	public void setListText(List<String> listText) {
		this.listText = listText;
	}
	public List<String> getListValue() {
		return listValue;
	}
	public void setListValue(List<String> listValue) {
		this.listValue = listValue;
	}
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public void parseListItems(){
		listItems = new ArrayList<>();
		if(listInfo != null){
			try {
				JSONArray array = new JSONArray(listInfo);
				
				listText = new ArrayList<>();
				listValue = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					ListItem item = new ListItem();
					String text = obj.getString(FEILD_TEXT);
					String value = obj.getString(FEILD_VALUE);
					listText.add(text);
					listValue.add(value);
					item.setText(text);
					item.setValue(value);
					if(obj.has(FEILD_ISCHECKED)){
						item.setIsChecked(obj.getBoolean(FEILD_ISCHECKED));
					}
					listItems.add(item);
				}
			} catch (JSONException e) {
				L.showLogInfo(L.TAG_EXCEPTION, e.toString());
			}
		}
	}
	
	
	/**
	 * 可选择控件的数据
	 *
	 */
	public class ListItem implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		private String text;//显示的值
		private String value;//需要返回的值

		private boolean isChecked;//是否选中

		public ListItem(){}
		public ListItem(String text,String value){
			this.text = text;
			this.value = value;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}

		public boolean isChecked() {
			return isChecked;
		}

		public void setIsChecked(boolean isChecked) {
			this.isChecked = isChecked;
		}
	}
}
