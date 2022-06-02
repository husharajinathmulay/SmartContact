package com.smart.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entites.User;
import com.smart.helper.Message;

@Controller
public class ForgotController {
	// simple
	@Autowired
	private UserRepository userrepository;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	// private EmailService emailService;
	// @Autowired
	// private SendEmailService sendEmailService;
	Random random = new Random(1000);

	@GetMapping("/forgot-password")
	public String openemailform() {
		return "forgot_email_form";
	}

	@PostMapping("/send-opt")
	public String send_otp(@RequestParam("email") String email, HttpSession session) throws Exception {

		int otp = random.nextInt(999999);
		// System.out.println(otp);
//	String  massage ="<h1>OTP= "+otp+" </h1>";
//	String subject="Smart Contatint Manager";
		String to = "hushar2018@gmail.com";
//	String host="smtp.gmail.com";
//	String port="587";
//	String userName="hushar2018@gmail.com";
//	String password="Husharmulay@12345";
//	String toAddress="hushar2018@gmail.com";
		// sendEmailService.sendHtmlEmail(host, port, password, userName, toAddress,
		// subject, massage);
		User user = userrepository.getUserByUserName(email);
		if (user == null) {
			session.setAttribute("message", new Message("you have enterd wrong email id !!", "alert-danger"));
			return "forgot_email_form";
		} else {
			javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject("Smart Contatint Manager");
			String html = "<!doctype html>\n" + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"\n"
					+ "      xmlns:th=\"http://www.thymeleaf.org\">\n" + "<head>\n" + "    <meta charset=\"UTF-8\">\n"
					+ "    <meta name=\"viewport\"\n"
					+ "          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n"
					+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" + "    <title>OTP</title>\n"
					+ "</head>\n" + "<body>\n" + "\n" + "<div>OTP : <b>" + otp + "</b></div>\n" + "\n" + "</body>\n"
					+ "</html>\n";

			helper.setText(html, true);
			helper.setTo(to);
			javaMailSender.send(mimeMessage);
//			String Apikey="7i2MagpZmJbNYsXDzH6lvT8kVBryQRo1OfG3dhSEKnP45cCIA02qU0vJKfmaFBpIn8dChrLwtc1MX5DH";
//			String sendeId="FSTSMS";
//			String language="English";
//			String route="P";
//			message=URLEncoder.encode(message,"UTF-8");
//			String number="7083719169";
//			SendSms.sendSms("this is custom massage using java code","7083719169");
//			String myurl="https://www.fast2sms.com/dev/bulkV2?authorization="+Apikey+"&sender_id="+sendeId+"&message"+message+"&language="+language+"route="+route+"&numbers="+number;
//			URL url=new URL(myurl);
//			HttpsURLConnection con=(HttpsURLConnection)url.openConnection();
//			con.setRequestMethod("GET");
//			con.setRequestProperty("User-Agent", "Mozilla/5.0");
//			con.setRequestProperty("cache-control", "no-cache");
//			int code=con.getResponseCode();
//			StringBuffer response=new StringBuffer();
//		    BufferedReader br =new BufferedReader(new InputStreamReader(con.getInputStream()));
//		    while(true) {
//		    	String line=br.readLine();
//		    	if(line==null) {
//		    		break;
//		    	}
//		    	response.append(line);
//		    }
		    
			// boolean flag=this.emailService.sendmail(subject, massage, to);
			// simple email start
//	SimpleMailMessage message=new SimpleMailMessage();
//	message.setFrom("hushar2018@gmail.com");
//	message.setTo(to);
//	message.setText(massage);
//	message.setSubject(subject);
//	javaMailSender.send(message);

			// simple email end
			// String sent=javaMailSender.send(message);
			// return "verify_otp";
//	System.out.println(flag);
//	if(flag) {
//		return "change_password";
//	}else {
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			session.setAttribute("message", new Message("we have send otp your email !!", "alert-success"));
			return "verify_otp";
		}

	}

	@PostMapping("verify-opt")
	public String otp_verity(@RequestParam("otp") int otp, HttpSession session) {
		int myotp = (int) session.getAttribute("myotp");
		// Integer new_otp=(int)otp;
		// System.out.println(myotp);
		// System.out.println(otp);
		String email = (String) session.getAttribute("email");

		if (myotp == otp) {

			User user = userrepository.getUserByUserName(email);
			if (user == null) {
				session.setAttribute("message", new Message("you have enterd wrong email !!", "alert-danger"));
				return "forgot_email_form";
			} else {
				session.setAttribute("message", new Message("OTP verify successfully !!", "alert-success"));
				return "change_new_password";
			}

		} else {
			System.out.println("no");
			session.setAttribute("message", new Message("you have enterd wrong otp !!", "alert-danger"));
			return "verify_otp";
		}

	}
	
	@PostMapping("change-new-password")
	public String otp_verity(@RequestParam("password") String password, HttpSession session) {
		
	    	String email = (String) session.getAttribute("email");
		    User user = userrepository.getUserByUserName(email);
			user.setPassword(this.passwordEncoder.encode(password));
			this.userrepository.save(user);
			session.setAttribute("message", new Message("Password Change Successfully !!", "alert-success"));
		
		return "redirect:/sing-up";

	}
}
