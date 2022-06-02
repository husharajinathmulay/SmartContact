package com.smart.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.smart.controller.CustomOAuth2User;
import com.smart.controller.CustomOAuth2UserService;
import com.smart.controller.OauthSuccesshandler;
//import org.springframework.stereotype.Component;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
@Configuration
@EnableWebSecurity
public class MyConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private CustomOAuth2UserService oauth2UserService;
	//@Autowired
	//private OauthSuccesshandler oauthSuccesshandler;
@Bean
public UserDetailsService getUserDetailsService() {
	return new UserDetailsServiceImpl();
}
@Bean
public BCryptPasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
}
@Bean
public DaoAuthenticationProvider authentionProvider() {
	DaoAuthenticationProvider daoauthenticationprovider= new DaoAuthenticationProvider();
	daoauthenticationprovider.setUserDetailsService(this.getUserDetailsService());
	daoauthenticationprovider.setPasswordEncoder(passwordEncoder());
	return  daoauthenticationprovider;
}

protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.authenticationProvider(authentionProvider());
}

protected void configure(HttpSecurity http) throws Exception {
	//http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasRole("USER").antMatchers("/**").permitAll().and().formLogin().loginPage("/sing-up").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index").and().csrf().disable();
	//http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasRole("USER").antMatchers("/**").permitAll().and().formLogin().loginPage("/sing-up").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index").and().csrf().disable();
	//http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasRole("USER").antMatchers("/**").permitAll().and().formLogin().loginPage("/sing-up").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index").and().csrf().disable();
	//http.antMatcher("/**").authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasRole("USER").antMatchers("/**").permitAll().and().formLogin().loginPage("/sing-up").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index").and().oauth2Login().and().csrf().disable();
	http.authorizeRequests()
	.antMatchers("/oauth2/**").permitAll()
	.antMatchers("/admin/**").hasRole("ADMIN")
	.antMatchers("/user/**").hasRole("USER")
	.antMatchers("/**").permitAll()
	.and()
	.formLogin()
	 .loginPage("/sing-up")
	 .loginProcessingUrl("/dologin")
	 .defaultSuccessUrl("/user/index")
	.and()
	.oauth2Login()
	 .loginPage("/sing-up")
	 .userInfoEndpoint().userService(oauth2UserService)
	 .and()
//	 .successHandler(new OauthSuccesshandler() {
//		 @Override
//		 public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//					Authentication authentication) throws IOException, ServletException {
//				CustomOAuth2User outhuser=(CustomOAuth2User) authentication.getPrincipal();
//				String email=outhuser.getEmail();
//	 
//	            response.sendRedirect("user/index");
//	        }
//	    })
	// .successHandler(oauthSuccesshandler)
	.and()
	.csrf().
	disable();
	
	
	
		//http.antMatcher("/**").authorizeRequests().antMatchers("/").permitAll().anyRequest().authenticated().and().oauth2Login();

}



}
