package hzst.android.form.info;


import hzst.android.entity.Accessory;

import java.util.ArrayList;
import java.util.List;
/**
 * {@link #FMAccessoryView}绑定的数据
 * @author wt
 *
 */
public class AccessoryViewInfo extends BaseViewInfo{
	private static final long serialVersionUID = 1L;
	
	private List<String> accessoryList;
	private List<Accessory> accessories;

	public static final String AC_ACCESSORYID = "accessoryId";
	public static final String AC_FILENAME = "fileName";
	public static final String AC_FILEPATH = "filePath";
	public static final String AC_OWNERID = "ownerId";
	
	public List<String> getAccessoryList() {
		return accessoryList;
	}
	public void setAccessoryList(List<String> accessoryList) {
		this.accessoryList = accessoryList;
	}
	public List<Accessory> getAccessories() {
		return accessories;
	}
	public void setAccessories(List<Accessory> accessories) {
		this.accessories = accessories;
		
		accessoryList = new ArrayList<String>();
		for (Accessory accessory : accessories) {
			accessoryList.add(accessory.getAttachmentUrl());
		}
	}
	
}
