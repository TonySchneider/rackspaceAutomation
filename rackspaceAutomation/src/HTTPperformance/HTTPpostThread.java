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
	private int mailID;
	public HTTPpostThread(String emailName, String url,List<String> cookies, String urlParameters,int mailID,String email,String pass) {
		super(url,cookies, urlParameters+mailID+"%22%2C%22unread%22%3A%5Btrue%2Cfalse%5D%7D%5D");
		this.emailName = emailName;
		this.selfID = ID++;
		this.mailID = mailID;
		setEmail(email);
		setPass(pass);
	}
	public boolean deleteFile(String path){
		File file = new File(path); 
		if(file.delete())
			return true;
		return false;
	}
	@Override
	public void run() {
		try {
			//support-managers@cg.solutions,backoffice-shared@boitsoft.com,
			String to = "support-managers@cg.solutions,backoffice-shared@boitsoft.com,tony@cg.solutions";
			MainPanel.setLog("("+emailName+") Downloding file...","regular");
			System.out.println("("+emailName+") Downloding file...");
			createConnection();
			String folderPath = fileInputStream(emailName);
			
			if(folderPath != null){
				File folder = new File(folderPath);
				File[] listOfFiles = folder.listFiles();
				if (listOfFiles[0].isFile()) {
					if(rackspaceSMTP.sendEmail(getEmail(),getPass(),getEmail(),to,"[No Reply] Stuck Mail on "+emailName,"Hey,\n File attached.",new File(listOfFiles[0].getAbsolutePath()))){
						System.out.println("("+emailName+")Mail sent.");
						MainPanel.setLog("("+emailName+")Mail sent.","regular");
					}
					deleteFile(listOfFiles[0].getAbsolutePath());
				}
				deleteFile(folderPath);
			}
			else
				MainPanel.setLog("("+emailName+") Folder does not exists", "error");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
