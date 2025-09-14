package com.aviation.certification.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.aviation.certification.service.UserService;

@Controller
public class DashboardController { // Добавляем public
	private final UserService userService;
	
	public DashboardController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/dashboard")
	public String dashboardRedirect(Authentication authentication) {
		if (authentication.getAuthorities().stream()
				.anyMatch(grantedAuthority ->
						grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
			return "redirect:/admin/dashboard";
		} else if (authentication.getAuthorities().stream()
				.anyMatch(grantedAuthority ->
						grantedAuthority.getAuthority().equals("ROLE_CANDIDATE"))) {
			return "redirect:/candidate/dashboard";
		}
		return "redirect:/home";
	}
}