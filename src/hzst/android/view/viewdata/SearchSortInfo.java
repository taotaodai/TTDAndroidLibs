package hzst.android.view.viewdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hzst.android.view.SearchSortView;
/**
 * {@link SearchSortView}使用时需要绑定数据实体
 * @author wt
 *
 */
public class SearchSortInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<SearchInfo> searchValues = new ArrayList<SearchInfo>();
	private List<String> sortValues = new ArrayList<String>();
	
	public SearchSortInfo(List<String> sortValues,List<SearchInfo> searchValues) {
		this.sortValues = sortValues;
		this.searchValues = searchValues;
	}
	
	public SearchSortInfo() {
	}

	public List<SearchInfo> getSearchValues() {
		return searchValues;
	}

	public void setSearchValues(List<SearchInfo> searchValues) {
		this.searchValues = searchValues;
	}

	public List<String> getSortValues() {
		return sortValues;
	}
	public void setSortValues(List<String> sortValues) {
		this.sortValues = sortValues;
	}
	
	public class SearchInfo implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private String searchName;//搜索名称
		private String inputValue = "";//搜索关键字
		private List<String> selectableValues;//只有选择搜索时用到，作为选择数据源
		private int type;
		
		public static final int TYPE_INPUT = 1;
		public static final int TYPE_SELECT = 2;
		public static final int TYPE_DATE = 3;

		public SearchInfo(String searchName,int type) {
			this.searchName = searchName;
			this.type = type;
		}
		public SearchInfo(String inputValue,String searchName,int type) {
			this.inputValue = inputValue;
			this.searchName = searchName;
			this.type = type;
		}
		
		public SearchInfo(String searchName,List<String> selectableValues,int type) {
			this.searchName = searchName;
			this.selectableValues = selectableValues;
			this.type = type;
		}
		
		public SearchInfo(String inputValue,String searchName,List<String> selectableValues,int type) {
			this.inputValue = inputValue;
			this.searchName = searchName;
			this.selectableValues = selectableValues;
			this.type = type;
		}
		
		public String getSearchName() {
			return searchName;
		}

		public void setSearchName(String searchName) {
			this.searchName = searchName;
		}

		public String getInputValue() {
			return inputValue;
		}
		public void setInputValue(String inputValue) {
			this.inputValue = inputValue;
		}
		public List<String> getSelectableValues() {
			return selectableValues;
		}
		public void setSelectableValues(List<String> selectableValues) {
			this.selectableValues = selectableValues;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		
	}
	
}
