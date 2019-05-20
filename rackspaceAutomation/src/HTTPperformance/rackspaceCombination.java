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

import Graphics.MainPanel;
import SMTPposts.rackspaceSMTP;
import SMTPposts.sendFileThread;

public class rackspaceCombination implements Runnable {
	private final ArrayList<Integer> mailsIDs = new ArrayList<Integer>();
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
//		String email = "finance@lordofthespins.com", pass = "nW23dbU2g4iuhfasSbHSc", emailName = "support lordofthespins",to = "tony@cg.solutions";
//		String folderPath = null;
//		try {
//			folderPath = rackspaceSMTP.donwloadEmail(email,pass,emailName);
//		} catch (MessagingException | IOException e1) {
//			System.out.println("ERROR - ("+emailName+")Downloading files..");
//			MainPanel.setLog("ERROR - ("+emailName+")Downloading files..","error");
//		}
//		if(folderPath != null){
//			File folder = new File(folderPath);
//			File[] listOfFiles = folder.listFiles();
//			if(listOfFiles != null){
//				ArrayList<Thread> threadList = new ArrayList<Thread>();
//				for(int i=0;i<listOfFiles.length;i++) {
//					System.out.println("("+emailName+")Sending files..");
//					MainPanel.setLog("("+emailName+")Sending files..","regular");
//					Thread thread = new Thread(new sendFileThread(email,pass,"support-managers@cg.solutions,backoffice-shared@boitsoft.com,tony@cg.solutions",emailName,listOfFiles[i]));
//					thread.start();
//		        	threadList.add(thread);
//				}
//		        for(Thread t : threadList) {
//		            try {
//						t.join();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//		        }
//				for(int i=0;i<listOfFiles.length;i++){
//					System.out.println("("+emailName+")Deleting files..");
//					MainPanel.setLog("("+emailName+")Deleting files..","regular");
//					if (listOfFiles[i].isFile())
//						deleteFile(listOfFiles[i].getAbsolutePath());
//				}
////				deleteFile(folderPath);
//				try {
//					rackspaceSMTP.removeEmail(email, pass);
//				} catch (MessagingException | IOException e) {
//					System.out.println("ERROR - ("+emailName+")Deleting files..");
//					MainPanel.setLog("ERROR - ("+emailName+")Deleting files..","error");
//				}
//			}
//			deleteFile(folderPath);
//		}
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
			        	Thread thread = new Thread((new HTTPpostDownload(emailName, "https://apps.rackspace.com/versions/webmail/16.4.1-RC/archive/fetch.php",login.getCookies(), login.getWSID()+"&msg_list=%5B%7B%22folder%22%3A%22INBOX%22%2C%22uid%22%3A%22",mailsIDs.get(i))));
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
			        String folderPath = System.getProperty("java.io.tmpdir")+emailName;
			        
			        if(new File(folderPath).exists()){
			        	threadList = new ArrayList<Thread>();
			        	
						File folder = new File(folderPath);
						File[] listOfFiles = folder.listFiles();
						for(int i=0;i<listOfFiles.length;i++){
							if (listOfFiles[i].isFile()) {
								Thread thread = new Thread((new sendFileThread(email,pass,to,emailName,listOfFiles[i])));
					        	thread.start();
					        	threadList.add(thread);
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
						
						
						for(int i=0;i<listOfFiles.length;i++)
							deleteFile(listOfFiles[i].getAbsolutePath());
						deleteFile(folderPath);
						
						try {
							rackspaceSMTP.removeEmail(email, pass);
						} catch (MessagingException | IOException e) {
							e.printStackTrace();
						}
						System.out.println("("+emailName+") Done");
						MainPanel.setLog("("+emailName+") Done", "regular");
					}
					else
						MainPanel.setLog("("+emailName+") Folder does not exists", "error");
			        
				}
			}
		}
		else{
			System.out.println("Connection Error at "+emailName+" the pass is: "+pass);
			MainPanel.setLog("Connection Error at "+emailName,"error");
		}
	}
	public static boolean deleteFile(String path){
		File file = new File(path); 
		if(file.delete())
			return true;
		return false;
	}
}
