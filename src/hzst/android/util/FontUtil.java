package hzst.android.util;

import android.content.Context;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.io.UnsupportedEncodingException;
/**
 * 字体处理类
 * 1.改变字体颜色大小，主要用于分段变化控件的Text
 * 2.验证字符串字节长度是否超出限制
 * @author wt
 *
 */
public class FontUtil {
	private Context context;
	
	public static final int FONT_SIZE_7 = 7;
	public static final int FONT_SIZE_8 = 8;
	public static final int FONT_SIZE_10 = 10;
	public static final int FONT_SIZE_12 = 12;
	public static final int FONT_SIZE_14 = 14;
	public static final int FONT_SIZE_16 = 16;
	public static final int FONT_SIZE_18 = 18;
	public static final int FONT_SIZE_20 = 20;
	
	public FontUtil(Context context) {
		this.context = context;
	}

	public SpannableStringBuilder getSpanString(String content,int color,int size){
		CharacterStyle cs = new AbsoluteSizeSpan(UnitsConversionUtil.sp2px(context, size));//第二参数为true，字体单位为dp
		
	    SpannableStringBuilder builder = new SpannableStringBuilder(content); 

	    builder.setSpan(cs, 0,content.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    builder.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    
	    return builder;
	}
	
	public SpannableStringBuilder getSpanString(String content,int size){
		CharacterStyle cs = new AbsoluteSizeSpan(UnitsConversionUtil.sp2px(context, size));

	    SpannableStringBuilder builder = new SpannableStringBuilder(content); 

	    builder.setSpan(cs, 0,content.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    
	    return builder;
	}
	
	public SpannableStringBuilder getSpanString(int color,String content){

	    SpannableStringBuilder builder = new SpannableStringBuilder(content); 

	    builder.setSpan(new ForegroundColorSpan(color), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    
	    return builder;
	}

	/**
	 *
	 * @param text
	 * @param size  字体大小(px)
	 * @return
	 */
	public static float getTextWidth(String text,float size){
		Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setTextSize(size);
		return mTextPaint.measureText(text);
	}
	/**
	 * 是否超出字数限制
	 * @param str
	 * @param limit 最大字节
	 * @return
	 */
	public static boolean isBeyondTextLimit(String str,int limit){
		try {
			if(str.getBytes("UTF-8").length > limit){
//				showMemberToast(Toast.LENGTH_SHORT, hint);
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
			return true;
		}
		return false;
	}
	
    private static final boolean isChinese(char c) {  
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
            return true;  
        }  
        return false;  
    }
    
    public static final boolean hasChinese(String str) {  
        char[] ch = str.toCharArray();  
        for (int i = 0; i < ch.length; i++) {  
            char c = ch[i];  
            if (isChinese(c)) {  
                return true;  
            }  
        }  
        return false;  
    }  
}
