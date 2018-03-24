package hzst.android.util.http;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.ConnectException;

/** 
 * 类说明 
 * @author shiqing
 * @version V1.0 
 * @createTime: 2014-11-26 下午5:16:03 
 */
class AsyncHttpRequest implements Runnable{
	
	private final static String TAG=AsyncHttpRequest.class.getName();

	private final AbstractHttpClient client;
	private final HttpContext context;
	private final HttpUriRequest  request;
	private final AsyncHttpResponseHandler responseHandler;
	private int executionCount;
	
	public AsyncHttpRequest(AbstractHttpClient client, HttpContext context, HttpUriRequest request, AsyncHttpResponseHandler responseHandler){
		this.client=client;
		this.context=context;
		this.request=request;
		this.responseHandler=responseHandler;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			if(responseHandler!=null){
				
				Log.d(TAG, "sendStartMessage");
				responseHandler.sendStartMessage();
			}
			makeRequestWithRetries();
			
			if(responseHandler!=null){
				Log.d(TAG, "sendFinishMessage");
				responseHandler.sendFinishMessage();
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			 if(responseHandler != null) {
                responseHandler.sendFinishMessage();
                responseHandler.sendFailureMessage(e, null);
            }
		}
	}
	
	private void makeRequest() throws IOException{
		if(!Thread.currentThread().isInterrupted()){
			HttpResponse response=client.execute(request, context);
			Log.d(TAG, "Return Response");
			if(!Thread.currentThread().isInterrupted()){
				if(responseHandler!=null){
					responseHandler.sendResponseMessage(response);
				}
			}else{
				//TODO: should raise InterruptedException? this block is reached whenever the request is cancelled before its response is received
			}
		}
	}
	
	private void makeRequestWithRetries() throws ConnectException{
		// This is an additional layer of retry logic lifted from droid-fu
        // See: https://github.com/kaeppler/droid-fu/blob/master/src/main/java/com/github/droidfu/http/BetterHttpRequestBase.java
		
		boolean retry=true;
		IOException cause=null;
		HttpRequestRetryHandler retryHandler=client.getHttpRequestRetryHandler();
		while (retry) {
			try {
				makeRequest();
				return;
			} catch (IOException e) {
				// TODO: handle exception
				cause=e;
				retry=retryHandler.retryRequest(cause, ++executionCount, context);
			} catch (NullPointerException e) {
				// there's a bug in HttpClient 4.0.x that on some occasions causes
                // DefaultRequestExecutor to throw an NPE, see
                // http://code.google.com/p/android/issues/detail?id=5255
				cause = new IOException("NPE in HttpClient" + e.getMessage());
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
			}
			
		}
		
		// no retries left, crap out with exception
		ConnectException ex= new ConnectException();
		ex.initCause(cause);
		throw ex;
	}

}
