package hzst.android.entity;
/**
 * 通用的Url参数
 * @author Administrator
 *
 */
public class UrlPRM {
	private int pageSize;
	private int pageIndex;
	
	private String k;
	
	public UrlPRM(int pageSize,int pageIndex) {
		this.pageSize = pageSize;
		this.pageIndex = pageIndex;
	}
	public UrlPRM(int pageSize,int pageIndex,String keyWord) {
		this.pageSize = pageSize;
		this.pageIndex = pageIndex;
		this.k = keyWord;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public int getCurrentPage(){
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getK() {
		return k;
	}
	public void setK(String k) {
		this.k = k;
	}
	public String getTitle(){
		return k;
	}
	
}
