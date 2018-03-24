package hzst.android.web;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/6/12.
 */
public class Task extends  BaseJsInterface{
    public Task(Context context) {
        super(context);
        name = getClassName(this);
    }

    public void openMoreActivity(String url,String title){
        Intent intent = new Intent();
        intent.setClass(context,MoreActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }
}
