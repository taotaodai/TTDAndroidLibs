package hzst.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化对象，保存在本地SharedPreferences中；
 * 反序列化对象，从本地SharedPreferences中去除对象。
 * @author wt
 *
 */
public class SharedPreferencesUtil {
	
	private SharedPreferences sharedPreferences;

	public SharedPreferencesUtil(Context context,String preferencesName) {
		this.sharedPreferences = context.getSharedPreferences(preferencesName, Activity.MODE_PRIVATE);
	}

	public SharedPreferences getSharedPreferences() {
		return sharedPreferences;
	}

	public SharedPreferencesUtil(Context context,SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
	}
	@Deprecated
	public void writeImage(Bitmap bitmap) throws Throwable {
		 Editor editor = sharedPreferences.edit();
		 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		 String imageString = imgEncodingToBase64String(bitmap);
		 editor.putString("image", imageString);
		 editor.commit();
		 byteArrayOutputStream.close();    
	}
	@Deprecated
	public void readImage(ImageView imageView) throws Throwable {
		 String string = sharedPreferences.getString("image", "");
		 byte[] imageBytes = Base64.decode(string.getBytes(), Base64.DEFAULT);
		 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
		 imageView.setImageDrawable(Drawable.createFromStream(byteArrayInputStream, "image"));
		 byteArrayInputStream.close();
	}
		 	 
	public void writeObj(Object obj,String key) throws Throwable {		 
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(obj);
		Editor editor = sharedPreferences.edit();
		String mobilesString = new String(Base64.encode(byteArrayOutputStream.toByteArray(),Base64.DEFAULT));
		editor.putString(key, mobilesString);
		editor.commit();
		objectOutputStream.close();
	}
	
	public Object readObj(String key){
	 
		String mobilesString = sharedPreferences.getString(key, "");
		if("".equals(mobilesString)){
			return null;
		}
		byte[] mobileBytes = Base64.decode(mobilesString.getBytes(), Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
		 
		ObjectInputStream objectInputStream;
		try {
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			objectInputStream.close();
			return objectInputStream.readObject();
		} catch (Exception e) {
			return null;
		}
	}

	public void remove(String key){
		Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * @param bitmap
	 * @deprecated 已经转移到
	 * {@link FileUtil}中
	 * @return
	 */
	@Deprecated
	public static String imgEncodingToBase64String(Bitmap bitmap){
		ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 50, byteArrayOutputStream);
		String s = new String(Base64.encode(byteArrayOutputStream.toByteArray(),Base64.DEFAULT));
		try {
			byteArrayOutputStream.close();
		} catch (IOException e) {
		}
		return s;
	}

	public static Object deepCopy(Object src) {
		Object dest = null;
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(src);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			dest = in.readObject();
		} catch (Exception e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
		return dest;
	}
}
