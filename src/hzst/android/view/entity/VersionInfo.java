package hzst.android.view.entity;

import hzst.android.util.UpdateManager;

/**
 * 版本信息。作为一个统一的版本信息实体，可以结合{@link UpdateManager}使用，也可以单独在其他与版本信息相关的模块中使用。
 * @author wt
 *
 */
public class VersionInfo {
	
	protected int versionNum;
	protected String downloadUrl;
	protected String remark;
	protected boolean mustUpdate;//是否强制更新
	
	public int getVersionNum() {
		return versionNum;
	}
	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public boolean isMustUpdate() {
		return mustUpdate;
	}
	public void setMustUpdate(boolean mustUpdate) {
		this.mustUpdate = mustUpdate;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	
}
