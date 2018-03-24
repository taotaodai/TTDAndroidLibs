package hzst.android.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * afinal框架的进一步封装
 * 1.文件下载，文件上传(支持批量上传)
 * 2.网络/本地图片显示
 * @author wt
 *
 */
public class AFinalUtil {
	private Context context;
	private OnDownLoadListener onDownLoadListener;
	private OnUploadListener onUploadListener;
	private UploadMultipleListener uploadMultipleListener;
	private int uploadIndex = 0;
	private List<String> files;
	
	public AFinalUtil(Context context) {
		this.context = context;
	}
	
	public void setOnDownLoadListener(OnDownLoadListener onDownLoadListener) {
		this.onDownLoadListener = onDownLoadListener;
	}

	public void setOnUploadListener(OnUploadListener onUploadListener) {
		this.onUploadListener = onUploadListener;
	}
	
	/**
	 * 下载并打开网络文件
	 * @param url
	 * @param target
	 */
	public void openNetRes(final String url,final String target){
		final File file = new File(target);
		final FileUtil fileUtils = new FileUtil(context);
		
		if(file.exists()){
			downloadIgnore(new OnDownloadIgnoreListener() {
				
				@Override
				public void ignore(boolean isIgnore) {
					if(isIgnore){
						FinalHttp finalHttp = new FinalHttp();
						finalHttp.download(url, target, new AjaxCallBack<File>() {
							
							@Override
							public void onSuccess(File t) {
								fileUtils.scanFileAsync(t.getPath());
								if(onDownLoadListener != null){
									onDownLoadListener.downLoadComplete();
								}
								fileUtils.openFile(t);
							}
						});
					}else{
						fileUtils.openFile(file);
					}
				}
			});

		}else{
			FinalHttp finalHttp = new FinalHttp();
			finalHttp.download(url, target, new AjaxCallBack<File>() {
				
				@Override
				public void onSuccess(File t) {
					fileUtils.scanFileAsync(t.getPath());
					if(onDownLoadListener != null){
						onDownLoadListener.downLoadComplete();
					}
					fileUtils.openFile(t);
				}
			});
		}
	}
	
	/**
	 * 下载并打开网络文件，并实时更新进度
	 * @param url
	 * @param target
	 * @param pb
	 */
	public void openNetRes(final String url,final String target,final ProgressBar pb){
		final File file = new File(target);
		final FileUtil fileUtils = new FileUtil(context);
		if(file.exists()){
			downloadIgnore(new OnDownloadIgnoreListener() {
				
				@Override
				public void ignore(boolean isIgnore) {
					if(isIgnore){
						FinalHttp finalHttp = new FinalHttp();
						
						finalHttp.download(url, target, new AjaxCallBack<File>() {

							@Override
							public void onStart() {
								if(pb != null){
									pb.setVisibility(View.VISIBLE);
								}
							}

							@Override
							public void onLoading(long count, long current) {
								if(pb != null){
									pb.setProgress(getProcess(current, count));
								}
							}
							
							@Override
							public void onSuccess(File t) {
								fileUtils.scanFileAsync(t.getPath());
								if(onDownLoadListener != null){
									onDownLoadListener.downLoadComplete();
								}
								if(pb != null){
									pb.setVisibility(View.GONE);
								}
								fileUtils.openFile(t);
							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								
								if(pb != null){
									pb.setVisibility(View.GONE);
								}
							}
						});
						
					}else{
						fileUtils.openFile(file);
					}
				}
			});

		}else{
			FinalHttp finalHttp = new FinalHttp();
			
			finalHttp.download(url, target, new AjaxCallBack<File>() {

				@Override
				public void onStart() {
					if(pb != null){
						pb.setVisibility(View.VISIBLE);
					}
				}

				@Override
				public void onLoading(long count, long current) {
					if(pb != null){
						pb.setProgress(getProcess(current, count));
					}
				}
				
				@Override
				public void onSuccess(File t) {
					fileUtils.scanFileAsync(t.getPath());
					if(onDownLoadListener != null){
						onDownLoadListener.downLoadComplete();
					}
					if(pb != null){
						pb.setVisibility(View.GONE);
					}
					fileUtils.openFile(t);
				}

				@Override
				public void onFailure(Throwable t, int errorNo,
						String strMsg) {
					
					if(pb != null){
						pb.setVisibility(View.GONE);
					}
				}
			});
		}
	}
	/**
	 * 下载并打开网络文件，并实时更新进度
	 * @param url
	 * @param target
	 * @param pd
	 */
	public void openNetRes(final String url,final String target,final ProgressDialog pd){
		final File file = new File(target);
		final FileUtil fileUtils = new FileUtil(context);
		if(file.exists()){
			downloadIgnore(new OnDownloadIgnoreListener() {
				
				@Override
				public void ignore(boolean isIgnore) {
					if(isIgnore){
						FinalHttp finalHttp = new FinalHttp();
						
						finalHttp.download(url, target, new AjaxCallBack<File>() {

							@Override
							public void onStart() {
								if(pd != null){
									pd.show();
								}
							}

							@Override
							public void onLoading(long count, long current) {
								if(pd != null){
									pd.setProgress(getProcess(current, count));
								}
							}
							
							@Override
							public void onSuccess(File t) {
								fileUtils.scanFileAsync(t.getPath());
								if(onDownLoadListener != null){
									onDownLoadListener.downLoadComplete();
								}
								if(pd != null){
									pd.dismiss();
								}
								fileUtils.openFile(t);
							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								
								if(pd != null){
									pd.dismiss();
								}
							}
						});
					}else{
						fileUtils.openFile(file);
					}
				}
			});
		}else{
			FinalHttp finalHttp = new FinalHttp();
			
			finalHttp.download(url, target, new AjaxCallBack<File>() {

				@Override
				public void onStart() {
					if(pd != null){
						pd.show();
					}
				}

				@Override
				public void onLoading(long count, long current) {
					if(pd != null){
						pd.setProgress(getProcess(current, count));
					}
				}
				
				@Override
				public void onSuccess(File t) {
					fileUtils.scanFileAsync(t.getPath());
					if(onDownLoadListener != null){
						onDownLoadListener.downLoadComplete();
					}
					if(pd != null){
						pd.dismiss();
					}
					fileUtils.openFile(t);
				}

				@Override
				public void onFailure(Throwable t, int errorNo,
						String strMsg) {
					
					if(pd != null){
						pd.dismiss();
					}
				}
			});
		}
	}
	/**
	 * 弹出是否覆盖文件的对话框
	 * @param onDownloadIgnoreListener
	 */
	private void downloadIgnore(final OnDownloadIgnoreListener onDownloadIgnoreListener){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("已有同名文件是否覆盖？");
		builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onDownloadIgnoreListener.ignore(false);
			}
		});
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onDownloadIgnoreListener.ignore(true);
			}
		});
		builder.create().show();
	}

	/**
	 * 下载并打网络资源(覆盖原有)
	 * @param url
	 * @param target
	 * @param pb
	 */
	@Deprecated
	public void openNetResIgnoreExists(String url,String target,final ProgressBar pb){
		final FileUtil fileUtils = new FileUtil(context);
		FinalHttp finalHttp = new FinalHttp();
		
		finalHttp.download(url, target, new AjaxCallBack<File>() {

			@Override
			public void onStart() {
				if(pb != null){
					pb.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onLoading(long count, long current) {
				if(pb != null){
					pb.setProgress(getProcess(current, count));
				}
			}
			
			@Override
			public void onSuccess(File t) {
				fileUtils.scanFileAsync(t.getPath());
				if(onDownLoadListener != null){
					onDownLoadListener.downLoadComplete();
				}
				if(pb != null){
					pb.setVisibility(View.GONE);
				}
				fileUtils.openFile(t);
			}

			@Override
			public void onFailure(Throwable t, int errorNo,
					String strMsg) {
				
				if(pb != null){
					pb.setVisibility(View.GONE);
				}
			}
			
		});
	}
	
	/**
	 * 上传一个文件
	 * @param url
	 * @param params
	 * @param pb
	 */
	public synchronized void uploadToServer(final String url,final AjaxParams params,final ProgressBar pb){
		FinalHttp finalHttp = new FinalHttp();
		finalHttp.configTimeout(20000);
		finalHttp.post(url, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if(pb != null){
					pb.setVisibility(View.GONE);
				}
			}

			@Override
			public void onLoading(long count, long current) {
				if(pb != null){
					pb.setProgress(getProcess(current, count));
				}
			}

			@Override
			public void onStart() {
				if(pb != null){
					pb.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onSuccess(Object t) {
				L.showLogInfo(L.TAG_NET_RESPONSE, t.toString());
				if(pb != null){
					pb.setVisibility(View.GONE);
				}
				if(uploadMultipleListener != null){
					uploadMultipleListener.uploadComplete(t);
					return;
				}
				if(onUploadListener != null){
					onUploadListener.uploadComplete(t);
				}
			}
			
		});
	}
	/**
	 * 上传多个文件
	 * @param url
	 * @param files
	 */
	public synchronized void uploadToServer(final String url,final List<String> files){
		if(files.size() == 0){
			return;
		}
		this.files = files;
		List<AjaxParams> paramsList = new ArrayList<AjaxParams>();
		for (String filePath : files) {
			AjaxParams params = new AjaxParams();
			try {
				params.put("file", new File(filePath));
			} catch (FileNotFoundException e) {
				L.showLogInfo(L.TAG_EXCEPTION, e.toString());
				continue;
			}
			paramsList.add(params);
		}
		
		uploadMultipleListener = new UploadMultipleListener() {
			
			@Override
			public void uploadComplete(Object result) {
				uploadIndex ++;
				if(uploadIndex == files.size() && onUploadListener != null){
					onUploadListener.uploadComplete(result);
				}else{
					uploadToServer(url);
				}
			}
		};
		uploadToServer(url);
	}
	
	private int getProcess(long current,long count){
        if (current != count && current != 0) {
            return (int) (current / (float) count * 100);
        } else {
            return 100;
        }
	}
	private synchronized void uploadToServer(String url){
		AjaxParams params = new AjaxParams();
		try {
			params.put("file", new File(files.get(uploadIndex)));
		} catch (FileNotFoundException e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		}
		uploadToServer(url, params, null);
	}
	/**
	 * 显示网络图片
	 * @param iv
	 * @param url
	 * @param loadingImgRes
	 * @param cachePath
	 */
	public void showImage(ImageView iv, String url, int loadingImgRes,String cachePath) {
		FinalBitmap bitmap = FinalBitmap.create(context);
		configImageParams(bitmap,cachePath,loadingImgRes);
		bitmap.display(iv, url);
	}

	/**
	 *
	 * @param iv
	 * @param url
	 * @param loadingImgRes
	 * @param cachePath
	 * @param maxLenght 图片最大宽/高
	 */
	public void showImage(ImageView iv, String url, int loadingImgRes,String cachePath,int maxLenght) {
		FinalBitmap bitmap = FinalBitmap.create(context);
		PhoneUtil phoneHelper = new PhoneUtil(context);
		float density = phoneHelper.getDensity();
		bitmap.configBitmapMaxWidth((int) (density*maxLenght));
		bitmap.configBitmapMaxHeight((int) (density*maxLenght));
		configImageParams(bitmap,cachePath,loadingImgRes);
		bitmap.display(iv, url);
	}
	private void configImageParams(FinalBitmap bitmap,String cachePath,int loadingImgRes){
		FileUtil.createDirIfNotExists(cachePath);
		Bitmap loadingBitmap = BitmapFactory.decodeResource(
				context.getResources(), loadingImgRes);
		bitmap.configDiskCachePath(cachePath);
		bitmap.configLoadingImage(loadingBitmap);
		bitmap.configLoadfailImage(loadingImgRes);
	}
	public interface OnDownLoadListener{
		void downLoadComplete();
	}
	public interface OnUploadListener{
		void uploadComplete(Object result);
	}
	
	private interface UploadMultipleListener{
		void uploadComplete(Object result);
	}
	
	private interface OnDownloadIgnoreListener{
		void ignore(boolean isIgnore);
	}
}
