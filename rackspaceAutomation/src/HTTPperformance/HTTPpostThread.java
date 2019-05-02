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
			MainPanel.setLog("("+emailName+") Downloding files...","regular");
			System.out.println("("+emailName+") Downloding files...");
			createConnection();
			String dictionaryPath = fileInputStream(emailName);
			String params = "type=batch&roe=false&jobs=%5B%7B%22call%22%3A%22Mail.deleteMessages%22%2C%22args%22%3A%5B%5B%7B%22folder%22%3A%22INBOX%22%2C%22uid%22%3A%22"+mailID+"%22%2C%22unread%22%3Afalse%7D%5D%2Cfalse%5D%7D%5D&wsid="+getWSID();
			HTTPpost toTrash = new HTTPpost("https://apps.rackspace.com/a/router.php",getCookies(),params);
			String toTrashStatus = toTrash.createConnection();
			if(toTrashStatus.equals("200 OK")){
				File folder = new File(dictionaryPath);
				File[] listOfFiles = folder.listFiles();
				for (int i = 0; i < listOfFiles.length; i++) {
					  if (listOfFiles[i].isFile()) {
						  if(rackspaceSMTP.sendEmail(getEmail(),getPass(),getEmail(),"tony@cg.solutions","Stuck Mail on "+emailName,"Hey,\n File attached.",new File(listOfFiles[i].getAbsolutePath()))){
								System.out.println("("+emailName+")Mail sent.");
								MainPanel.setLog("("+emailName+")Mail sent.","regular");
							}
					  }
					}
				deleteFile(dictionaryPath);
				System.out.println("("+emailName+") done.");
				MainPanel.setLog("("+emailName+") done.","regular");
			}
			else
				MainPanel.setLog("("+emailName+") Doesn't go to trash", "error");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
