package hzst.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
/**
 * 常量工具类。
 * 请在项目中建立一个常量类并继承它，也可避免常量重复定义的情况出现。
 * @author wt
 *
 */
public interface Constants {
	String CHARSET_UTF8 = "UTF-8";
	
	String EMPTY_STRING = "";
	
	String PATH_SD = Environment.getExternalStorageDirectory().getPath();
	
	/**
	 * {@link Activity#startActivityForResult(Intent intent, int requestCode)}
	 * 页面发起意图时传入requestCode.
	 * {@link Activity#onActivityResult(int requestCode, int resultCode, Intent data)}
	 * 当从上一个页面返回时，根据requestCode做接下来的操作。
	 */
	int REQUEST_CODE_IMG = 50;
	int REQUEST_CODE_LOCALFILE = 49;
	int REQUEST_CODE_CAMERA = 48;
	
	/**
	 * {@link Activity#onActivityResult(int requestCode, int resultCode, Intent data)}
	 * 当从上一个页面返回时,根据resultCode做接下来的操作。
	 * 自定义resultCode请从101开始。
	 */
	int RESULT_CODE_RESFRESH_LIST = 100;//刷新列表页
	int RESULT_SELECT_RESOURCE = 99;
	
	String SYMBOL_AND = "&";
	String SYMBOL_COMMA = ",";
	String SYMBOL_EQUAL = "=";
	String SYMBOL_QUESTION_MARK = "?";
	String SYMBOL_VERTICAL_BAR = "|";
	String SYMBOL_SEMICOLON = ";";
	
	String SUCCESS = "1";
	String FAIL = "0";
	String TRUE = "1";
	String FALSE = "0";

	/**
	 * Created by Administrator on 2017-2-22.
	 * 服务器端返回的结果码
	 */
	class ResultCode {
		public static final int CODE_OK = 200;//成功
		public static final int CODE_ERROR = 201;//错误
	}
}
