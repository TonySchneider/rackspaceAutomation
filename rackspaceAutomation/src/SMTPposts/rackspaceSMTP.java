package SMTPposts;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class rackspaceSMTP {
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
		 		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		 		mimeBodyPart.setContent(msg, "text/plain");
		 
		 		Multipart multipart = new MimeMultipart();
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
