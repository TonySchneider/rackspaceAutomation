package rackspaceAutomation;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public abstract class HTTP {
	protected String strURL;
	protected URL url;
	protected HttpURLConnection conn;  
	protected List<String> cookies;
	public HTTP(String url){
		strURL = url;  
	}
	public String getStrURL(){return this.strURL;}
	public URL getURL(){return this.url;}
	public void setURL(URL url){this.url = url;}
	public void setConn(HttpURLConnection conn){this.conn = conn;}
	public List<String> getCookies(){return this.cookies;}
	public void setCookies(List<String> cookies){this.cookies = cookies;}
	public HttpURLConnection getConn(){return this.conn;}
//	public String createConnection() throws IOException{
//		url = new URL(strURL);
//		conn= (HttpURLConnection) url.openConnection();   
//		return "";
//	}
}
