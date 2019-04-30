package Graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	private static MainFrame frame = null;
	private static Dimension frameSize;
	public MainFrame(String title){
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(0,0));//set border layout for set 2 panels - one for the city background and one for the below buttons
	}
	public static MainFrame getInstance(){
		if(frame == null)
			frame = new MainFrame("Rackstar Downloader");
		return frame;
	}
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//get the screen size.
		MainFrame frame = MainFrame.getInstance();
		final MainPanel backgroundPanel = new MainPanel();

		frameSize = new Dimension((int)MainPanel.getBackgroundSize().getWidth(),(int)MainPanel.getBackgroundSize().getHeight());
//		final ButtonsPanel buttonsPanel = new ButtonsPanel();
		frame.add(backgroundPanel);
//		frame.add(buttonsPanel,BorderLayout.SOUTH);//stick the panel of the buttons in the SOUTH location.  
		frame.setPreferredSize(new Dimension(frameSize.width,(int)(frameSize.height)));//set size of the frame as the size of the background
		frame.setLocation(screenSize.width/2-frame.getPreferredSize().width/2, screenSize.height/2-frame.getPreferredSize().height/2);//set the frame in the middle of the screen
		
		frame.pack(); 
		frame.setVisible(true); 
		
	}
	
}