package hzst.android.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import org.json.JSONObject;

import hzst.android.Constants;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取服务器数据工具类
 * @author wt 2015-3-11
 *
 */
public class NetworkDataUtil {
	/**
	 * get请求
	 * @param
	 * @return
	 */
	public static String getJsonData(String url){
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len;
		String str;

		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("GET");
			InputStream inStream;
			inStream = conn.getInputStream();
			while ((len = inStream.read(data)) != -1) {
				outStream.write(data, 0, len);
			}
			inStream.close();

			str = new String(outStream.toByteArray(), "UTF-8");
		} catch (Exception e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
			return "";
		}
		Log.i("zby", str);
		return str;
	}
	/**
	 * post请求
	 */
	@SuppressWarnings("rawtypes")
	public static String doPost(Map<String, Object> params,String url) {
		return doPost(parseParams(params),url,new HashMap<String, String>());
	}

	public static String doPost(Map<String, Object> params,String url,Map<String,String> headers) {
		return doPost(parseParams(params),url,headers);
	}

	private static String parseParams(Map<String, Object> params){
		StringBuffer sb = new StringBuffer();
		// 组织请求参数
		Iterator<?> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry element = (Map.Entry) it.next();
			Object key = element.getKey();
			Object value = element.getValue();
			if(value instanceof List){
				for (Object obj:
						((List) value)){
					sb.append(key);
					sb.append("=");
					sb.append(obj);
					sb.append("&");
				}
			}else {
				sb.append(key);
				if(!TextUtils.isEmpty(key.toString())){
					sb.append("=");
				}
				sb.append(element.getValue());
				sb.append("&");
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}


	private static synchronized String doPost(String request,String url,Map<String,String> headers){
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		// BufferedReader bufferedReader = null;
		StringBuffer responseResult = new StringBuffer();

		HttpURLConnection httpURLConnection = null;

		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			httpURLConnection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");

			httpURLConnection.setRequestProperty("Content-Length", String.valueOf(request.getBytes().length));

			httpURLConnection.addRequestProperty("Cookie",MobCookieManager.getCookie());

			Iterator<?> it = headers.entrySet().iterator();
			while (it.hasNext()){
				Map.Entry element = (Map.Entry) it.next();
				httpURLConnection.setRequestProperty(element.getKey().toString(),element.getValue().toString());
			}

			// 发送POST请求必须设置如下两行
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);

			// 获取URLConnection对象对应的输出流
			printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			printWriter.write(request);
			// flush输出流的缓冲
			printWriter.flush();
			// 根据ResponseCode判断连接是否成功
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode != 200) {

//				log.error(" Error===" + responseCode);
			} else {
				MobCookieManager.setCookies(httpURLConnection.getHeaderFields());

//				log.info("Post Success!");
			}
			// 定义BufferedReader输入流来读取URL的ResponseData
			bufferedReader = new BufferedReader(new InputStreamReader(
					httpURLConnection.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				responseResult.append(line);
			}
		} catch (Exception e) {
			L.showLogInfo(L.TAG_EXCEPTION, e.toString());
		} finally {
			try {
				httpURLConnection.disconnect();
				if (printWriter != null) {
					printWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception ex) {
				L.showLogInfo(L.TAG_EXCEPTION, ex.toString());
			}
		}

		return responseResult.toString();
	}

	/**
	 * 根据传入的实体数据，拼出完整的接口。
	 * 利用反射获取参数名与实体类中匹配的get方法返回的值，这样就省去了手动一个一个拼接参数。
	 * @param urlFrame  带参数的接口
	 * @param dataBeans 需要与参数关联的实体对象数据
	 * @param isEncrypt 是否为参数名加密
	 * @return
	 */
	public static String spellUrl(String urlFrame,boolean isEncrypt,Object... dataBeans){
		Pattern pattern = Pattern.compile("[&,?]{1}[\\w]{1,}={1}");
		Matcher m = pattern.matcher(urlFrame);
		String url = null;
		/*
		遍历提取出所有参数
		 */
		while (m.find()){
			String prmName = m.group();
			String fieldName = prmName.substring(1, prmName.length() - 1);
			for (Object bean : dataBeans) {
				try {
					/*
					根据参数名去调用实体对象的get方法，并为参数插入值
					 */
					Object temp = ReflectUtil.invokeGet(bean, fieldName);
					String prmValue;
					if(temp == null){
						prmValue = "";
					}else{
						prmValue = String.valueOf(temp);
					}

					if(url == null){
                        url = urlFrame.replaceFirst("\\"+prmName,prmName+URLEncoder.encode(prmValue,Constants.CHARSET_UTF8));
                    }else{
                        url = url.replaceFirst("\\"+prmName,prmName+URLEncoder.encode(prmValue,Constants.CHARSET_UTF8));
                    }
					break;
				} catch (Exception e) {
					continue;
				}
			}
		}
		if(isEncrypt){
			try {
				//利用URL类取出参数数据，并加密，然后再拼接
				URL temp = new URL(url);
				String urlEn = "http://" + temp.getAuthority() + temp.getPath() + Constants.SYMBOL_QUESTION_MARK + EncryptUtil.encrypt(temp.getQuery());
				return urlEn;
			} catch (Exception e) {
				L.showLogInfo(L.TAG_EXCEPTION, e.toString());
			}
		}

		return url;
	}

}
