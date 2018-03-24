package hzst.android.form.info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hzst.android.Constants;
import hzst.android.util.JsonDataParser;
import hzst.android.util.L;

/**
 * 选择控件({@link hzst.android.form.view.FMSelectView})绑定的数据
 * @author wt
 *
 */
public class SelectViewInfo extends EditViewInfo{
	private static final long serialVersionUID = 1L;
	//数据源类型
	private String sourceType;
	//数据源,json数组
	private String selectSource;
	private List<SelectSource> sourceList;
	//为两位整数。第一位(0/1)用来判断是否多选；第二位(0/1/2)用来判断三种传值情况：名称+id、名称、id、。
	private String selectType;
	private boolean isMultiple;
	private String passType;
	private String sourceUrl;//选择数据获取接口

	/**
	 * {@link #sourceType}的值
	 */
	public static final String SOURCE_MEMBER = "member";
	public static final String SOURCE_SCIENTIFIC = "scientific";//课题
	public static final String SOURCE_FOLLOW = "follow";//选择数据和控件数据一起传过来

	/**
	 * 可选数据源的相关字段
	 */
	public static final String FIELD_SELECT_VALUES = "values";
	public static final String FIELD_FIELD = "field";
	public static final String FIELD_SELECT_ID = "id";
	public static final String FIELD_SELECT_USE = "use";
	public static final String FIELD_SELECT_NAME = "itemName";
	public static final String FIELD_SELECT_TITLE = "title";
	public static final String FIELD_SELECT_CONTENT = "content";

	/**
	 * 对应{@link #sourceType}
	 */
	public static final String FIELD_SOURCE_TYPE = "sourceType";//选择的数据源类型

	public static final String FIELD_SELECT_SOURCE = "selectSource";//选择的数据源
	/**
	 * 对应{@link #selectType}
	 */
	public static final String FIELD_SELECT_TYPE = "selectType";//选择的数据源是否多选、是否需要传name和id
	/**
	 * 对应{@link #sourceUrl}
	 */
	public static final String FIEDL_SELECT_SOURCEURL = "sourceUrl";

	public static final String SELECT_TYPE_MULTY = "1";
	public static final String SELECT_TYPE_SINGLE = "0";
	/**
	 * {@link #passType}的值
	 */
	public static final String PASS_TYPE_ALL = "0";
	public static final String PASS_TYPE_NAME = "1";
	public static final String PASS_TYPE_ID = "2";

	public List<SelectSource> getSourceList() {
		return sourceList;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSelectSource() {
		return selectSource;
	}

	public void setSelectSource(String selectSource) {
		this.selectSource = selectSource;
		sourceList = parseSource(selectSource);
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
		isMultiple = Constants.TRUE.equals(selectType.substring(0, 1));
		passType = selectType.substring(1);
	}

	public boolean isMultiple() {
		return isMultiple;
	}

	public String getPassType() {
		return passType;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	private static List<SelectSource> parseSource(String json) {
		List<SelectSource> selectSources = null;
		if (selectSources == null) {
			selectSources = new ArrayList<>();
		}
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				SelectSource source = new SelectSource();
				JSONObject obj = array.getJSONObject(i);
				source.setId(obj.getString(FIELD_SELECT_ID));
				source.setItemName(obj.getString(FIELD_SELECT_NAME));
				source.setPosition(i);
				List<SelectSource.SourceValue> sourceValues = new ArrayList<>();
				JSONArray valueArray = new JSONArray(obj.getString(FIELD_SELECT_VALUES));
				for (int j = 0; j < valueArray.length(); j++) {
					SelectSource.SourceValue value = source.new SourceValue();
					JSONObject valueObj = valueArray.getJSONObject(j);
                    value.setField(JsonDataParser.parseJsonElement(valueObj,FIELD_FIELD));
					value.setTitle(valueObj.getString(FIELD_SELECT_TITLE));
					value.setContent(valueObj.getString(FIELD_SELECT_CONTENT));
					sourceValues.add(value);
				}
				source.setSourceValues(sourceValues);
				selectSources.add(source);
			}
		} catch (JSONException e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
		return selectSources;
	}
	
}
