package com.smart.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entites.User;
import com.smart.helper.Message;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userrepository;
	@GetMapping("/home")
	public String home_new(Model model) {
		model.addAttribute("name", "hushar");	
		return "home";
	}
	
	@GetMapping("/dashboard")
	@ResponseBody
	public Principal dashboard(Principal principal) {
		//model.addAttribute("name", "hushar");	
		return principal;
	}
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("name", "hushar");	
		return "about";
	}
	@GetMapping("/sing-up")
	public String sign_up(Model model) {
		model.addAttribute("name", "hushar");	
		return "login_user";
	}
	
	@GetMapping("/sign-in")
	public String sign_in(Model model) {
		model.addAttribute("user",new User());	
		return "signin";
	}
	@PostMapping("/process")
	public String sign(@Valid @ModelAttribute("user") User user,BindingResult result,HttpSession session,Model model) {
		if(result.hasErrors()) {
			System.out.println(result);
			session.setAttribute("message", new Message("something went wrong !!", "alert-danger"));
			return "signin";
		}
	
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageurl("download.png");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userrepository.save(user);
		model.addAttribute("user",new User());
		session.setAttribute("message", new Message("Registration Successfully !!", "alert-success"));
	
		return "signin";
	}
	
	

}
