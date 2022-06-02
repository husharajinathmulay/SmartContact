package com.smart.entites;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;
@Entity
@Table(name="USER")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
private int id;
	@NotBlank(message="username not empty")
	@Size(min=3,max=12,message="username between 3 to 12 charcters")
private String name;
private String secondName;
private String work;
@Column(unique=true)
@NotBlank(message="emil id not empty")
@Email(regexp="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message="Invalid email")
private String email;
@NotBlank(message="password not empty")
@Size(min=3,max=500,message="password between 3 to 500 charcters")
private String password;
private boolean enabled;
private String role;
private String imageurl;
@Column(length=500)
@NotBlank(message="about not empty")
@Size(min=3,max=12,message="about between 3 to 120 charcters")
private String about;
@Enumerated(EnumType.STRING)
@Column(name="auth_provider")
private AuthenticationProvider authenticationProvider;
private String description;
//@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
@OneToMany(cascade=CascadeType.ALL,mappedBy="user",orphanRemoval=true)
private List<Contact> contact=new ArrayList<Contact>();
@AssertTrue(message="check terms and condition")
private boolean aggred;
public User() {
	super();
	// TODO Auto-generated constructor stub
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getSecondName() {
	return secondName;
}
public void setSecondName(String secondName) {
	this.secondName = secondName;
}
public String getWork() {
	return work;
}
public void setWork(String work) {
	this.work = work;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public boolean isEnabled() {
	return enabled;
}
public void setEnabled(boolean enabled) {
	this.enabled = enabled;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}

public String getImageurl() {
	return imageurl;
}
public void setImageurl(String imageurl) {
	this.imageurl = imageurl;
}
public String getAbout() {
	return about;
}
public void setAbout(String about) {
	this.about = about;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public List<Contact> getContact() {
	return contact;
}
public void setContact(List<Contact> contact) {
	this.contact = contact;
}
public boolean isAggred() {
	return aggred;
}
public void setAggred(boolean aggred) {
	this.aggred = aggred;
}

public AuthenticationProvider getAuthenticationProvider() {
	return authenticationProvider;
}
public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
	this.authenticationProvider = authenticationProvider;
}

@Override
public String toString() {
	return "User [id=" + id + ", name=" + name + ", secondName=" + secondName + ", work=" + work + ", email=" + email
			+ ", password=" + password + ", enabled=" + enabled + ", role=" + role + ", imageurl=" + imageurl
			+ ", about=" + about + ", authenticationProvider=" + authenticationProvider + ", description=" + description
			+ ", contact=" + contact + ", aggred=" + aggred + "]";
}
public User(int id,
		@NotBlank(message = "username not empty") @Size(min = 3, max = 12, message = "username between 3 to 12 charcters") String name,
		String secondName, String work,
		@NotBlank(message = "emil id not empty") @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email") String email,
		@NotBlank(message = "password not empty") @Size(min = 3, max = 500, message = "password between 3 to 500 charcters") String password,
		boolean enabled, String role, String imageurl,
		@NotBlank(message = "about not empty") @Size(min = 3, max = 12, message = "about between 3 to 120 charcters") String about,
		AuthenticationProvider authenticationProvider, String description, List<Contact> contact,
		@AssertTrue(message = "check terms and condition") boolean aggred) {
	super();
	this.id = id;
	this.name = name;
	this.secondName = secondName;
	this.work = work;
	this.email = email;
	this.password = password;
	this.enabled = enabled;
	this.role = role;
	this.imageurl = imageurl;
	this.about = about;
	this.authenticationProvider = authenticationProvider;
	this.description = description;
	this.contact = contact;
	this.aggred = aggred;
}





}