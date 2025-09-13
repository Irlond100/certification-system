package com.aviation.certification.controller;

import com.aviation.certification.model.User;
import com.aviation.certification.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class ProfileController {
	private final UserService userService;
	
	public ProfileController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/profile")
	public String profile(Authentication authentication, Model model) {
		String username = authentication.getName();
		Optional<User> userOptional = userService.findByUsername(username);
		
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			model.addAttribute("user", user);
			return "profile";
		}
		
		return "redirect:/dashboard?error=user_not_found";
	}
}