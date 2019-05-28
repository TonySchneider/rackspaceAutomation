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

import threadClasses.deleteFileThread;
import threadClasses.rackspaceSMTP;
import threadClasses.sendFileThread;
import Graphics.MainPanel;

public class rackspaceCombination extends Thread{
	private static int activeThreads = 0;
	private static int ID = 0;
	private static int noStuckEmailsID = 0;
	private final ArrayList<Integer> mailsIDs = new ArrayList<Integer>();
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
//	public static boolean deleteFile(String path){
//		File file = new File(path); 
//		if(file.delete())
//			return true;
//		return false;
//	}
//	public static void deleteTempFiles(){
//		File folder = new File(System.getProperty("java.io.tmpdir"));
//		File[] listOfFiles = folder.listFiles();
//		for(int i=0;i<listOfFiles.length;i++)
//			if (listOfFiles[i].isFile()) 
//				listOfFiles[i].delete();
//	}
	@Override
	public void run() {
//		System.out.println(rackspaceCombination.currentThread().getThreadGroup().activeCount());
//		if(rackspaceCombination.currentThread().getThreadGroup().activeCount() == 85){
//			synchronized(rackspaceCombination.class){
//				System.out.println("(start run)There are 82 alive threads");
//			}
//		}
		String urlLogin = "https://apps.rackspace.com/login.php", rackspace = "https://apps.rackspace.com";
		String urlLoginParameters  = "hostname=mailtrust.com&type=email&fake_pwd=Password";
		HTTPpost login = null;
		try {
			login = new HTTPpost(urlLogin,urlLoginParameters,email,pass);
			ConnStatus = login.createConnection();
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
//		if(rackspaceCombination.currentThread().getThreadGroup().activeCount() == 85){
//			synchronized(rackspaceCombination.class){
//				System.out.println("(After login)There are 82 alive threads");
//			}
//		}else{
//			synchronized(rackspaceCombination.class){
//			System.out.println("(After login)There are not 85 alive threads -> "+rackspaceCombination.currentThread().getThreadGroup().activeCount()+"amount of alive threads");
//			}
//		}
		/////////////////////
		if(ConnStatus.equals("302 Found")){
//			if(rackspaceCombination.currentThread().getThreadGroup().activeCount() == 85){
//				synchronized(rackspaceCombination.class){
//					System.out.println("(After 302 Found)There are 82 alive threads");
//				}
//			}else{
//				synchronized(rackspaceCombination.class){
//				System.out.println("(After 302 Found)There are not 85 alive threads -> "+rackspaceCombination.currentThread().getThreadGroup().activeCount()+"amount of alive threads");
//				}
//			}
			login.firstSettings();
			HTTPget webmail = new HTTPget(rackspace+login.getLocationResponse(),login.getCookies());
			try {
				ConnStatus = webmail.createConnection();
			} catch (IOException e1) {
				e1.printStackTrace();
//				synchronized(rackspaceCombination.class){
//					System.out.println("createConnection Exceprion");
//				}
				return;
			}
//			if(rackspaceCombination.currentThread().getThreadGroup().activeCount() == 85){
//				synchronized(rackspaceCombination.class){
//					System.out.println("(After Create Connection)There are 82 alive threads");
//				}
//			}else{
//				synchronized(rackspaceCombination.class){
//				System.out.println("(After Create Connection)There are not 85 alive threads -> "+rackspaceCombination.currentThread().getThreadGroup().activeCount()+"amount of alive threads");
//				}
//			}
			if(ConnStatus.equals("200 OK")){
				
//				if(rackspaceCombination.currentThread().getThreadGroup().activeCount() == 85){
//					synchronized(rackspaceCombination.class){
//						System.out.println("(After OK statuc)There are 82 alive threads");
//					}
//				}else{
//					synchronized(rackspaceCombination.class){
//					System.out.println("(After OK statuc)There are not 85 alive threads -> "+rackspaceCombination.currentThread().getThreadGroup().activeCount()+"amount of alive threads");
//					}
//				}
				
				synchronized(rackspaceCombination.class){
//					System.out.println("Connected properly to "+emailName);
					MainPanel.setLog("Connected properly to "+emailName,"regular");
				}
				try {
					setIDS(webmail.getSource());
				} catch (ParseException | IOException e) {
					e.printStackTrace();
				}
//				if(rackspaceCombination.currentThread().getThreadGroup().activeCount() == 85){
//					synchronized(rackspaceCombination.class){
//						System.out.println(emailName+"(After webmail.getSource)There are 82 alive threads");
//					}
//				}else{
//					synchronized(rackspaceCombination.class){
//					System.out.println(emailName+"(After webmail.getSource)There are not 85 alive threads -> "+rackspaceCombination.currentThread().getThreadGroup().activeCount()+"amount of alive threads");
//					}
//				}
				activeThreads = Thread.currentThread().getThreadGroup().activeCount();
				if(mailsIDs.size() != 0){
					synchronized(rackspaceCombination.class){
						ID++;
					}
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
			        
			        
			        
			        threadList = new ArrayList<Thread>();
			        File folder = new File(System.getProperty("java.io.tmpdir"));
			        File[] listOfFiles = folder.listFiles();
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
			        
					ID--;
					if(ID == 0)
						MainPanel.setLog("DONE!","done");
			        
					
					
					
//			        String folderPath = System.getProperty("java.io.tmpdir")+emailName;
//			        
//			        if(new File(folderPath).exists()){
//			        	for(int i=0;i<threadList.size();i++)
//			        		threadList.get(i).interrupt();
//			        	
//			        	
//			        	threadList = new ArrayList<Thread>();
//			        	
//						File folder = new File(folderPath);
//						File[] listOfFiles = folder.listFiles();
//						for(int i=0;i<listOfFiles.length;i++){
//							if (listOfFiles[i].isFile()) {
//								Thread thread = new Thread((new sendFileThread(email,pass,to,emailName,listOfFiles[i])));
//					        	thread.start();
//					        	threadList.add(thread);
//							}
//						}
//						for(Thread t : threadList) {
//				            // waits for this thread to die
//				            try {
//								t.join();
//							} catch (InterruptedException e) {
//								e.printStackTrace();
////								synchronized(rackspaceCombination.class){
////									System.out.println("Join Exceprion");
////								}
//								return;
//							}
//				        }
//						
						
//						for(int i=0;i<listOfFiles.length;i++)
//							deleteFile(listOfFiles[i].getAbsolutePath());
//						deleteFile(folderPath);
//						
//						try {
//							rackspaceSMTP.removeEmail(email, pass);
//						} catch (MessagingException | IOException e) {
//							e.printStackTrace();
////							synchronized(rackspaceCombination.class){
////								System.out.println("removeEmail Exceprion");
////							}
//							return;
//						}
//						synchronized(rackspaceCombination.class){
//							System.out.println("("+emailName+") Done");
//							MainPanel.setLog("("+emailName+") Done", "regular");
//							ID--;
//							if(ID == 0){
//								for(int i=0;i<threadList.size();i++)
//					        		threadList.get(i).interrupt();
//								deleteTempFiles();
//								MainPanel.setLog("DONE!","done");
//								interrupt();
//							}
//						}
			        }else{
			        	noStuckEmailsID++;
			        }
				}
			}else{
				synchronized(rackspaceCombination.class){
//					System.out.println(emailName+" -> Incorrect credentials");
					MainPanel.setLog(emailName+" -> Incorrect credentials","error");
				}
			}
//		if(activeThreads == noStuckEmailsID){
//			System.out.println("No Stuck Emails!");
//			MainPanel.setLog("No Stuck Emails!","done");
//		}
	}
//	public static void raiseStaticID(){
//		synchronized(rackspaceCombination.class){
//			noStuckEmailsID++;
////			System.out.println(noStuckEmailsID);
//			if(noStuckEmailsID == 65){
//				deleteTempFiles();
//				MainPanel.setLog("No Stuck Emails!","done");
//			}
//		}
//	}
}
