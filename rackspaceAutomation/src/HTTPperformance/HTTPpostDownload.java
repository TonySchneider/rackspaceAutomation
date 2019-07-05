package HTTPperformance;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.lingala.zip4j.exception.ZipException;
import Graphics.MainPanel;

public class HTTPpostDownload extends HTTPpost implements Runnable {
	private static int ID = 0;
	private String emailName;
	public HTTPpostDownload(String emailName, String url,List<String> cookies, String urlParameters,int mailID) {
		super(url,cookies, urlParameters+mailID+"%22%2C%22unread%22%3A%5Btrue%2Cfalse%5D%7D%5D");
		this.emailName = emailName;
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
			MainPanel.setLog("("+emailName+") Downloding file...","regular");
			createConnection();
			fileInputStream(emailName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.gc();
	}

}
