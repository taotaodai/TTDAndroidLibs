package hzst.android.util;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.ServiceConnectionSE;


/**
 * 调用服务器WebService
 * @author wt 2015-5-20
 *
 */
public class WebServiceUtil {
    private String url = ""; //测试服务器

    private static final String Namespace = "http://tempuri.org/";// 命名空间
    public static final String EX_TIMEOUT = "timeOut";

    public WebServiceUtil(String url) {
        this.url = url;
    }

    /**
     * 调用WebService
     *
     * @return WebService的返回值
     */
    public String CallWebService(String MethodName, Map<String, String> params) {
        // 1、指定webservice的命名空间和调用的方法名
        SoapObject request = new SoapObject(Namespace, MethodName);
        // 2、设置调用方法的参数值，如果没有参数，可以省略，
        if (params != null) {
            Iterator<Entry<String, String>> iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, String> entry = iter.next();
                request.addProperty(entry.getKey(),
                        entry.getValue());
            }
        }
        // 3、生成调用Webservice方法的SOAP请求信息。该信息由SoapSerializationEnvelope对象描述
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER12);
        envelope.bodyOut = request;
        // c#写的应用程序必须加上这句
        envelope.dotNet = true;
        MyHttpTransport ht = new MyHttpTransport(url, 60000);
        // 使用call方法调用WebService方法
        try {
            ht.call(null, envelope);
        } catch (Exception e) {
        	L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        	if(e instanceof SocketTimeoutException){
        		return EX_TIMEOUT;
        	}
        }
        try {
            final SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            if (result != null) {
                return result.toString();
            }

        } catch (Exception e) {
        	L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        return null;
    }
    
    public String CallWebService2(String MethodName, Map<String, String> params) {
        // 1、指定webservice的命名空间和调用的方法名
        SoapObject request = new SoapObject(Namespace, MethodName);
        // 2、设置调用方法的参数值，如果没有参数，可以省略，
        if (params != null) {
            Iterator<Entry<String, String>> iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, String> entry = iter.next();
                request.addProperty(entry.getKey(),
                        entry.getValue());
            }
        }
        // 3、生成调用Webservice方法的SOAP请求信息。该信息由SoapSerializationEnvelope对象描述
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER12);
        envelope.bodyOut = request;
        // c#写的应用程序必须加上这句
        envelope.dotNet = true;
        MyHttpTransport ht = new MyHttpTransport(url, 60000);
        // 使用call方法调用WebService方法
        try {
            ht.call(null, envelope);
        } catch (Exception e) {
        	L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        try {
            final SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            if (result != null) {
//                Log.d("----收到的回复----", result.toString());
                return result.toString();
            }

        } catch (Exception e) {
        	L.showLogInfo(L.TAG_EXCEPTION, e.toString());
            if(e instanceof NullPointerException){
            	return "notconn";
            }
        }
        return null;
    }


    public String CallWebServiceObj(String MethodName, Map<String, String> params) {
        // 1、指定webservice的命名空间和调用的方法名
        SoapObject request = new SoapObject(Namespace, MethodName);
        // 2、设置调用方法的参数值，如果没有参数，可以省略，
        if (params != null) {
            Iterator<Entry<String, String>> iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, String> entry = iter.next();
                request.addProperty((String) entry.getKey(),
                        (String) entry.getValue());
            }
        }
        // 3、生成调用Webservice方法的SOAP请求信息。该信息由SoapSerializationEnvelope对象描述
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER12);
        envelope.bodyOut = request;
        // c#写的应用程序必须加上这句
        envelope.dotNet = true;
        MyHttpTransport ht = new MyHttpTransport(url, 60000);
        // 使用call方法调用WebService方法
        try {
            ht.call(null, envelope);
        } catch (Exception e) {
        	L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        try {
            final Object object = envelope.getResponse();

            if (object != null) {
                return object.toString();
            }

        } catch (SoapFault e) {
        	L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        return null;
    }

    class MyHttpTransport extends HttpTransportSE {
        private int timeout = 10000; //默认超时时间为20s

        public MyHttpTransport(String url) {
            super(url);
        }

        public MyHttpTransport(String url, int timeout) {
            super(url);
            this.timeout = timeout;
        }

        @Override
        public ServiceConnection getServiceConnection() throws IOException {
            ServiceConnectionSE serviceConnection = new ServiceConnectionSE(this.url, timeout);
            return serviceConnection;
        }
    }
}
