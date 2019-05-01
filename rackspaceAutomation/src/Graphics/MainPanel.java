package Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import HTTPperformance.rackspaceCombination;

public class MainPanel extends JPanel implements ActionListener{
	private BufferedImage background = loadImage();
	private static Dimension backgroundSize;
	private final JPanel topPanel = new JPanel(),logPanel = new JPanel();
	private static final JTextArea logs = new JTextArea(12,40);
	private static JSONArray emails;
	private static JSONObject credentials;
	private final Power power = new Power("/Images/power.png","/Images/power2.png");;
	public MainPanel(){
		setLayout(new GridLayout(2,1));
		topPanel.setOpaque(false);
		logPanel.setOpaque(false);
		power.addActionListener(this);
		topPanel.add(power);
		add(topPanel);
		
		logPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		JLabel logFile = new JLabel("Log File");
		logFile.setFont(new Font("Calibri", Font.BOLD, 18));
		logFile.setForeground(Color.red);
		logPanel.add(logFile,c);
		
		logs.setLineWrap(true);
		logs.setEditable(false);
		JScrollPane logsScroll = new JScrollPane(logs);
		c.gridy = 1;
		logPanel.add(logsScroll,c);
		
		add(logPanel);
	}
	private BufferedImage loadImage(){
        URL imagePath = getClass().getResource("/Images/background.jpg");
        BufferedImage result = null;
        try {
            result = ImageIO.read(imagePath);//read the image file with ImageIO class.
            backgroundSize = new Dimension(result.getWidth(),result.getHeight());
        } catch (IOException e) {//Catch due to try to read the image file.
            System.err.println("Error, the image is not found.");
        }
        return result;
    }
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);//Call the default method due to overriding.
        setPreferredSize(backgroundSize);
        Dimension size = getSize();//get the size of this panel and set it into Dimension(size) object.
        g.drawImage(background, 0, 0,size.width, size.height,0, 0, background.getWidth(), background.getHeight(), null);//set the background in 0,0 location (drawing the background)
    }
	public static Dimension getBackgroundSize(){return backgroundSize;}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == power){
			try {
				parseEmailCredentials();
			} catch (IOException | ParseException e1) {
				e1.printStackTrace();
			}
			for(int i=0; i<emails.size();i++){
				new Thread(new rackspaceCombination((String)emails.get(i), (String)credentials.get(emails.get(i)))).start();
			}
		}
	}
	public static void parseEmailCredentials() throws FileNotFoundException, IOException, ParseException{
		JSONParser parser = new JSONParser(); 
		File f=new File("credentials.json");
		String path = f.getAbsolutePath();
		path = path.replace("\\", "\\\\").replace("rackspaceAutomation\\\\credentials.json", "rackspaceAutomation\\\\src\\\\HTTPperformance\\\\credentials.json");
		JSONObject bodySource = (JSONObject) parser.parse(new FileReader(path));
		emails = (JSONArray)bodySource.get("emails");
		credentials = (JSONObject)bodySource.get("credentials");
	}
	public static void setLog(String log){
		logs.setText(logs.getText()+"\n"+log);
	}
}
