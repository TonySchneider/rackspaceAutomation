package HTTPperformance;

import java.util.ArrayList;
import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class mainProject {
	private static final ArrayList<Integer> mailsIDs = new ArrayList<Integer>();
	
	public static void main(String[] args) throws IOException, ParseException{
		String urlLogin = "https://apps.rackspace.com/login.php", rackspace = "https://apps.rackspace.com";
		String urlLoginParameters  = "hostname=mailtrust.com&type=email&fake_pwd=Password&user_name=finance@bronzecasino.com&password=cdRYPwbox9Qcs2uePSCfrUNW";
		
		
		HTTPpost login = new HTTPpost(urlLogin,urlLoginParameters);
		System.out.println(login.createConnection());
		login.firstSettings();
		String wsid = login.getWSID();
		/////////////////////
		
		HTTPget webmail = new HTTPget(rackspace+login.getLocationResponse(),login.getCookies());
		System.out.println(webmail.createConnection());

		/////////////////////
		setIDS(webmail.getSource());
		/////////////////////
		
		
        for(int i=0;i<mailsIDs.size();i++)
        	new Thread((new HTTPpostThread("Bronze_Support_"+i, "https://apps.rackspace.com/versions/webmail/16.4.1-RC/archive/fetch.php",login.getCookies(), wsid+"&msg_list=%5B%7B%22folder%22%3A%22INBOX%22%2C%22uid%22%3A%22"+mailsIDs.get(i)+"%22%2C%22unread%22%3Afalse%7D%5D"))).start();
        	
	}
	public static void setIDS(String source) throws ParseException{
		int index = source.indexOf("MessageList.inbox_cache");
        int index2 = source.indexOf(";;");
        source = (String) source.subSequence(index+26, index2);
        JSONParser parser = new JSONParser();
        JSONObject bodySource = (JSONObject) parser.parse(source);
        JSONArray IDs = (JSONArray) bodySource.get("headers");
        JSONObject temp = null;
        String strSize;
        for(int i=0;i<IDs.size();i++){
        	temp = (JSONObject) parser.parse(String.valueOf(IDs.get(i)));
        	strSize = (String)temp.get("size");
        	if(strSize.contains("MB")){
        		strSize = strSize.replace(" MB", "");
        		if(Integer.valueOf(strSize) > 10)
            		mailsIDs.add(Integer.valueOf((String)temp.get("id")));
        	}
        }
	}
}
