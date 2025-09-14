package com.aviation.certification.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.aviation.certification.service.UserService;

@Controller
public class ProfileController {
	private final UserService userService;
	
	public ProfileController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/profile")
	public String profile(Model model, Authentication authentication) {
		String username = authentication.getName();
		com.aviation.certification.model.User user = userService.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
		model.addAttribute("user", user);
		return "profile";
	}
}