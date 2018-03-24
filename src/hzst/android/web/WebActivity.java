package hzst.android.web;

import hzst.android.Constants;
import hzst.android.util.FileUtil;
import hzst.android.util.MediaUtil;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/6/7.
 */
public class WebActivity extends Activity{
    public WebView webView;
    protected BaseJsInterface baseJsInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        baseJsInterface = new BaseJsInterface(this);
        webView.addJavascriptInterface(baseJsInterface, "jsInterface");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK:
                switch (requestCode){
                    case Constants.REQUEST_CODE_CAMERA:
                        Bitmap bitmap = MediaUtil.getPhotoData(this, data);
                        String imgData = FileUtil.imgEncodingToBase64String(bitmap);

                        executeJsFunction(baseJsInterface.operation.getOnSuccess(), imgData);
                        break;
                    case Constants.REQUEST_CODE_IMG:
//                        bitmap = BitmapFactory.decodeFile(MediaUtil.getPath(this,data.getData()));
//                        imgData = SharedPreferencesUtil.imgEncodingToBase64String(bitmap);

                        executeJsFunction(baseJsInterface.operation.getOnSuccess(), MediaUtil.getPath(this,data.getData()));
                        break;
                }

        }
    }

    protected void executeJsFunction(String method,String... parameters){
        StringBuffer sb = new StringBuffer("");
        for (String s : parameters) {
            sb.append("'");
            sb.append(s);
            sb.append("'");
            sb.append(",");
        }
        if(sb.length() > 0){
            sb.setLength(sb.length() - 1);
        }

//        sb.append("\"plugin\":\"Camera\",");
//        sb.append("\"imgdata\":\"");
//        sb.append(parameters[0]);
//        sb.append("\"");
        webView.loadUrl("javascript:" + method + "(" + sb + ")");

    }

    protected void executeJsFunction(String method){
        webView.loadUrl("javascript:" + method + "()");
    }
}
