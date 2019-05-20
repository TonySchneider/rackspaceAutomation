package SMTPposts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.mail.Message;
import javax.mail.MessagingException;

public class donwloadFileThread implements Runnable {
	public static int ID = 0;
	private Message message;
	private String folderName;
	public donwloadFileThread(Message message,String folderName){
		this.message = message;
		this.folderName = folderName;
		ID++;
	}

	@Override
	public void run() {
//		OutputStream out = null;
//		try {
//			InputStream in = getConn().getInputStream();
//			File downloadedFile = File.createTempFile(fileName+"_", ".zip");
//		    FileOutputStream out = new FileOutputStream(downloadedFile);        
//		    byte[] buffer = new byte[1024];
//		    int len = in.read(buffer);
//		    while (len != -1) {
//		        out.write(buffer, 0, len);
//		        len = in.read(buffer);
//		        if (Thread.interrupted()) {
//		            try {
//		                throw new InterruptedException();
//		            } catch (InterruptedException e) {
//		                e.printStackTrace();
//		            }
//		        }
//		    }
//		    in.close();
//		    out.close();
		    
		    
		try {
		    InputStream in = message.getInputStream();
		    File downloadedFile = File.createTempFile(message.getSubject(), ".eml",new File(System.getProperty("java.io.tmpdir")+folderName+"\\"));
		    FileOutputStream out = new FileOutputStream(downloadedFile);   
		    byte[] buffer = new byte[1024];
		    int len = in.read(buffer);
		    while (len != -1) {
		        out.write(buffer, 0, len);
		        len = in.read(buffer);
		        if (Thread.interrupted()) {
		            try {
		                throw new InterruptedException();
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }finally{
		            	in.close();
		    		    out.close();
		            }
		        }
		    }

		    
		    
//			String fileName = message.getSubject().replace('\\',' ').replace('/',' ').replace(':',' ').replace('*',' ').replace('?',' ').replace('"',' ').replace('<',' ').replace('>',' ').replace('|',' ');
//			File tempFile = new File(System.getProperty("java.io.tmpdir")+folderName+"\\"+message.getSubject()+".eml");
//			if(tempFile.exists())
//				out = new FileOutputStream(System.getProperty("java.io.tmpdir")+folderName+"\\"+message.getSubject()+ID+".eml");
//			else
//				out = new FileOutputStream(System.getProperty("java.io.tmpdir")+folderName+"\\"+message.getSubject()+".eml");
//		    message.writeTo(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
