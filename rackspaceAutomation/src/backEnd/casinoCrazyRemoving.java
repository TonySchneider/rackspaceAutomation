package backEnd;

import java.io.IOException;

import javax.mail.MessagingException;

public class casinoCrazyRemoving {

	public static void main(String[] args) {
		try {
			gmailPorts.readEmail("finance@crazycasinoclub.com","");
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
	}
}
