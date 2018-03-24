//package hzst.android.web;
//
//import android.content.Context;
//import android.content.Intent;
//
//import hzst.android.util.SysIntentUtil;
//import hzst.android.zxing.CaptureActivity;
//import hzst.android.zxing.CaptureActivityHandler;
//
//public class Camera extends BaseJsInterface{
//	public Camera(Context context) {
//		super(context);
//		name = getClassName(this);
//	}
//
//    public void takePhoto() {
//    	SysIntentUtil intentUtil = new SysIntentUtil(context);
//    	intentUtil.takePhoto();
//    }
//
//	public void openImgManager(){
//		SysIntentUtil intentUtil = new SysIntentUtil(context);
//		intentUtil.openImgManager();
//	}
//
//	/**
//	 * 扫码
//	 */
//	public void scanCode(){
//		Intent intent = new Intent(context, CaptureActivity.class);
//		context.startActivity(intent);
//		CaptureActivityHandler.setOnScanCodeListener(new CaptureActivityHandler.OnScanCodeListener() {
//			@Override
//			public void decodeSucceeded(String code) {
//				getWebExample().executeJsFunction(getWebExample().baseJsInterface.operation.getOnSuccess(),code);
//			}
//		});
//	}
//
//}
