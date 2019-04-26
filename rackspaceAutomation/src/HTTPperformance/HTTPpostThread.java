package HTTPperformance;

import java.io.IOException;
import java.util.List;

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
			System.out.println(createConnection());
			fileInputStream(fileName);
			System.out.println("Thread "+selfID+"is done.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
