package no.fagskolentelemark.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
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

import no.fagskolentelemark.GitIgnored;

public class Email {

	private static Session session = null;

	public static boolean init() {
		try {
			// Connection information
			Properties props =  new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.host", "m.outlook.com");
			props.put("mail.smtp.auth", "true");

			// Active session
			session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(GitIgnored.credentials[0], GitIgnored.credentials[1]);
				}
			});
			session.setDebug(false);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean sendMail(String htmlText, String email) {
		try {
			// Variables
			InternetAddress fromAddress = new InternetAddress(GitIgnored.credentials[1],
					GitIgnored.credentials[2]);
			InternetAddress toAddress = new InternetAddress(email);

			// Message
			Message msg = new MimeMessage(session);
			msg.setFrom(fromAddress);
			msg.addRecipient(Message.RecipientType.TO,toAddress);
			msg.addRecipient(Message.RecipientType.CC, new InternetAddress(
					GitIgnored.credentials[2]));
			msg.setSubject("Fagskolen Telemark - Brukernavn og passord");

			Multipart multipart = new MimeMultipart();

			// Text
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setContent(htmlText, "text/html");

			//			// Attachment
			//			MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			//
			//			URL url = new URL("https://www.dropbox.com/s/gzah5hhx07hoeea/Innlogging%2Btil%2BFEIDE.pdf?dl=1");
			//			URLDataSource uds = new URLDataSource(url);
			//			attachmentBodyPart.setDataHandler(new DataHandler(uds));
			//			attachmentBodyPart.setFileName("Innlogging til Fronter FEIDE.pdf");
			//			multipart.addBodyPart(attachmentBodyPart);

			msg.setContent(multipart);
			multipart.addBodyPart(textBodyPart);

			// Send email
			Transport.send(msg);
			return true;

		} catch (MessagingException e) {
			System.out.println(e.getMessage()+ e.getStackTrace());

		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			init();
			System.out.println("Retrying... " + email);
			sendMail(htmlText, email);
		}
		return false;
	}
}
