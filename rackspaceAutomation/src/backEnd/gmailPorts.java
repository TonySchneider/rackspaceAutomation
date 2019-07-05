package backEnd;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

public class gmailPorts {
	public static boolean readEmail(String username, String password) throws MessagingException, IOException{
		// 1. Setup properties for the mail session.
        Properties props = new Properties();
        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.pop3.socketFactory.fallback", "false");
        props.put("mail.pop3.socketFactory.port", "995");
        props.put("mail.pop3.port", "995");
        props.put("mail.pop3.host", "pop.gmail.com");
        props.put("mail.pop3.user", username);
        props.put("mail.store.protocol", "pop3");

        // 2. Creates a javax.mail.Authenticator object.
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        // 3. Creating mail session.
        Session session = Session.getDefaultInstance(props, auth);

        // 4. Get the POP3 store provider and connect to the store.
        Store store = session.getStore("pop3");
        store.connect("pop.gmail.com", username, password);

        // 5. Get folder and open the INBOX folder in the store.
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);

        // 6. Retrieve the messages from the folder.
        Message[] messages = inbox.getMessages();
        for (Message message : messages) {
        	System.out.println(message.getReceivedDate());
        	message.setFlag(Flags.Flag.DELETED, true);
        }

        // 7. Close folder and close store.
        inbox.close(true);
        store.close();
		return true;
	}
}
