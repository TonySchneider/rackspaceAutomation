package rackspaceAutomation;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPpost extends HTTP {
	private String locationResponse;
	private String urlParameters;
	private String wsid;
	private byte[] postData;
	private int postDataLength;
	public HTTPpost(String url, String urlParameters) {
		super(url);
		this.urlParameters = urlParameters;
		postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
		postDataLength = postData.length;
	}
	public String getLocationResponse(){return this.locationResponse;}
	public String getWSID(){return this.wsid;}
	public String createConnection() throws IOException{
		setURL(new URL(getStrURL()));
		setConn((HttpURLConnection)getURL().openConnection());
//		url = new URL(strURL);
//		conn= (HttpURLConnection) url.openConnection();  
		this.conn.setDoOutput( true );
		this.conn.setInstanceFollowRedirects( false );
		this.conn.setRequestMethod( "POST" );
		this.conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		this.conn.setRequestProperty( "charset", "utf-8");
		this.conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		this.conn.setUseCaches( false );
		try( DataOutputStream wr = new DataOutputStream( this.conn.getOutputStream())) {
		   wr.write( postData );
		   wr.flush();
		}
		setCookies(this.conn.getHeaderFields().get("Set-Cookie"));
		this.locationResponse = this.conn.getHeaderField("Location");
		this.wsid = (String) getLocationResponse().subSequence(15, getLocationResponse().length());
		return "("+getClass().getSimpleName()+")Status: "+this.conn.getResponseCode()+" "+this.conn.getResponseMessage();
	}
}
