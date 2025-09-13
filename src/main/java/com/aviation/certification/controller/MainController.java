package com.aviation.certification.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/home")
	public String homePage() {
		return "home";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/dashboard")
	public String dashboard() {
		logger.info("Dashboard page requested");
		return "dashboard";
	}
}
