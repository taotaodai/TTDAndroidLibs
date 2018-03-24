package hzst.android.web;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import hzst.android.util.L;

public class BaseJsInterface {
	protected Operation operation;
	protected static String name;
	protected Context context;
	protected Handler handler = new Handler(){

	};
	
	public BaseJsInterface(Context context) {
		this.context = context;
	}

//	public static String getName() {
//		return name;
//	}
	
	protected String getClassName(Object obj){
		String clName = obj.getClass().getName();
		return clName.substring(clName.lastIndexOf(".")+1);
	}

	/**
	 * js调用安卓的入口方法
	 * @param json
	 */
	@JavascriptInterface
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void execute(final String json){
		handler.post(new Runnable() {
			@Override
			public void run() {
				//{"plugin":"Camera","method":"takePhoto","parameters":[]}
				try {
					Gson gson = new Gson();
					operation = gson.fromJson(json, new TypeToken<Operation>(){}.getType());
					String className = getClass().getPackage().getName()+"."+operation.getPlugin();
					//根据类名获取Class对象
					Class<?> c = Class.forName(className);
					//参数类型数组
					@SuppressWarnings("rawtypes")
					Class[] parameterTypes = {Context.class};
					//根据参数类型获取相应的构造函数
					Constructor<?> constructor = c.getConstructor(parameterTypes);
					//根据获取的构造函数和参数，创建实例
					Object object = constructor.newInstance(new Object[]{context});
//			Object object = Class.forName(className).newInstance();

//			Method[] methods = object.getClass().getMethods();
//			for (int i = 0;i<= methods.length;i ++) {
//				System.out.println( methods[i].getName()+ "~~~");
//			}
					@SuppressWarnings("rawtypes")
					Class[] types = null;
					Object[] values= null;
					List<Operation.Parameter> parameters = operation.getParameters();
					if(parameters != null){
						values = new String[parameters.size()];
						types = new Class[parameters.size()];
						for (int i = 0;i < parameters.size();i ++) {
							types[i] = String.class;
							values[i] = parameters.get(i).getValue();
						}
					}
					Method method = object.getClass().getMethod(operation.getMethod(), types);

					method.invoke(object, values);
				} catch (Exception e) {
					L.showLogInfo(L.TAG_EXCEPTION,e.toString());
				}
			}
		});
	}

	protected WebActivity getWebExample(){
		if(context instanceof WebActivity){
			return (WebActivity) context;
		}
		return null;
	}

}
