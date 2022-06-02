package com.smart.service;

import java.io.IOException;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
//import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;
@Service
public class EmailService {
	public  boolean sendmail(String subject,String massage,String to) throws AddressException, MessagingException, IOException {
		String from="hushar2018@gmail.com";  
		boolean f=false;
		Properties props = new Properties();
		   props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.starttls.enable", "true");
		   props.put("mail.smtp.host", "smtp.gmail.com");
		   props.put("mail.smtp.port", "587");
		   
		   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication("hushar2018@gmail.com", "Husharmulay@12345");
		      }
		   });
		   Message msg = new MimeMessage(session);
		   try {
			   msg.setFrom(new InternetAddress(from, false));
				  // m.setFrom(from);
				   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
				   msg.setSubject(subject);
				   msg.setContent("Tutorials point email", "text/html");
				   msg.setSentDate(new Date());
				  // msg.setText(massage);
				   Transport.send(msg);
				   f=true;
		   }
		   catch(Exception e) {
			   e.printStackTrace();
		   }
		  
		   
		   return f;
		}
}


