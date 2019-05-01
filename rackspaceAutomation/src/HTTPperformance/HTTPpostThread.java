package HTTPperformance;

import java.io.File;
import java.io.IOException;
import java.util.List;

import SMTPposts.rackspaceSMTP;

public class HTTPpostThread extends HTTPpost implements Runnable {
	private static int ID = 0;
	private int selfID;
	private String fileName;
	public HTTPpostThread(String fileName, String url,List<String> cookies, String urlParameters) {
		super(url,cookies, urlParameters);
		this.fileName = fileName;
		this.selfID = ID++;
	}

	@Override
	public void run() {
		try {
			System.out.println("("+fileName+") Downloding files...");
			createConnection();
			fileInputStream(fileName);
			
//			rackspaceSMTP.sendEmail("support@spinupcasino.com","gz94OPVkmGaNwy18Rl","support@spinupcasino.com","tony@cg.solutions","test","test",new File("C:/Users/tony/AppData/Local/Temp/Enzo_Financial_0_7580955758952788779.zip"));
			System.out.println("("+fileName+") done.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
