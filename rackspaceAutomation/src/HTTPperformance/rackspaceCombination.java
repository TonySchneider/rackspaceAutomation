package HTTPperformance;

import java.awt.List;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.mail.MessagingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Graphics.MainPanel;
import SMTPposts.rackspaceSMTP;

public class rackspaceCombination implements Runnable {
	public static int threadID = 0;
	private final ArrayList<Integer> mailsIDs = new ArrayList<Integer>();
	private String email;
	private String pass;
	private String emailName;
	private String ConnStatus;
	public rackspaceCombination(String email, String pass){
		this.email = email;
		this.pass = pass;
		emailName = email.substring(0, email.indexOf("@"))+" "+email.substring(email.indexOf("@")+1, email.indexOf("."));
	}
	public void setIDS(String source) throws ParseException{
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
        		if(Double.valueOf(strSize) > 10.0)
            		mailsIDs.add(Integer.valueOf((String)temp.get("id")));
        	}
        }
	}
	@Override
	public void run() {
		threadID++;
		String urlLogin = "https://apps.rackspace.com/login.php", rackspace = "https://apps.rackspace.com";
		String urlLoginParameters  = "hostname=mailtrust.com&type=email&fake_pwd=Password";
		
		HTTPpost login = null;
		try {
			login = new HTTPpost(urlLogin,urlLoginParameters,email,pass);
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			ConnStatus = login.createConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/////////////////////
		if(ConnStatus.equals("302 Found")){
			login.firstSettings();
			HTTPget webmail = new HTTPget(rackspace+login.getLocationResponse(),login.getCookies());
			try {
				ConnStatus = webmail.createConnection();
				if(ConnStatus.equals("200 OK")){
					System.out.println("Connected properly to "+emailName);
					MainPanel.setLog("Connected properly to "+emailName,"regular");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(ConnStatus.equals("200 OK")){
				/////////////////////
				try {
					setIDS(webmail.getSource());
				} catch (ParseException | IOException e) {
					e.printStackTrace();
				}
				/////////////////////
				
				if(mailsIDs.size() != 0){

					ArrayList<Thread> threadList = new ArrayList<Thread>();
			        for(int i=0;i<mailsIDs.size();i++){
			        	Thread thread = new Thread((new HTTPpostThread(emailName, "https://apps.rackspace.com/versions/webmail/16.4.1-RC/archive/fetch.php",login.getCookies(), login.getWSID()+"&msg_list=%5B%7B%22folder%22%3A%22INBOX%22%2C%22uid%22%3A%22",mailsIDs.get(i),email,pass)));
			        	thread.start();
			        	threadList.add(thread);
			        }
			        for(Thread t : threadList) {
			            // waits for this thread to die
			            try {
							t.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			        }
			        try {
						rackspaceSMTP.removeEmail(email, pass);
					} catch (MessagingException | IOException e) {
						e.printStackTrace();
					}
				}
			}
			if(threadID == 1){
				threadID--;
				MainPanel.setLog("DONE --------------- DONE ", "done");
			}
		}
		else{
			System.out.println("Connection Error at "+emailName+" the pass is: "+pass);
			MainPanel.setLog("Connection Error at "+emailName,"error");
		}
		
	}
}
