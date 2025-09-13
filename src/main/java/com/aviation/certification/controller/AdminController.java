package com.aviation.certification.controller;

import com.aviation.certification.model.User;
import com.aviation.certification.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	private final UserService userService;
	
	public AdminController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/dashboard")
	public String adminDashboard(Model model) {
		return "admin/dashboard";
	}
	
	@GetMapping("/users")
	public String manageUsers(Model model) {
		List<User> users = userService.findAll();
		model.addAttribute("users", users);
		return "admin/users";
	}
	
	@GetMapping("/tests")
	public String manageTests() {
		return "admin/tests";
	}
	
	@GetMapping("/statistics")
	public String viewStatistics() {
		return "admin/statistics";
	}
}