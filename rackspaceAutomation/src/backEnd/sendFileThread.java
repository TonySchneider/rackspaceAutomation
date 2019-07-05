package backEnd;

import java.io.File;

import Graphics.MainPanel;

public class sendFileThread implements Runnable {
	private String email;
	private String pass;
	private String to;
	private String emailName;
	private File file;
	public sendFileThread(String email,String pass,String to,String emailName,File file){
		this.email = email;
		this.pass = pass;
		this.to = to;
		this.emailName = emailName;
		this.file = file;
	}
	@Override
	public void run() {
		MainPanel.setLog("("+emailName+") Sending file..", "regular");
		rackspaceSMTP.sendEmail(email,pass,email,to,"[No Reply] Stuck Mail on "+emailName,"Hey,\n File attached.",file);
		
		System.gc();
	}

}
