package rackspaceAutomation;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HTTPrequests {
	private static URL rackspace;
	public static String getSourceCode() throws IOException{
		rackspace = new URL("https://apps.rackspace.com/index.php");
		HttpURLConnection yc = (HttpURLConnection) rackspace.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine,source = "";
        while ((inputLine = in.readLine()) != null) 
        	source = source + inputLine + "\n";
        in.close();
        return source;
	}
	public static String returnInputStream(InputStream inputStream) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String inputLine,source = "";
		while ((inputLine = in.readLine()) != null) 
        	source = source + inputLine + "\n";
        in.close();
        return source;
	}
	public static void main(String[] args) throws IOException, ParseException{
		String urlParameters  = "hostname=mailtrust.com&type=email&fake_pwd=Password&user_name=finance%40enzocasino.com&password=wV6kERa4hkfH59RIEpNi";
		byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength = postData.length;
		String request        = "https://apps.rackspace.com/login.php";
		URL    url            = new URL( request );
		HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "POST" );
		conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		conn.setRequestProperty( "charset", "utf-8");
		conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		conn.setUseCaches( false );
		try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
		   wr.write( postData );
		   wr.flush();
		}
		System.out.println(conn.getResponseCode());
		System.out.println(conn.getResponseMessage());
		System.out.println(conn.getHeaderField("Location"));
		String Location = conn.getHeaderField("Location");
		List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
		String wsid = (String) Location.subSequence(15, Location.length());
//		System.out.println(returnInputStream(conn.getInputStream()));
		
//		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        String inputLine,source = "";
//        while ((inputLine = in.readLine()) != null) 
//        	source = source + inputLine + "\n";
//        in.close();
//        System.out.println(source);
        
        
        
        
		
		String request2        = "https://apps.rackspace.com"+Location;
		URL    url2            = new URL( request2 );
		HttpURLConnection conn2= (HttpURLConnection) url2.openConnection();   
		for (String cookie : cookies) {
			conn2.setRequestProperty("Cookie", cookie.split(";", 1)[0]);
		}
//		conn2.setDoOutput( true );
//		conn2.setInstanceFollowRedirects( false );
//		conn2.setRequestMethod( "GET" );
		System.out.println(conn2.getURL());
		System.out.println(conn2.getResponseCode());
		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        String inputLine,source = "";
        while ((inputLine = in.readLine()) != null) 
        	source = source + inputLine + "\n";
        in.close();
        
        int index = source.indexOf("MessageList.inbox_cache");
        int index2 = source.indexOf(";;");
        System.out.println(index);
        System.out.println();
        System.out.println(source.subSequence(index+26, index2));
        source = (String) source.subSequence(index+26, index2);
        JSONParser parser = new JSONParser();
        JSONObject bodySource = (JSONObject) parser.parse(source);
        JSONArray IDs = (JSONArray) bodySource.get("headers");
        JSONObject temp = null;
        String strSize;
        ArrayList<Integer> intIDS = new ArrayList<Integer>();
        for(int i=0;i<IDs.size();i++){
        	temp = (JSONObject) parser.parse(String.valueOf(IDs.get(i)));
        	strSize = (String)temp.get("size");
        	if(strSize.contains("MB")){
        		strSize = strSize.replace(" MB", "");
        		if(Integer.valueOf(strSize) > 10)
            		intIDS.add(Integer.valueOf((String)temp.get("id")));
        	}
        }
        for(int i=0;i<intIDS.size();i++)
        	System.out.println(intIDS.get(i));
        
        String urlParameters3  = wsid+"&msg_list=%5B%7B%22folder%22%3A%22INBOX%22%2C%22uid%22%3A%22"+intIDS.get(0)+"%22%2C%22unread%22%3Afalse%7D%5D";
		byte[] postData3       = urlParameters3.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength3 = postData3.length;
		String request3        = "https://apps.rackspace.com/versions/webmail/16.4.1-RC/archive/fetch.php";
		URL    url3            = new URL( request3 );
		HttpURLConnection conn3= (HttpURLConnection) url3.openConnection();           
		conn3.setDoOutput( true );
		conn3.setInstanceFollowRedirects( false );
		conn3.setRequestMethod( "POST" );
		for (String cookie : cookies) {
			conn3.setRequestProperty("Cookie", cookie.split(";", 1)[0]);
		}
		conn3.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		conn3.setRequestProperty( "charset", "utf-8");
		conn3.setRequestProperty( "Content-Length", Integer.toString( postDataLength3 ));
		conn3.setUseCaches( false );
		try( DataOutputStream wr = new DataOutputStream( conn3.getOutputStream())) {
		   wr.write( postData3 );
		   wr.flush();
		}
		System.out.println(conn3.getURL());
		System.out.println(conn3.getResponseCode());
		InputStream in3 = conn3.getInputStream();
		File downloadedFile = File.createTempFile("test", ".zip");
	    FileOutputStream out = new FileOutputStream(downloadedFile);        
	    byte[] buffer = new byte[1024];
	    int len = in3.read(buffer);
	    while (len != -1) {
	        out.write(buffer, 0, len);
	        len = in3.read(buffer);
	        if (Thread.interrupted()) {
	            try {
	                throw new InterruptedException();
	            } catch (InterruptedException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	    }
	    in.close();
	    out.close();
	    System.out.println(downloadedFile.getAbsolutePath());
//        Writer writer = null;
//
//        try {
//            writer = new BufferedWriter(new OutputStreamWriter(
//                  new FileOutputStream("source.txt"), "utf-8"));
//            writer.write(source);
//        } catch (IOException ex) {
//            // Report
//        } finally {
//           try {writer.close();} catch (Exception ex) {/*ignore*/}
//        }
//        
//        
	}
}
