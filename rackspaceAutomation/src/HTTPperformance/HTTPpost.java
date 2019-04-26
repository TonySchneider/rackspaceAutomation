package HTTPperformance;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HTTPpost extends HTTP {
	private String locationResponse;
	private String wsid;
	private byte[] postData;
	private int postDataLength;
	public HTTPpost(String url, String urlParameters) {
		super(url);
		postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		postDataLength = postData.length;
	}
	public HTTPpost(String url, List<String> cookies, String urlParameters) {
		super(url,cookies);
		postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		postDataLength = postData.length;
	}
	public String getLocationResponse(){return this.locationResponse;}
	public String getWSID(){return this.wsid;}
	public String createConnection() throws IOException{
		super.createConnection();
		this.conn.setDoOutput(true);
		this.conn.setInstanceFollowRedirects(false);
		this.conn.setRequestMethod("POST");
		this.conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
		this.conn.setRequestProperty("charset", "utf-8");
		this.conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		this.conn.setUseCaches(false);
		try(DataOutputStream wr = new DataOutputStream(this.conn.getOutputStream())) {
		   wr.write(postData);
		   wr.flush();
		}
		return getRespondeStatus();
	}
	public void firstSettings(){
		setCookies(this.conn.getHeaderFields().get("Set-Cookie"));
		this.locationResponse = this.conn.getHeaderField("Location");
		this.wsid = (String) getLocationResponse().subSequence(15, getLocationResponse().length());
	}
	public void fileInputStream(String fileName) throws IOException{
		InputStream in = getConn().getInputStream();
		File downloadedFile = File.createTempFile(fileName+"_", ".zip");
	    FileOutputStream out = new FileOutputStream(downloadedFile);        
	    byte[] buffer = new byte[1024];
	    int len = in.read(buffer);
	    while (len != -1) {
	        out.write(buffer, 0, len);
	        len = in.read(buffer);
	        if (Thread.interrupted()) {
	            try {
	                throw new InterruptedException();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    in.close();
	    out.close();
	}
}
