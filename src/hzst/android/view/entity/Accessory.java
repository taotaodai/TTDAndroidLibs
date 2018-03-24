package hzst.android.view.entity;

import java.io.Serializable;

/**
 * 附件实体
 * @author wt
 *
 */
public class Accessory implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String attachmentName;
	private String attachmentUrl;
	private String ownerId;
	private String accessoryId;
	private boolean isUploaded;
	
	public Accessory(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
		this.attachmentName = attachmentUrl.substring(attachmentUrl.lastIndexOf("/")+1);
	}
	public Accessory() {
	}
	
	public boolean isUploaded() {
		return isUploaded;
	}
	public void setUploaded(boolean isUploaded) {
		this.isUploaded = isUploaded;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public String getAttachmentUrl() {
		return attachmentUrl;
	}
	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}
	public String getAccessoryId() {
		return accessoryId;
	}
	public void setAccessoryId(String accessoryId) {
		this.accessoryId = accessoryId;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
}	
