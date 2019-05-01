package HTTPperformance;

import java.io.File;
import java.io.IOException;
import java.util.List;

import Graphics.MainPanel;
import SMTPposts.rackspaceSMTP;

public class HTTPpostThread extends HTTPpost implements Runnable {
	private static int ID = 0;
	private int selfID;
	private String emailName;
	public HTTPpostThread(String emailName, String url,List<String> cookies, String urlParameters,String email,String pass) {
		super(url,cookies, urlParameters);
		this.emailName = emailName;
		this.selfID = ID++;
		setEmail(email);
		setPass(pass);
	}
	public boolean deleteFile(String path){
		File file = new File(path); 
		if(file.delete()) { 
            return true;
        } 
        else{ 
        	return false;
        } 
	}
	@Override
	public void run() {
		try {
			MainPanel.setLog("("+emailName+") Downloding files...");
//			System.out.println("("+emailName+") Downloding files...");
			createConnection();
			String filePath = fileInputStream(emailName);
			if(rackspaceSMTP.sendEmail(getEmail(),getPass(),getEmail(),"tony@cg.solutions,boris.v@cg.solutions","Stuck Mail on "+emailName,"Hey,\n File attached.",new File(filePath))){
//				System.out.println("("+emailName+")Mail sent.");
				MainPanel.setLog("("+emailName+")Mail sent.");
			}
			deleteFile(filePath);
//			System.out.println("("+emailName+") done.");
			MainPanel.setLog("("+emailName+") done.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
