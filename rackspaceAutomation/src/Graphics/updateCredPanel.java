package Graphics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class updateCredPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final JComboBox<String> emailMenu = new JComboBox<String>();
	private final JTextField passField = new JTextField();
	private final JButton update = new JButton("Update");
	public updateCredPanel(String title,JSONArray emails){
		setOpaque(false);
		setBorder(BorderFactory.createTitledBorder("Update Credentials"));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0,10,0,10);
//		c.gridx = 0;
		c.gridy = 0;
		JLabel email = new JLabel("Email: ");
		add(email,c);
		c.insets = new Insets(0,10,20,10);
		c.gridy = 1;
		for(int i=0;i<emails.size();i++){
			emailMenu.addItem((String)emails.get(i));
		}
		add(emailMenu,c);
		
		
		
		JLabel password = new JLabel("Password: ");
		c.insets = new Insets(0,10,0,10);
		c.gridy = 2;
		add(password,c);
		
		c.insets = new Insets(0,10,20,10);
		c.gridy = 3;
		add(passField,c);
		
		c.gridy = 4;
		add(update,c);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == update){
//			JSONParser parser = new JSONParser(); 
//			File f=new File("\\\\192.168.21.11\\tech support\\Tony's Tools\\RackSpace Shit\\credentials.json");
//			String path = f.getAbsolutePath();
//			JSONObject bodySource = null;
//			try {
//				bodySource = (JSONObject) parser.parse(new FileReader(path));
//			} catch (IOException | ParseException e1) {
//				e1.printStackTrace();
//			}
//			bodySource.put(emailMenu.getSelectedItem(), passField.getText());
//			
//			
//			BufferedWriter writer = null;
//	        try {
//
//	            File logFile = new File("\\\\192.168.21.11\\tech support\\Tony's Tools\\RackSpace Shit\\credentials.json");
//
//	            writer = new BufferedWriter(new FileWriter(logFile,true));
//	            writer.newLine();
//	            writer.write(bodySource);
//	        } catch (Exception e2) {
//	            e2.printStackTrace();
//	        } finally {
//	            try {
//	                // Close the writer regardless of what happens...
//	                writer.close();
//	            } catch (Exception e2) {
//	            }
//	        }
		}
	}
}
