package hzst.android.util;

import hzst.android.Constants;
import hzst.android.R;
import hzst.android.adapter.CommonAdapter;
import hzst.android.entity.BaseUser;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 常用的系统调用
 * @author wt
 *
 */
public class SysIntentUtil {
	private Context context;
	private Fragment fragment;
	
	public SysIntentUtil(Context context) {
		this.context = context;
	}
	@Deprecated
    public SysIntentUtil(Fragment fragment) {
    	this.fragment = fragment;
	}

	/**
     * 打开系统图片管理
     */
    public void openImgManager(){
		Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intentGallery, Constants.REQUEST_CODE_IMG);
		
    }
    /**
     * 打开文件管理器
     */
    public void openFileManager(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, Constants.REQUEST_CODE_LOCALFILE);
    }
    /**
     * 拍照
     */
    public void takePhoto(){
    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        Uri uri = Uri.fromFile(file);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    	startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA);
    }

    /**
     * 呼叫
     * @param phoneNumber
     */
    public void callPhone(String phoneNumber){
    	Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	context.startActivity(intent);
    }
    /**
     * 拨号
     * @param phoneNumber
     */
    public void dial(String phoneNumber){
    	Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	context.startActivity(intent);
    }
    /**
     * 发短信(多个号码用";"隔开)
     * @param phoneNumber
     */
    public void sendMessage(String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.putExtra("address", phoneNumber);
		intent.setData(Uri.parse("smsto:" + phoneNumber));  
//		intent.setType("vnd.android-dir/mms-sms");
		context.startActivity(intent);
    }

	/**
	 * 打开通讯窗口，可以选择呼叫或者发送信息
	 * @param user
	 */
    public void showCommunicationWindow(BaseUser user){
    	final PhoneUtil phoneUtil = new PhoneUtil(context);
    	final BackgroundUtil backgroundUtil = new BackgroundUtil(context);
    	
    	View view = LayoutInflater.from(context).inflate(R.layout.view_communication, null);
    	TextView tvUser = (TextView) view.findViewById(R.id.tv_user);
		if(!TextUtils.isEmpty(user.getUserName())){
			tvUser.setText(user.getUserName());
		}else{
			tvUser.setText(user.getMemberName());
		}

    	tvUser.setGravity(Gravity.CENTER);
    	if(user.getPosition() != null){
    		tvUser.append("\n"+user.getPosition());
    	}
    	ListView lvUserinfo = (ListView) view.findViewById(R.id.lv_userinfo);
    	final List<String> infos = new ArrayList<String>();
    	
    	String[] phoneNumbers = user.getPhoneNumber().split(",");
    	for (String phone : phoneNumbers) {
    		infos.add(phone);
		}
    	
    	if(user.getCornet() != null && !"".equals(user.getCornet())){
    		infos.add(user.getCornet());
    	}
    	CommonAdapter<String> adapter = new CommonAdapter<String>(context, infos, R.layout.adapter_communication) {

			@Override
			public void convert(ViewHolder helper, final String item, int position) {
				helper.setText(R.id.tv_number, item);
				TextView tvMsg = helper.getView(R.id.tv_msg);
				backgroundUtil.setPressColor(tvMsg, R.color.bg_gray6, phoneUtil.getDensity()*5);
				tvMsg.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sendMessage(item);
					}
				});
			}
		};
		lvUserinfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				callPhone(infos.get(position));
			}
		});
		lvUserinfo.setAdapter(adapter);

		Dialog dialog = new Dialog(context,R.style.dialog_transparency);
		dialog.setContentView(view);
		dialog.show();
//    	final PopupWindow pop = new PopupWindow(view,(int) (phoneUtil.getWidth() - 30*phoneUtil.getDensity()),LayoutParams.WRAP_CONTENT);
//    	
//    	pop.setAnimationStyle(R.style.PopupAnim_Fade);
//		pop.setBackgroundDrawable(new ColorDrawable(0));
//		pop.setOutsideTouchable(true);
//		pop.setFocusable(true);
//
//		pop.setTouchInterceptor(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//					pop.dismiss();
//					return true;
//				}
//				return false;
//			}
//		});
//    	pop.setOnDismissListener(new MyPopDismissListener(context));
//    	backgroundUtil.setWindowAlpha(0.7f);
//    	pop.showAtLocation(((Activity)context).getCurrentFocus(), Gravity.CENTER, 0, 0);
    }
    
    /**
     * 安装应用
     * @param apkPath
     */
    public void installApk(String apkPath){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    /**
     * 卸载应用
     * @param packageName
     */
    public void uninstallApk(String packageName){
    	Uri uri =Uri.fromParts("package", packageName, null);
    	Intent it = new Intent(Intent.ACTION_DELETE, uri);
    	context.startActivity(it);
    }
    /**
     * 打开其他应用
     * @param packageName "com.yellowbook.android2"
     * @param className   "com.yellowbook.android2.AndroidSearch"
     */
    public void openOtherApp(String packageName,String className){
    	Intent i = new Intent();
    	ComponentName cn = new ComponentName(packageName,className);
    	i.setComponent(cn);
    	i.setAction("android.intent.action.MAIN");
    	startActivityForResult(i, Activity.RESULT_OK);
    }
    
    private void startActivityForResult(Intent intent,int requestCode){
		if(context != null){
			((Activity)context).startActivityForResult(intent, requestCode);
		}else{
			fragment.startActivityForResult(intent, requestCode);
		}
    }
}
