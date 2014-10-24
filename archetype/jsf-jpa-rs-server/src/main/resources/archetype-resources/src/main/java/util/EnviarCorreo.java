package ${package}.util;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;

import ${package}.configs.FrontEndConfig;
import ${package}.configs.MailConfig;

import app_common.app_common.util.FileCache;

public class EnviarCorreo {

	@Inject
	MailConfig mailConfig;

	@Inject
	private FrontEndConfig frontEndconfig;

	@Inject
	private Logger log;

	public void sendSSLMessage(String recipients[], String[] recipientsCC,
			String[] recipientsCCO, String subject, String message,
			String from, String fileName, String path) throws Exception {

		FileCache cache = new FileCache();
		String pathPie = mailConfig.getRoot() + mailConfig.getPieDePagina();
		String pieDePagina = "";

		try {
			pieDePagina = cache.getFileFromCache(pathPie);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Properties props = new Properties();

		props.put("mail.smtp.auth", mailConfig.getSmtpAuth());
		props.put("mail.smtp.starttls.enable", mailConfig.getSmtpStarttls());
		props.put("mail.smtp.host", mailConfig.getSmtpHostName());
		props.put("mail.smtp.port", mailConfig.getSmtpPort());
		props.put("mail.smtp.ssl.trust", mailConfig.getSmtpTrust());

		Session session = null;
		if (mailConfig.getSmtpAuth()) {
			session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(mailConfig
									.getEmailFromAddress(), mailConfig
									.getEmailFromPasswrd());
						}
					});
		} else {
			session = Session.getInstance(props);
		}

		session.setDebug(mailConfig.getSmtpDebug());

		BodyPart mensaje = new MimeBodyPart();
		// mensaje.setText(message);
		mensaje.setContent(message + pieDePagina,
				"text/html; charset=\"utf-8\"");
		MimeMultipart multiParte = new MimeMultipart();
		multiParte.addBodyPart(mensaje);

		BodyPart messageBodyPart = new MimeBodyPart();
		DataSource fds = new FileDataSource(mailConfig.getRoot()
				+ mailConfig.getIsoImage());
		messageBodyPart.setDataHandler(new DataHandler(fds));
		messageBodyPart.setHeader("Content-ID", "<iso>");
		// add it
		multiParte.addBodyPart(messageBodyPart);

		if (path != null) {
			BodyPart adjunto = new MimeBodyPart();
			adjunto.setDataHandler(new DataHandler(new FileDataSource(path)));
			adjunto.setFileName(fileName);
			multiParte.addBodyPart(adjunto);
		}

		Message msg = new MimeMessage(session);

		InternetAddress addressFrom = new InternetAddress(
				mailConfig.getEmailFromAddress());
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// verifica si destinatario CC no es vacio
		if (recipientsCC[0].isEmpty() == false) {

			InternetAddress[] addressToCC = new InternetAddress[recipientsCC.length];
			for (int i = 0; i < recipientsCC.length; i++) {
				addressToCC[i] = new InternetAddress(recipientsCC[i]);
			}
			msg.setRecipients(Message.RecipientType.CC, addressToCC);

		}

		// verifica si destinatario CCO no es vacio
		if (recipientsCCO[0].isEmpty() == false) {

			InternetAddress[] addressToCCO = new InternetAddress[recipientsCCO.length];
			for (int i = 0; i < recipientsCCO.length; i++) {
				addressToCCO[i] = new InternetAddress(recipientsCCO[i]);
			}
			msg.setRecipients(Message.RecipientType.BCC, addressToCCO);

		}

		// Configura el asunto y el tipo de contenido
		msg.setSubject(subject);
		msg.setContent(multiParte);
		Transport.send(msg);
		/*
		 * Transport transport = session.getTransport("smtp");
		 * transport.connect(mailConfig.getSmtpHostName(),
		 * mailConfig.getSmtpPort(), mailConfig.getEmailFromAddress(),
		 * mailConfig.getEmailFromPasswrd()); transport.sendMessage(msg,
		 * msg.getAllRecipients());
		 */

	}

}