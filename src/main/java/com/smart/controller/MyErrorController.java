package com.smart.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;

public class MyErrorController implements ErrorController{
	@GetMapping("/error")
public String error() {
	return "error";
}
}
