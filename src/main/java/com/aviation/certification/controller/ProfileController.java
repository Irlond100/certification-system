package com.aviation.certification.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.aviation.certification.service.UserService;
import com.aviation.certification.repository.UserRepository;

@Controller
public class ProfileController {
	private final UserService userService;
	private final UserRepository userRepository;
	
	public ProfileController(UserService userService, UserRepository userRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
	}
	
	@GetMapping("/profile")
	public String profile(Model model, Authentication authentication) {
		String username = authentication.getName();
		com.aviation.certification.model.User user = userRepository.findByUsernameWithRolesAndSpecializations(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
		model.addAttribute("user", user);
		return "profile";
	}
}