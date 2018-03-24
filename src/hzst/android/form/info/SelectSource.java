package hzst.android.form.info;

import java.io.Serializable;
import java.util.List;
/**
 * {@link hzst.android.form.SelectSourceListActivity}选择数据列表中数据的实体类
 * @author wt
 *
 */
public class SelectSource implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * {@link hzst.android.form.view.FMSelectView}中数据所对应的数据库id
	 */
	private String id;
	/**
	 * {@link hzst.android.form.view.FMSelectView}中的数据
	 */
	private String itemName;
	/**
	 * 用于匹配于表单中与该{@link hzst.android.form.view.FMSelectView}相关联的控件(根据字段名来匹配)
	 */
	private List<SourceValue> sourceValues;
	//是否被选择，多选的时候会用到
	private boolean isSelected;

	private int position;
	/**
	 * 信息用途{@link hzst.android.form.info.SelectSource.SourceValue#use}的值
	 */
	public static final String USE_SHOW = "show";
	public static final String USE_PASS_BACK = "passBack";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public List<SourceValue> getSourceValues() {
		return sourceValues;
	}
	public void setSourceValues(List<SourceValue> sourceValues) {
		this.sourceValues = sourceValues;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * 1.{@link #itemName}的相关信息，比如固定资产中"标准滤光片"的型号规格、设备编号、生产厂家等；
	 * 2.表单中匹配与其字段名相同的控件(这些控件自然是和{@link hzst.android.form.view.FMSelectView}中的数据有关联，但这里我们并不需要关心)，作用是为该控件赋值。
	 */
	public class SourceValue implements Serializable{
		private static final long serialVersionUID = 1L;
		
		private String field;//数据库中的字段名
		private String title;//该信息条目的名称
		private String content;//该信息条目的具体内容
		private String use;//信息用途。包括展示、回传等。
		
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}

		public String getUse() {
			return use;
		}

		public void setUse(String use) {
			this.use = use;
		}
	}
}
