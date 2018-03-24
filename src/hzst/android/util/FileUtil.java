package hzst.android.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

/**
 * 本地文件操作类。
 * @author wt
 *
 */
public class FileUtil {
	private Context context;
	private String filePath;
	
	public static final int UNIT_KB = 1024;
	public static final int UNIT_MB = UNIT_KB*1024;
	public static final int UNIT_GB = UNIT_MB*1024;

	/**
	 * sd卡的根目录
	 */
	public static String SD_PATH = Environment.getExternalStorageDirectory().getPath();
	private static final String[][] MIME_MapTable={
		    //{后缀名，    MIME类型}
		    {"3gpp",    "video/3gpp"},
			{"amr","audio/x-mpeg"},
		    {"apk",    "application/vnd.android.package-archive"},
//		    {".asf",    "video/x-ms-asf"},
		    {"avi",    "video/x-msvideo"},
//		    {".bin",    "application/octet-stream"},
//		    {".bmp",      "image/bmp"},
//		    {".c",        "text/plain"},
//		    {".class",    "application/octet-stream"},
//		    {".conf",    "text/plain"},
//		    {".cpp",    "text/plain"},
		    {"doc",    "application/msword"},
		    {"docx",    "application/msword"},
//		    {".exe",    "application/octet-stream"},
		    {"gif",    "image/gif"},
//		    {".gtar",    "application/x-gtar"},
//		    {".gz",        "application/x-gzip"},
//		    {".h",        "text/plain"},
//		    {".htm",    "text/html"},
//		    {".html",    "text/html"},
//		    {".jar",    "application/java-archive"},
//		    {".java",    "text/plain"},
		    {"jpeg",    "image/jpeg"},
		    {"jpg",    "image/jpeg"},
//		    {".js",        "application/x-javascript"},
//		    {".log",    "text/plain"},
//		    {".m3u",    "audio/x-mpegurl"},
//		    {".m4a",    "audio/mp4a-latm"},
//		    {".m4b",    "audio/mp4a-latm"},
//		    {".m4p",    "audio/mp4a-latm"},
//		    {".m4u",    "video/vnd.mpegurl"},
//		    {".m4v",    "video/x-m4v"},    
//		    {".mov",    "video/quicktime"},
//		    {".mp2",    "audio/x-mpeg"},
		    {"mp3",    "audio/x-mpeg"},
		    {"mp4",    "video/mp4"},
//		    {".mpc",    "application/vnd.mpohun.certificate"},        
//		    {".mpe",    "video/mpeg"},    
//		    {".mpeg",    "video/mpeg"},    
//		    {".mpg",    "video/mpeg"},    
//		    {".mpg4",    "video/mp4"},    
//		    {".mpga",    "audio/mpeg"},
//		    {".msg",    "application/vnd.ms-outlook"},
//		    {".ogg",    "audio/ogg"},
		    {"pdf",    "application/pdf"},
		    {"png",    "image/png"},
//		    {".pps",    "application/vnd.ms-powerpoint"},
		    {"ppt",    "application/vnd.ms-powerpoint"},
//		    {".prop",    "text/plain"},
//		    {".rar",    "application/x-rar-compressed"},
//		    {".rc",        "text/plain"},
		    {"rmvb",    "audio/x-pn-realaudio"},
//		    {".rtf",    "application/rtf"},
//		    {".sh",        "text/plain"},
//		    {".tar",    "application/x-tar"},    
//		    {".tgz",    "application/x-compressed"}, 
		    {"txt",    "text/plain"},
//		    {".wav",    "audio/x-wav"},
//		    {".wma",    "audio/x-ms-wma"},
//		    {".wmv",    "audio/x-ms-wmv"},
//		    {".wps",    "application/vnd.ms-works"},
		    {"xls", "application/vnd.ms-excel"}, 
		    {"xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}, 
		    {"xml",    "text/xml"},
//		    {".xml",    "text/plain"},
//		    {".z",        "application/x-compress"},
//		    {".zip",    "application/zip"},
		    {"",        "*/*"}};
	
	public FileUtil(Context context,String filePath){
		this.context = context;
		if(filePath != null && "".equals(filePath)){
			this.filePath = filePath;
		}else{
			filePath = SD_PATH;
		}
	}
	/**
	 * 保存在SD卡下
	 * @param context
	 */
	public FileUtil(Context context){
		this.context = context;
		filePath = SD_PATH;
	}

	/**
	 * 检查SD卡是否可用
	 * @return
	 */
	public boolean isSdCardEnable(){
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(context, "SD卡不可用，请检查", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	/**
	 * 保存图片
	 * @param fileName 
	 * @param bitmap   
	 * @throws IOException
	 */
	public void savaBitmap(String fileName, Bitmap bitmap){
		if(!isSdCardEnable()){
			return;
		}
		if(bitmap == null){
			return;
		}
		File folderFile = new File(filePath);
		
		if(!folderFile.exists()){
			folderFile.mkdirs();
		}
		File file = new File(getFullPath(fileName));
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			L.showLogInfo(L.TAG_EXCEPTION,e.toString());
		}
	}
	
	/**
	 * 从手机或者sd卡获取Bitmap
	 * @param fileName
	 * @return
	 */
	public Bitmap getBitmap(String fileName){
		try {
			return BitmapFactory.decodeFile(getFullPath(fileName));
		} catch (Exception e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
		return null;
	}

	/**
	 * 从assets目录下获取图片
	 * @param fileName
	 * @return
	 */
	public Bitmap getImageFromAssetsFile(String fileName){
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try{
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e){
            L.showLogInfo(L.TAG_EXCEPTION,e.toString());
        }
        return image;
    }
	public String getTextFromAssetsFile(String fileName){
		try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
		return "";
	}
	
	
	
	/**
	 * 判断文件是否存在
	 * @param fileName
	 * @return
	 */
	public boolean isFileExists(String fileName){
		return new File(getFullPath(fileName)).exists();
	}
	
	/**
	 * 获取文件的大小
	 * @param fileName
	 * @return
	 */
	public long getFileSize(String fileName) {
		return new File(getFullPath(fileName)).length();
	}
	
	
	/**
	 * 读取文件
	 * @param fileName 
	 * @return
	*/
	public String read(String fileName) {
		InputStreamReader read = null;
		try {
			File f = new File(getFullPath(fileName));
			if (f.isFile() && f.exists()) {
				read = new InputStreamReader(
				new FileInputStream(f),"UTF-8");
				
				return read(read);
			}
	
		} catch (Exception e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}finally{
			try {
				read.close();
			} catch (IOException e) {
			}
		}
		return null;
			
	}
	
	public static String read(InputStreamReader isr){
		String readTxt = "";
		try {
			BufferedReader reader = new BufferedReader(isr);
			String line;
			while ((line = reader.readLine()) != null) {
				readTxt += line;
			}
		} catch (IOException e) {
		}
		
		return readTxt;
	}
	

	/**
	 * 写入文件
	 * @param writeText
	 * 文件路径
	*/
	public void write(String fileName,String writeText) {
		try {
		File file = createFile(fileName);
		
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
		BufferedWriter writer = new BufferedWriter(write);
		writer.write(writeText);
		writer.close();
		} catch (Exception e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
	}
	
    /**
     * 追加文件：使用RandomAccessFile
     */
    public void writeFileAppend(String fileName, String writeText) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(getFullPath(fileName), "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.write(writeText.getBytes("utf-8"));
            randomFile.close();
        } catch (IOException e) {
        	L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
    }

	/**
	 * 复制文件到指定目录
	 * @param fileName
	 * @param toPath
	 */
    public void copy(String fileName,String toPath){
		File file = new File(getFullPath(fileName));
    	if(file.exists()){
    		try {
				FileInputStream fis = new FileInputStream(file);
				createDirIfNotExists(getFullPath(toPath));
				FileOutputStream fos = new FileOutputStream(getFullPath(getFullPath(toPath)+getFileName(file.getPath())));
				byte[] buffer = new byte[UNIT_KB];
				while(fis.read(buffer) != -1){
					fos.write(buffer);
				}
				fis.close();
				fos.close();
			} catch (Exception e) {
				L.showLogInfo(L.TAG_EXCEPTION,e.toString());
			}
    	}else{
			L.showLogInfo(L.TAG_OTHER_INFO,"文件不存在："+file.getPath());
		}
    }
	
    /** 
     * 获得指定文件的byte数组 
     */  
    public byte[] getFileBytes(String fileName){
        byte[] buffer = null;  
        try {  
            File file = new File(getFullPath(fileName));
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();
            bos.close();
            buffer = bos.toByteArray();  
        } catch (Exception e) {  
        	L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        return buffer;  
    }

	/**
	 * 选择已安装的应用打开对应格式的文件
	 * @param file
	 */
	public void openFile(File file){
		Intent intent = new Intent(); 
		intent.setAction(android.content.Intent.ACTION_VIEW); 
		intent.setDataAndType(Uri.fromFile(file), getFileType(file.getName()));
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(context, "系统不支持该文件类型", Toast.LENGTH_SHORT).show();
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
	}
	public void openFile(String fileName){
		File file = new File(getFullPath(fileName));
		if(!file.exists()){
			Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), getFileType(file.getName()));
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(context, "系统不支持该文件类型", Toast.LENGTH_SHORT).show();
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
	}

	 /**
	  * 扫描指定文件
	  * @param fileName
	  */
	public void scanFileAsync(String fileName) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(getFullPath(fileName))));
        context.sendBroadcast(scanIntent);
	}
	
	/**
	 * 获取文件后缀名
	 * @param fileName
	 * @return
	 */
	public static String getFileType(String fileName){
		String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
		String type = "";
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if(MIME_MapTable[i][0].equals(fileType)){
				type = MIME_MapTable[i][1];
			}
		}
		return type;		
	}

	/**
	 * 创建文件夹
	 * @param filePath 完整路径
	 * @return
	 */
	public static File createDirIfNotExists(String filePath){
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 创建文件
	 * @param fileName 文件名或者相对于{@link #filePath}下的路径
	 * @return
	 */
	public File createFile(String fileName){
		String fullPath;
		int index = fileName.lastIndexOf(File.separator);
		if(index >= 0){
			if(index == fileName.length()-1){//已"/"结尾，只创建文件夹
				return createDirIfNotExists(filePath + fileName);
			}else{
				createDirIfNotExists(filePath + fileName.substring(index));
				fullPath = filePath + fileName;
			}
		}else{
			fullPath = filePath + fileName;
		}
		File file = new File(fullPath);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				L.showLogInfo(L.TAG_EXCEPTION,e.toString());
				return null;
			}
		}
		return file;
	}
	/**
	 * 截取路径中的文件名
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath){
		return filePath.substring(filePath.lastIndexOf(File.separator)+1);
	}

	/**
	 * 把图片编码为Base64格式字符串
	 * @param bitmap
	 * @return
	 */
	public static String imgEncodingToBase64String(Bitmap bitmap){
		ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
		String s = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
		try {
			byteArrayOutputStream.close();
		} catch (IOException e) {
		}
		return s;
	}

	private String getFullPath(String fileName){
		if(fileName.startsWith(File.separator)){
			return filePath+fileName;
		}
		return filePath+File.separator+fileName;
	}
	
}
