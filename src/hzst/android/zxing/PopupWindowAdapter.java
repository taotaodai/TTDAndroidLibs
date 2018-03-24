package hzst.android.zxing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import hzst.android.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/7.
 */
public class PopupWindowAdapter extends BaseAdapter {
    private Context context;
    private String[] txts;
    private int[] imgs;
    private LayoutInflater inflater;

    public PopupWindowAdapter(Context context,String[] txts,int[] imgs) {
        this.context = context;
        this.txts = txts;
        this.imgs = imgs;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return txts.length;
    }

    @Override
    public Object getItem(int position) {
        return txts[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;
        if (convertView == null){
            viewHold = new ViewHold();
            convertView = inflater.inflate(R.layout.item_list_popup,parent,false);
            viewHold.iv_item_img = (ImageView) convertView.findViewById(R.id.iv_item_img);
            viewHold.tv_item_text = (TextView) convertView.findViewById(R.id.tv_item_text);
            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHold) convertView.getTag();
        }
       // viewHold.iv_item_img.setBackgroundResource(imgs[position]);
        viewHold.tv_item_text.setText(txts[position]);
        return convertView;
    }
    class ViewHold{
        TextView tv_item_text;
        ImageView iv_item_img;
    }

}
