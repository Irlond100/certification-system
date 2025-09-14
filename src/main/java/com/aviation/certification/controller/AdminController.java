package com.aviation.certification.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.aviation.certification.service.UserService;
import org.springframework.security.core.Authentication;
import com.aviation.certification.model.User;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	private final UserService userService;
	
	public AdminController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/dashboard")
	public String adminDashboard() {
		return "admin/dashboard";
	}
	
	@GetMapping("/users")
	public String manageUsers(Model model, Authentication authentication) {
		String currentUsername = authentication.getName();
		model.addAttribute("users", userService.findAll());
		model.addAttribute("currentUsername", currentUsername);
		return "admin/users";
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUserForm(@PathVariable Long id, Model model) {
		Optional<User> user = userService.findById(id);
		if (user.isPresent()) {
			model.addAttribute("user", user.get());
			return "admin/user-edit";
		}
		return "redirect:/admin/users";
	}
	
	@PostMapping("/users/edit/{id}")
	public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
		userService.save(user);
		return "redirect:/admin/users";
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable Long id, Authentication authentication) {
		String currentUsername = authentication.getName();
		Optional<User> user = userService.findById(id);
		
		if (user.isPresent() && !user.get().getUsername().equals(currentUsername)) {
			userService.deleteById(id);
		}
		return "redirect:/admin/users";
	}
	
	@GetMapping("/tests")
	public String manageTests() {
		return "admin/tests";
	}
}