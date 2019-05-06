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
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import HTTPperformance.HTTPpostThread;
import HTTPperformance.rackspaceCombination;

public class MainPanel extends JPanel implements ActionListener{
	private BufferedImage background = loadImage();
	private static Dimension backgroundSize;
	private final JPanel topPanel = new JPanel(),powerPanel = new JPanel(),logPanel = new JPanel();
	private updateCredPanel credentialsPanel;
	private static final JTextPane logs = new JTextPane();
	private static JSONArray emails;
	private static JSONObject credentials;
	private final Power power = new Power("/Images/power.png","/Images/power2.png");;
	public MainPanel(){
		try {
			parseEmailCredentials();
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		setLayout(new GridLayout(2,1));
		topPanel.setLayout(new GridLayout(1,2));
		topPanel.setOpaque(false);
//		credentialsPanel.setOpaque(false);
		
		powerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		JLabel warning= new JLabel(new ImageIcon(this.getClass().getResource("/Images/warning.jpg"))); 
		powerPanel.add(warning,c);
		powerPanel.setOpaque(false);
		credentialsPanel = new updateCredPanel("Update Credentials",emails);
		topPanel.add(credentialsPanel);
		logPanel.setOpaque(false);
		power.addActionListener(this);
		c.gridy = 1;
		powerPanel.add(power,c);
		
		topPanel.add(powerPanel);
		add(topPanel);
		
		logPanel.setBorder(BorderFactory.createTitledBorder(null, "Logs", TitledBorder.LEFT, TitledBorder.TOP, new Font("Monospace",Font.BOLD,12), Color.RED));
		logs.setPreferredSize(new Dimension(450,190));
//		logs.setLineWrap(true);
		logs.setEditable(false);
		DefaultCaret caret = (DefaultCaret) logs.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane logsScroll = new JScrollPane(logs);
		logPanel.add(logsScroll);
		
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
			power.setEnabled(false);
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
	public static void setLog(String log,String type){
		StyledDocument doc = logs.getStyledDocument();

        Style style = logs.addStyle("I'm a Style", null);
		if(type.equals("error")){
			StyleConstants.setForeground(style, Color.red);
			try { doc.insertString(doc.getLength(), log+"\n",style); }
	        catch (BadLocationException e){}
		}else if(type.equals("regular")){
			StyleConstants.setForeground(style, Color.black);
			try { doc.insertString(doc.getLength(), log+"\n",style); }
	        catch (BadLocationException e){}
		}else if(type.equals("No emails")){
			StyleConstants.setForeground(style, Color.BLUE);
			try { doc.insertString(doc.getLength(), log+"\n",style); }
	        catch (BadLocationException e){}
		}else if(type.equals("done")){
			StyleConstants.setForeground(style, Color.GREEN);
			StyleConstants.setFontSize(style, 25);
			StyleConstants.setBold(style,true);
			try { doc.insertString(doc.getLength(), log+"\n",style); }
	        catch (BadLocationException e){}
		}
	}
	public static JSONArray getEmail(){
		return emails;
	}
}
