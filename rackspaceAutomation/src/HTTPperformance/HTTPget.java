package HTTPperformance;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class HTTPget extends HTTP {
	public HTTPget(String url) {
		super(url);
	}
	public HTTPget(String url,List<String> cookies) {
		super(url,cookies);
	}
	public String createConnection() throws IOException{
		super.createConnection();
		return getRespondeStatus();
	}
	public String getSource() throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(getConn().getInputStream()));
        String inputLine,source = "";
        while ((inputLine = in.readLine()) != null) 
        	source = source + inputLine + "\n";
        in.close();
        return source;
	}
}
