package hzst.android.web;

import android.content.Context;

import hzst.android.util.PhoneUtil;

/**
 * Created by Administrator on 2016/6/8.
 */
public class Connection extends BaseJsInterface{
    public Connection(Context context) {
        super(context);
        name = getClassName(this);
    }

    public void getNetworkState(){
        PhoneUtil phoneUtil = new PhoneUtil(context);
        int state = phoneUtil.getNetWorkType();
        getWebExample().executeJsFunction(getWebExample().baseJsInterface.operation.getOnSuccess(),String.valueOf(state));
    }
}
