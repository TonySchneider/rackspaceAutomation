package HTTPperformance;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class HTTPpost extends HTTP {
	private String locationResponse;
	private String wsid;
	private byte[] postData;
	private int postDataLength;
	private String email;
	private String pass;
	public HTTPpost(String url, String urlParameters,String email,String pass) throws UnsupportedEncodingException {
		super(url);
		postData = (urlParameters+"&user_name="+email+"&password="+URLEncoder.encode(pass, "UTF-8")).getBytes(StandardCharsets.UTF_8);
		postDataLength = postData.length;
		this.email = email;
		this.pass = pass;
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
	public String fileInputStream(String fileName) throws IOException{
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
	            }finally{
	            	in.close();
	        	    out.close();
	            }
	        }
	    }
//	    String nameOfFile = downloadedFile.getName();
	    String path = System.getProperty("java.io.tmpdir")+fileName;
	    File dir = new File(path);
	    if(!dir.exists())
	    	dir.mkdir();
	    String DonloadedFilePath = downloadedFile.getAbsolutePath();
	    try {
	         ZipFile zipFile = new ZipFile(DonloadedFilePath);
	         zipFile.setRunInThread(false);
	         zipFile.extractAll(path);
	    } catch (ZipException e) {
	        e.printStackTrace();
	    }finally{
	    	deleteFile(DonloadedFilePath);
	    }
	    return DonloadedFilePath;
	}
	public boolean deleteFile(String path){
		File file = new File(path); 
		if(file.delete())
            return true;
        return false;
	}
	public String getEmail(){return this.email;}
	public String getPass(){return this.pass;}
	public void setEmail(String email){this.email = email;}
	public void setPass(String pass){this.pass = pass;}
}
