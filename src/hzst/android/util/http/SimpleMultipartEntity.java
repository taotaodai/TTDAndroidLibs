package hzst.android.util.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/** 
 * 类说明 
 * @author shiqing
 * @version V1.0 
 * @createTime: 2014-11-26 上午10:40:20 
 */
public class SimpleMultipartEntity implements HttpEntity{
	
	private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	
	private String boundary=null;
	
	ByteArrayOutputStream out=new ByteArrayOutputStream();
	boolean isSetLast= false;
	boolean isSetFirst = false;
	
	public SimpleMultipartEntity(){
		final StringBuffer buf=new StringBuffer();
		final Random rand=new Random();
		for(int i=0; i<30; i++){
			buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
		}
		this.boundary=buf.toString();
	}
	
	public void writeFirstBoundaryIfNeeds(){
		if(!isSetFirst){
			try {
				out.write(("--"+boundary+"\r\n").getBytes());
			} catch (final IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		isSetFirst=true;
	}
	
	public void writeLastBoundaryIfNeeds(){
		if(isSetLast)
			return;
		
		try {
			out.write(("--"+boundary+"\r\n").getBytes());
		} catch (final IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		isSetLast=true;
	}
	
	public void addPart(final String key, final String value){
		writeFirstBoundaryIfNeeds();
		try {
			out.write(("Content-Disposition: form-data; name=\"" +key+"\"\r\n\r\n").getBytes());
            out.write(value.getBytes());
            out.write(("\r\n--" + boundary + "\r\n").getBytes());
		} catch (final IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void addPart(final String key, final String fileName, final InputStream fin, final boolean isLast){
		addPart(key, fileName, fin, "application/octet-stream", isLast);
	}
	
	public void addPart(final String key, final String fileName, final InputStream fin, String type, final boolean isLast){
		writeFirstBoundaryIfNeeds();
		try {
            type = "Content-Type: "+type+"\r\n";
            out.write(("Content-Disposition: form-data; name=\""+ key+"\"; filename=\"" + fileName + "\"\r\n").getBytes());
            out.write(type.getBytes());
            out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

            final byte[] tmp = new byte[4096];
            int l = 0;
            while ((l = fin.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }
            if(!isLast)
            	out.write(("\r\n--" + boundary + "\r\n").getBytes());
            out.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	public void addPart(final String key, final File value, final boolean isLast){
		try {
			addPart(key, value.getName(), new FileInputStream(value), isLast);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void consumeContent() throws IOException, UnsupportedOperationException {
		// TODO Auto-generated method stub
		if(isStreaming()){
			throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
		}
	}

	@Override
	public InputStream getContent() throws IOException, IllegalStateException {
		// TODO Auto-generated method stub
		return new ByteArrayInputStream(out.toByteArray());
	}

	@Override
	public Header getContentEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getContentLength() {
		// TODO Auto-generated method stub
		writeLastBoundaryIfNeeds();
		return out.toByteArray().length;
	}

	@Override
	public Header getContentType() {
		// TODO Auto-generated method stub
		return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
	}

	@Override
	public boolean isChunked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRepeatable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStreaming() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeTo(final OutputStream outStream) throws IOException {
		// TODO Auto-generated method stub
		outStream.write(out.toByteArray());
	}

}
