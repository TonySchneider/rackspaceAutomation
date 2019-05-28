package threadClasses;

import java.io.File;

import Graphics.MainPanel;

public class deleteFileThread extends Thread {
	private String pathFile;
	private String emailName;
	public deleteFileThread(String pathFile,String emailName){
		this.pathFile = pathFile;
		this.emailName = emailName;
	}
	public static boolean deleteFile(String path){
		File file = new File(path); 
		if(file.delete())
			return true;
		return false;
	}
	public void run(){
		System.out.println("("+emailName+") Deleting file..");
		MainPanel.setLog("("+emailName+") Deleting file..", "regular");
		
		deleteFile(pathFile);
	}
}
