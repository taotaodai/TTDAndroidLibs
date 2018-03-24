package hzst.android.entity;

import android.net.Uri;

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
	private String ownerId;//附件所有人id
	private String accessoryId;
	private Uri uri;
	private boolean isUploaded;//是否已上传，只有在上传过程中会用到
	private boolean isLocalFile;
	
	public Accessory(String attachmentUrl,boolean isLocalFile) {
		this.attachmentUrl = attachmentUrl;
		this.attachmentName = attachmentUrl.substring(attachmentUrl.lastIndexOf("/")+1);
		this.isLocalFile = isLocalFile;
	}
	public Accessory(Uri uri,String attachmentUrl,boolean isLocalFile) {
		this.uri = uri;
		this.attachmentUrl = attachmentUrl;
		this.attachmentName = attachmentUrl.substring(attachmentUrl.lastIndexOf("/")+1);
		this.isLocalFile = isLocalFile;
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

	public boolean isLocalFile() {
		return isLocalFile;
	}

	public void setIsLocalFile(boolean isLocalFile) {
		this.isLocalFile = isLocalFile;
	}

	public Uri getUri() {
		return uri;
	}

	public void setUri(Uri uri) {
		this.uri = uri;
	}
}
