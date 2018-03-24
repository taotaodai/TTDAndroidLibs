package hzst.android.util;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * json数据解析类。
 * 1.利用google的gson把json数据转换为对应的实体类；
 * 2.利用安卓源生{@link JSONObject}对json数据进行一些操作。
 * @author wt
 *
 */
public class JsonDataParser {
	@SuppressWarnings({ "unchecked" })
	/**
	 * json转换为实体类实例集合
	 * @param json
	 * @param type
	 * @param beanList
	 * @return
	 */
	public static <TT, T> int getBeanList(String json,Class<T> type,List<TT> beanList){
		Gson gson = new Gson();
		T temp = gson.fromJson(json, type);
		
		if(temp == null){
			return 0;
		}

		for (TT tt : (TT[])temp) {
			beanList.add(tt);
		}
		return ((TT[])temp).length;
	}
	
	/**
	 * json转换为单个实体类实例
	 * @param json
	 * @param type
	 * @return
	 */
	public static <T> T getBean(String json,Class<T> type) throws Exception{
		Gson gson = new Gson();
		return gson.fromJson(json, type);
	}

	public static<T> String getJsonBean(Class<T> type,Object obj){
		Gson gson = new Gson();
		return gson.toJson(obj, type);
	}

	public static<T> String getJsonList(List<T> beanList){
		Gson gson = new Gson();
		return gson.toJson(beanList);
	}
	/**
	 * 获取json指定字段名对应的值，没有该字段则返回""
	 * @param obj
	 * @param name
	 * @return
	 */
	public static String parseJsonElement(JSONObject obj,String name){
		if(obj.has(name)){
			try {
				return obj.getString(name);
			} catch (JSONException e) {
				L.showLogInfo(L.TAG_EXCEPTION, e.toString());
				return "";
			}
		}
		return "";
	}

}
