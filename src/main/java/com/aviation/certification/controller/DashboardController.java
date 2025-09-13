package com.aviation.certification.controller;

import com.aviation.certification.model.User;
import com.aviation.certification.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class DashboardController {
	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
	private final UserService userService;
	
	public DashboardController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		logger.info("Dashboard request received");
		
		if (authentication == null || !authentication.isAuthenticated()) {
			return "redirect:/login";
		}
		
		String username = authentication.getName();
		Optional<User> userOptional = userService.findByUsername(username);
		
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			
			// Проверяем роли пользователя
			boolean isAdmin = user.getRoles().stream()
					.anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
			
			boolean isInstructor = user.getRoles().stream()
					.anyMatch(role -> role.getName().equals("ROLE_INSTRUCTOR"));
			
			boolean isCandidate = user.getRoles().stream()
					.anyMatch(role -> role.getName().equals("ROLE_CANDIDATE"));
			
			model.addAttribute("user", user);
			model.addAttribute("isAdmin", isAdmin);
			model.addAttribute("isInstructor", isInstructor);
			model.addAttribute("isCandidate", isCandidate);
			
			logger.info("User roles - Admin: {}, Instructor: {}, Candidate: {}", isAdmin, isInstructor, isCandidate);
			
			// Перенаправляем на соответствующую панель
			if (isAdmin) {
				return "redirect:/admin/dashboard";
			} else if (isInstructor) {
				return "redirect:/instructor/dashboard";
			} else if (isCandidate) {
				return "redirect:/candidate/dashboard";
			}
		}
		
		return "redirect:/home?error=access_denied";
	}
}