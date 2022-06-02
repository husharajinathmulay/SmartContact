package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.smart.dao.ContactRepository;
import com.smart.dao.MyorderRepository;
import com.smart.dao.UserRepository;
import com.smart.entites.Contact;
import com.smart.entites.Myorder;
import com.smart.entites.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userrepository;
	@Autowired
	private ContactRepository contactrepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private MyorderRepository myorderRepository;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String username = principal.getName();
		User user = userrepository.getUserByUserName(username);
		model.addAttribute("user", user);

	}

	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {
		return "normal/user_dashboard";
	}
	
	
	
//	@GetMapping("/orders/{page}")
//	public String show_order(@PathVariable("page") Integer page, Model model, Principal principal) {
	@GetMapping("/orders")
	public String show_order(Model model, Principal principal) {
	String username = principal.getName();
		User user = userrepository.getUserByUserName(username);
		//Pageable pageable = PageRequest.of(page, 3);
		//Page<Myorder> order =myorderRepository.findOrderByUser(user.getId(),pageable);
	List<Myorder> order =myorderRepository.findOrderByUser(user.getId());
	//order.forEach(id->{System.out.println(id);});
		model.addAttribute("order", order);
    	//model.addAttribute("currentPage", page);
		//model.addAttribute("totalPages", order.getTotalPages());
		//System.out.println(order.getTotalPages());
		//System.out.println(page);
		// List<Contact> contact=user.getContact();
		return "normal/orders";
	}

	@GetMapping("/show-contact/{page}")
	public String show_contacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		String username = principal.getName();
		User user = userrepository.getUserByUserName(username);
		Pageable pageable = PageRequest.of(page, 3);
		Page<Contact> contact = contactrepository.findContactByUser(user.getId(), pageable);
		model.addAttribute("contact", contact);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contact.getTotalPages());
		
		// List<Contact> contact=user.getContact();
		return "normal/show_contacts";
	}

	@GetMapping("/contact-details/{id}")
	public String contact_details(@PathVariable("id") Integer id, Model model, Principal principal) {
		String username = principal.getName();
		User user = userrepository.getUserByUserName(username);
		Optional<Contact> contactoptional = contactrepository.findById(id);
		Contact contact = contactoptional.get();
		if (user.getId() == contact.getUser().getId())
			model.addAttribute("contact", contact);
		return "normal/show_details";
	}

	@GetMapping("/your-profile")
	public String contact_details() {
		return "normal/profile_details";
	}

	@GetMapping("/change-password")
	public String change_password() {
		return "normal/change_password";
	}

	@GetMapping("/contact-delete/{id}")
	public String delete_details(@PathVariable("id") Integer id, HttpSession session, Principal principal) {
		String username = principal.getName();
		User user = userrepository.getUserByUserName(username);
		Optional<Contact> contactoptional = contactrepository.findById(id);
		Contact contact = contactoptional.get();
		user.getContact().remove(contact);
		this.userrepository.save(user);

		// if(user.getId()==contact.getUser().getId())
		// contact.setUser(null);
		// contactrepository.delete(contact);
		System.out.println("deleted successfully");
		session.setAttribute("message", new Message("Contact deleted Successfully !!", "alert-success"));
//		model.addAttribute("contact", contact);
		return "redirect:/user/show-contact/0";
	}

	@PostMapping("/contact-update/{id}")
	public String update_details(@PathVariable("id") Integer id, Model model, HttpSession session) {
		Optional<Contact> contactoptional = contactrepository.findById(id);
		Contact contact = contactoptional.get();
		// if(user.getId()==contact.getUser().getId())
		// System.out.println("deleted successfully");
		// session.setAttribute("message", new Message("Contact deleted Successfully
		// !!", "alert-success"));
		model.addAttribute("contact", contact);
		// model.addAttribute("contact", new Contact());
		return "normal/update_contact";
	}

	@GetMapping("/add-contact")
	public String add_contact(Model model, Principal principal) {
		model.addAttribute("contact", new Contact());
		return "normal/add_contact";
	}

	@PostMapping("/contact-process")
	public String processContact(@Valid @ModelAttribute Contact contact, BindingResult result,
			@RequestParam("image") MultipartFile file, Principal principal, HttpSession session) throws Exception {
//		if(result.hasErrors()) {
//			System.out.println(result);

//			return "normal/add_contact";
//		}

		try {
			String name = principal.getName();
			User user = userrepository.getUserByUserName(name);
			contact.setUser(user);
			if (file.isEmpty()) {
				contact.setImage("4.jpg");

			} else {
				contact.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("image uploaded");
			}
			user.getContact().add(contact);
			userrepository.save(user);
			session.setAttribute("message", new Message("Contact added Successfully !!", "alert-success"));
		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
			session.setAttribute("message", new Message("something went wrong !!", "alert-danger"));
		}

		return "normal/add_contact";
	}

	@PostMapping("/contact-update-process")
	public String updateContact(@Valid @ModelAttribute Contact contact, BindingResult result,
			@RequestParam("image") MultipartFile file, Principal principal, HttpSession session) throws Exception {
		try {
			String name = principal.getName();
			User user = userrepository.getUserByUserName(name);
			Contact oldcontact = contactrepository.findById(contact.getId()).get();
			contact.setUser(user);
			// System.out.println(contact.getEmail());
			if (file.isEmpty()) {
				contact.setImage(oldcontact.getImage());

			} else {
				// String image=oldcontact.getImage();
				File deleteFile = new ClassPathResource("static/image").getFile();
				File file1 = new File(deleteFile, oldcontact.getImage());
				file1.delete();
				contact.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("image uploaded");
			}
			// user.getContact().add(contact);
			contactrepository.save(contact);
			session.setAttribute("message", new Message("Contact updated Successfully !!", "alert-success"));
		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
			session.setAttribute("message", new Message("something went wrong !!", "alert-danger"));
		}

		return "redirect:/user/show-contact/0";
	}

	@PostMapping("/change-password-form")
	public String change_pass(@RequestParam("old_password") String old_password,
			@RequestParam("new_password") String new_password, Model model, Principal principal, HttpSession session)
			throws Exception {
		String name = principal.getName();
		User user = userrepository.getUserByUserName(name);
		String password = user.getPassword();
		if (passwordEncoder.matches(old_password, password)) {
			user.setPassword(this.passwordEncoder.encode(new_password));
			this.userrepository.save(user);
			session.setAttribute("message", new Message("Password Change Successfully !!", "alert-success"));
		} else {
			session.setAttribute("message", new Message("Please enter correct old password !!", "alert-danger"));
		}
		return "redirect:/user/change-password";
	}

	@PostMapping("/create-order")
	@ResponseBody
	public String orders(@RequestBody Map<String, Object> data,Principal principal) throws Exception {
		 String name = principal.getName();
		 User user = userrepository.getUserByUserName(name);
		int amt = Integer.parseInt(data.get("amount").toString());
		//int amte=(String)amt;
		var client = new RazorpayClient("rzp_test_Wn2lcwFTeCfzXU", "57nZNvfj2KNGANLIemyk7MeN");
		JSONObject obj = new JSONObject();
		obj.put("amount", amt * 100);
		obj.put("currency", "INR");
		obj.put("receipt", "txn_235425");
		Order order = client.Orders.create(obj);
		Myorder myorder=new Myorder();
		myorder.setAmount(order.get("amount")+" ");
		myorder.setOrderId(order.get("id"));
		myorder.setPaymentId(null);
		myorder.setStatus("created");
		myorder.setUser(user);
		myorder.setReciept(order.get("receipt"));
		this.myorderRepository.save(myorder);
		
		return order.toString();
	}

	@PostMapping("/payment-success")
	@ResponseBody
	public ResponseEntity<?> updateorder(@RequestBody  Map<String,Object> data){
		Myorder order =myorderRepository.findByOrderId(data.get("order_id").toString());
		order.setPaymentId(data.get("payment_id").toString());
		order.setStatus(data.get("status").toString());
		myorderRepository.save(order);
		return ResponseEntity.ok(Map.of("msg","updated"));
	}

}