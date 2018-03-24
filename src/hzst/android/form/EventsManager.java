package hzst.android.form;

import android.content.Intent;

import java.util.List;
import java.util.Map;

import hzst.android.form.event.OnAccessoryUploadListener;
import hzst.android.form.info.BaseViewInfo;
import hzst.android.form.view.FMAccessoryView;

/**
 * Created by wt on 2017/4/13.
 */
public class EventsManager implements OnAccessoryUploadListener {
    private OnAccessoryOperateListener onAccessoryOperateListener;
    private OnActivityResultListener onActivityResultListener;
    private OnSubmitListener onSubmitListener;

    private List<BaseViewInfo> viewList;
    public EventsManager(List<BaseViewInfo> viewList){
        this.viewList = viewList;
    }
    public OnAccessoryOperateListener getOnAccessoryOperateListener() {
        return onAccessoryOperateListener;
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }

    public void setOnAccessoryOperateListener(OnAccessoryOperateListener onAccessoryOperateListener) {
        this.onAccessoryOperateListener = onAccessoryOperateListener;
    }

    public OnActivityResultListener getOnActivityResultListener() {
        return onActivityResultListener;
    }

    public void setOnActivityResultListener(OnActivityResultListener onActivityResultListener) {
        this.onActivityResultListener = onActivityResultListener;
    }

    public void uploaduploadAccessories(String url){
        if(onAccessoryOperateListener != null){
            onAccessoryOperateListener.uploadAccessories(url);
        }else {
            if(onSubmitListener != null){
                onSubmitListener.submit();
            }
        }
    }

    /**
     * 附件上传成功
     */
    @Override
    public void uploadComplete() {
        if(onSubmitListener != null){
            onSubmitListener.submit();
        }
    }

    /**
     * 附件操作监听器
     */
    public interface OnAccessoryOperateListener {
        void uploadAccessories(String url);
    }

    /**
     * 页面回调监听
     */
    public interface OnActivityResultListener{
        void activityResult(int requestCode, int resultCode, Intent data);
    }

    /**
     * 通知表单页面开始提交数据
     */
    public interface OnSubmitListener{
        void submit();
    }
}
