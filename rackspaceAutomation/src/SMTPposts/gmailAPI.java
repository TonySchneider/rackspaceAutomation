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

//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.util.Base64;
//import java.util.Properties;
//
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Multipart;
//import javax.mail.Session;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;

public class gmailAPI {
	public static void gmail(String username, String password, String from,String to, String Subject,String msg, File file){
		// Recipient's email ID needs to be mentioned.
//	      String to = "tony@cg.solutions";//change accordingly
//
//	      // Sender's email ID needs to be mentioned
//	      String from = "tony@cg.solutions";//change accordingly
//	      final String username = "tony@cg.solutions";//change accordingly
//	      final String password = "ifeirkpfqyibuvut";//change accordingly

	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "smtp.gmail.com";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "465");

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
//	         message.setText(msg);
	         
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

	         System.out.println("Sent message successfully....");

	      } catch (MessagingException e) {
	            throw new RuntimeException(e);
	      }
	}
	
	
//	public static MimeMessage createEmail(String to,String from,String subject,String bodyText)throws MessagingException {
//		Properties props = new Properties();
//		Session session = Session.getDefaultInstance(props, null);
//
//		MimeMessage email = new MimeMessage(session);
//
//		email.setFrom(new InternetAddress(from));
//		email.addRecipient(javax.mail.Message.RecipientType.TO,
//				new InternetAddress(to));
//		email.setSubject(subject);
//		email.setText(bodyText);
//		return email;
//	}
//	public static Message createMessageWithEmail(MimeMessage emailContent)throws MessagingException, IOException {
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        emailContent.writeTo(buffer);
//        byte[] bytes = buffer.toByteArray();
//        String encodedEmail = Base64.getUrlEncoder().encodeToString(bytes);
//        Message message = new Message();
//        message.setRaw(encodedEmail);
//        return message;
//    }
//	public static MimeMessage createEmailWithAttachment(String to,String from,String subject,String bodyText,File file)throws MessagingException, IOException {
//		Properties props = new Properties();
//		Session session = Session.getDefaultInstance(props, null);
//
//		MimeMessage email = new MimeMessage(session);
//
//		email.setFrom(new InternetAddress(from));
//		email.addRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(to));
//		email.setSubject(subject);
//
//		MimeBodyPart mimeBodyPart = new MimeBodyPart();
//		mimeBodyPart.setContent(bodyText, "text/plain");
//
//		Multipart multipart = new MimeMultipart();
//		multipart.addBodyPart(mimeBodyPart);
//
//		mimeBodyPart = new MimeBodyPart();
//		DataSource source = new FileDataSource(file);
//
//		mimeBodyPart.setDataHandler(new DataHandler(source));
//		mimeBodyPart.setFileName(file.getName());
//
//		multipart.addBodyPart(mimeBodyPart);
//		email.setContent(multipart);
//
//		return email;
//	}
//	public static Message sendMessage(Gmail service,String userId,MimeMessage emailContent)throws MessagingException, IOException {
//		Message message = createMessageWithEmail(emailContent);
//		message = service.users().messages().send(userId, message).execute();
//
//		System.out.println("Message id: " + message.getId());
//		System.out.println(message.toPrettyString());
//		return message;
//	}
//	private static Credential authorize() throws Exception {
//	   // load client secrets
//	   GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
//	       new InputStreamReader(CalendarSample.class.getResourceAsStream("/client_secrets.json")));
//	   // set up authorization code flow
//	   GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//	       httpTransport, JSON_FACTORY, clientSecrets,
//	       Collections.singleton(CalendarScopes.CALENDAR)).setDataStoreFactory(dataStoreFactory)
//	      .build();
//	   // authorize
//	   return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//	}
}
