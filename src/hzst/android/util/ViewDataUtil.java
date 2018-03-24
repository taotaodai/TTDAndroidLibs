package hzst.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

public class ViewDataUtil {
	private Context context;
	
    public ViewDataUtil(Context context) {
    	this.context = context;
	}

//	public void insertIcon(int id,TextView tv) {
//        // SpannableString连续的字符串，长度不可变，同时可以附加一些object;可变的话使用SpannableStringBuilder，参考sdk文档
//        SpannableString ss = new SpannableString(tv.getText().toString()+ "[smile]");
//        // 得到要显示图片的资源
//        Drawable d = context.getResources().getDrawable(id);
//        // 设置高度
//        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//        // 跨度底部应与周围文本的基线对齐
//        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
//        // 附加图片
//        ss.setSpan(span, tv.getText().length(),
//        		tv.getText().length() + "[smile]".length(),
//                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//
////        setText(ss);
//        tv.append(ss);
//    }
    
    public void insertIcon(Bitmap bitmap,TextView tv,String content) {
        // SpannableString连续的字符串，长度不可变，同时可以附加一些object;可变的话使用SpannableStringBuilder，参考sdk文档
        SpannableString ss = new SpannableString(content);
        // 得到要显示图片的资源
//        Drawable d = getResources().getDrawable(id);
        // 设置高度
//        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        // 跨度底部应与周围文本的基线对齐
        ImageSpan span = new ImageSpan(context, bitmap);
        // 附加图片
        ss.setSpan(span, 0,content.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.append(ss);
//        setText(ss);
    }
}
