package HTTPperformance;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.mail.MessagingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import backEnd.deleteFileThread;
import backEnd.rackspaceSMTP;
import backEnd.sendFileThread;
import Graphics.MainPanel;

public class rackspaceCombination extends Thread{
	private static ArrayList<Integer> mailsIDs = new ArrayList<Integer>();
//	private final String to = "tony@cg.solutions";
	private final String to = "support-managers@cg.solutions,backoffice-shared@boitsoft.com,tony@cg.solutions";
	private String email;
	private String pass;
	private String emailName;
	private String ConnStatus;
	public rackspaceCombination(String email, String pass){
		this.email = email;
		this.pass = pass;
		emailName = email.substring(0, email.indexOf("@"))+" "+email.substring(email.indexOf("@")+1, email.indexOf("."));
	}
	public static boolean thereNoStuckMails(){return mailsIDs.isEmpty();}
	public static void clearMails(){mailsIDs = new ArrayList<Integer>();}
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
		String urlLogin = "https://apps.rackspace.com/login.php", rackspace = "https://apps.rackspace.com";
		String urlLoginParameters  = "hostname=mailtrust.com&type=email&fake_pwd=Password";
		HTTPpost login = null;
		HTTPget webmail = null;
		ArrayList<Thread> threadList = null;
		File folder = null;
		File[] listOfFiles = null;
		
		
		try {
			login = new HTTPpost(urlLogin,urlLoginParameters,email,pass);
			ConnStatus = login.createConnection();
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		if(ConnStatus.equals("302 Found")){
			

			login.firstSettings();
			webmail = new HTTPget(rackspace+login.getLocationResponse(),login.getCookies());
			try {
				ConnStatus = webmail.createConnection();
			} catch (IOException e1) {
				e1.printStackTrace();

				return;
			}
			if(ConnStatus.equals("200 OK")){
				
				
				synchronized(rackspaceCombination.class){
					MainPanel.setLog("Connected properly to "+emailName,"regular");
				}
				try {
					setIDS(webmail.getSource());
				} catch (ParseException | IOException e) {
					e.printStackTrace();
				}
				if(mailsIDs.size() != 0){
					
					threadList = new ArrayList<Thread>();
					
			        for(int i=0;i<mailsIDs.size();i++){
			        	Thread thread = new Thread((new HTTPpostDownload(emailName, "https://apps.rackspace.com/versions/webmail/16.4.5-RC/archive/fetch.php",login.getCookies(), login.getWSID()+"&msg_list=%5B%7B%22folder%22%3A%22INBOX%22%2C%22uid%22%3A%22",mailsIDs.get(i))));
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
			        
			        threadList = new ArrayList<Thread>();
			        folder = new File(System.getProperty("java.io.tmpdir"));
			        listOfFiles = folder.listFiles();
			        for(int i=0;i<listOfFiles.length;i++){
						if (listOfFiles[i].isFile()) {
							if(listOfFiles[i].getName().contains(emailName)){
								Thread thread = new Thread((new sendFileThread(email,pass,to,emailName,listOfFiles[i])));
					        	thread.start();
					        	threadList.add(thread);
							}
						}
					}
			        for(Thread t : threadList) {
			            // waits for this thread to die
			            try {
							t.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			        }
			        
			        
			        
			        for(int i=0;i<listOfFiles.length;i++){
						if (listOfFiles[i].isFile()) {
							if(listOfFiles[i].getName().contains(emailName)){
								Thread thread = new Thread((new deleteFileThread(listOfFiles[i].getAbsolutePath(),emailName)));
					        	thread.start();
					        	threadList.add(thread);
							}
						}
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
			}else{
				synchronized(rackspaceCombination.class){
					MainPanel.setLog(emailName+" -> Incorrect credentials","error");
				}
			}
		System.gc();
	}
}
