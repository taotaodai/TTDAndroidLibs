package hzst.android.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import hzst.android.Constants;
import hzst.android.R;
import hzst.android.view.HorizontalProgressBar;
import hzst.android.entity.VersionInfo;

/**
 * 版本更新。因为每个应用获取版本信息的方式和获取到的参数可能不一样，所以该类不提供获取版本信息的功能。
 * 1-检查版本号，提示下载；
 * 2-下载，完成安装。
 * @author wt
 *
 */
public class UpdateManager {
	private Context mContext;
	private Activity activity;
	
	private boolean interceptFlag = false;
	 /* 下载包安装路径 */
    private String savePath = "";
    private String saveFileName = "";
    private int progress;
//	private String downloadUrl;
	private String apkName;

	private Dialog noticeDialog;
	private Dialog downloadDialog;
	private TextView tvUpdateInfo;

    /* 进度条与通知ui刷新的handler和msg常量 */
    private HorizontalProgressBar mProgress;
    private VersionInfo versionInfo;
    
    private Thread downLoadThread;

    private static final int DOWN_UPDATE = 1; 
    private static final int DOWN_OVER = 2;
    
    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
    	};
    };
    
	public UpdateManager(Context context) {
		this.mContext = context;
		this.activity = (Activity)context;
	}
	
	public void update(String savePath,VersionInfo versionInfo){
		String apkName = versionInfo.getDownloadUrl().substring(versionInfo.getDownloadUrl().lastIndexOf("/"));
		update(savePath, apkName, versionInfo);
	}
	
	/**
	 * 检查更新。当外部获取到版本信息时调用这个方法。
	 * @param savePath
	 * @param apkName
	 * @param versionInfo
	 */
	public void update(String savePath,String apkName,VersionInfo versionInfo){
		
		this.savePath = savePath;
		this.apkName = apkName+".apk";
		this.versionInfo = versionInfo;
		int newVersion = versionInfo.getVersionNum();
		try {
			if(newVersion > getVersion()){
				if(Constants.TRUE.equals(versionInfo.getMustUpdate())){
					showDownloadDialog();
				}else{
					showNoticeDialog();
				}
			}
		} catch (NumberFormatException e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
	}
	/**
	 * 提示是否下载
	 */
	private void showNoticeDialog(){

		AlertDialog.Builder builder = new Builder(mContext);
		builder.setCancelable(false);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View hintView = inflater.inflate(R.layout.dialog_update_hint, null);
		tvUpdateInfo = (TextView) hintView.findViewById(R.id.tv_update_info);
		tvUpdateInfo.setText(versionInfo.getRemark());
		builder.setView(hintView);
		builder.setPositiveButton("下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(Constants.TRUE.equals(versionInfo.getMustUpdate())){
					((Activity)mContext).finish();
				}
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}
	/**
	 * 显示更新进度
	 */
	private void showDownloadDialog(){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setCancelable(false);
		
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.dialog_progress, null);
		mProgress = (HorizontalProgressBar)v.findViewById(R.id.pb_down);
		
		builder.setView(v);
		if(!Constants.TRUE.equals(versionInfo.getMustUpdate())){
			builder.setNegativeButton("取消", new OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					interceptFlag = true;
					
				}
			});
		}

		downloadDialog = builder.create();
		downloadDialog.show();
		
		downloadApk();
	}
	/**
	 * 下载应用
	 */
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			try {
				URL url = new URL(versionInfo.getDownloadUrl());
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
								
				File saveFile = new File(savePath);
				saveFileName = savePath + apkName;
				
				if(!saveFile.exists()){
					saveFile.mkdirs();
				}
				File ApkFile = new File(saveFileName);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				
				int count = 0;
				byte buf[] = new byte[1024];
				
				do{
		    		int numread = is.read(buf);
		    		count += numread;
		    	    progress =(int)(((float)count / length) * 100);
		    	    //更新进度
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){
		    			//下载完成通知安装
		    			mHandler.sendEmptyMessage(DOWN_OVER);
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);//点击取消就停止下载.
				
				fos.close();
				is.close();

			} catch (Exception e) {
				L.showLogInfo(L.TAG_EXCEPTION, e.toString());
			}
		}
	};

	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					String sdpath = Environment.getExternalStorageDirectory() + "/";
//					mSavePath = sdpath + "download";
					URL url = new URL(versionInfo.getDownloadUrl());
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();

					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(savePath,apkName);
					saveFileName = savePath + apkName;
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					byte buf[] = new byte[1024];
					do {
						int numread = is.read(buf);
						count += numread;
						progress = (int) (((float) count / length) * 100);
						mHandler.sendEmptyMessage(DOWN_UPDATE);
						if (numread <= 0) {
							mHandler.sendEmptyMessage(DOWN_OVER);
							break;
						}

						fos.write(buf, 0, numread);
					} while (!interceptFlag);
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			downloadDialog.dismiss();
		}
	};
	
	 /**
     * 下载apk
     */
	private void downloadApk(){
//		downLoadThread = new Thread(mdownApkRunnable);
//		downLoadThread.start();
		new downloadApkThread().start();
	}
	 /**
     * 安装apk
     */
	private void installApk(){
		File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        SysIntentUtil intentUtil = new SysIntentUtil(mContext); 
        intentUtil.installApk(saveFileName);
        activity.finish();
	}
	/**
	 * 获取当前应用内部版本号
	 * @return
	 */
	@Deprecated
	public int getVersion() {
		PackageManager manager = mContext.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(mContext.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
		int version = info.versionCode;
		return version;
	}

	/**
	 * 获取应用内部版本号
	 * @param context
	 * @return
	 */
	public static int getVersion(Context context) {
		PackageManager manager = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
		int version = info.versionCode;
		return version;
	}

	/**
	 * 获取应用版本名称(版本号)
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context){
		PackageManager manager = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
		String version = info.versionName;
		return version;
	}
	
//	public interface OnVersionUpdateListener{
//		VersionInfo getVersionInfo();
//		void noNewerVersion();
//	}
}

