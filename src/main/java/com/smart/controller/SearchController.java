package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entites.Contact;
import com.smart.entites.User;

@RestController
public class SearchController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepositry;
	@GetMapping("/search/{query}")
public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal){
		User user=userRepository.getUserByUserName(principal.getName());
//		List<Contact> contact = contactRepositry.findByNameAndUser(query,user);
		List<Contact> contact = contactRepositry.findByNameAndUser(query,user);
		//List<Contact> contact=contactRepositry.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contact);
		
	}
}
