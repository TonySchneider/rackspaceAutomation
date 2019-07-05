package backEnd;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class rackspaceSMTP {
	public static boolean removeEmail(String username, String password) throws MessagingException, IOException{
		Properties properties = new Properties();
		String host = "imap.emailsrvr.com";
		properties.put("mail.imap.host", host);
		properties.put("mail.imap.port", "143");
		properties.put("mail.imap.starttls.enable", "true");
		Session emailSession = Session.getDefaultInstance(properties);

		//create the POP3 store object and connect with the pop server
		Store store = emailSession.getStore("imap");

		store.connect(host, username, password);

		//create the folder object and open it
		Folder emailFolder = store.getFolder("INBOX");
		emailFolder.open(Folder.READ_WRITE);

		// retrieve the messages from the folder in an array and print it
		Message[] messages = emailFolder.getMessages();
		
		for (int i = 0, n = messages.length; i < n; i++) {
			if((double)messages[i].getSize()/1000000 > 10.0){
				messages[i].setFlag(Flags.Flag.DELETED, true);
			}
		}

		//close the store and folder objects
//		emailFolder.expunge();
		emailFolder.close(true);
		store.close();
		return true;
	}
	public static boolean sendEmail(String username, String password, String from,String to, String Subject,String msg, File file){
		// Recipient's email ID needs to be mentioned.
		//		      String to = "tony@cg.solutions";//change accordingly
		//
		//		      // Sender's email ID needs to be mentioned
		//		      String from = "tony@cg.solutions";//change accordingly
		//		      final String username = "tony@cg.solutions";//change accordingly
		//		      final String password = "ifeirkpfqyibuvut";//change accordingly

		// Assuming you are sending email through relay.jangosmtp.net
		String host = "smtp.emailsrvr.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "25");

		// Get the Session object.
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject(Subject);

			// Now set the actual message
			//		         message.setText(msg);

			//Attaching file

			Multipart multipart = new MimeMultipart();
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(msg, "text/plain");
			
			multipart.addBodyPart(mimeBodyPart);
			
			mimeBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(file);

			mimeBodyPart.setDataHandler(new DataHandler(source));
			mimeBodyPart.setFileName(file.getName());

			multipart.addBodyPart(mimeBodyPart);
			message.setContent(multipart);
			
			
			// Send message
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}
}
