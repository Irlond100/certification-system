package com.aviation.certification.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.aviation.certification.service.UserService;
import org.springframework.security.core.Authentication;
import com.aviation.certification.model.User;
import com.aviation.certification.model.Role;
import com.aviation.certification.repository.RoleRepository;

import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	private final UserService userService;
	private final RoleRepository roleRepository;
	
	public AdminController(UserService userService, RoleRepository roleRepository) {
		this.userService = userService;
		this.roleRepository = roleRepository;
	}
	
	@GetMapping("/dashboard")
	public String adminDashboard() {
		return "admin/dashboard";
	}
	
	@GetMapping("/users")
	public String listUsers(Model model, Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();
		model.addAttribute("currentUsername", currentUser.getUsername());
		model.addAttribute("users", userService.findAll());
		return "admin/users";
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUserForm(@PathVariable Long id, Model model, Authentication authentication) {
		Optional<User> user = userService.findById(id);
		if (user.isPresent()) {
			// Получаем все роли, кроме ADMIN
			List<Role> availableRoles = roleRepository.findAll().stream()
					.filter(role -> !role.getName().equals("ROLE_ADMIN"))
					.collect(Collectors.toList());
			
			model.addAttribute("user", user.get());
			model.addAttribute("availableRoles", availableRoles);
			return "admin/user-edit";
		}
		return "redirect:/admin/users";
	}
	
	@PostMapping("/users/edit/{id}")
	public String updateUser(@PathVariable Long id,
			@RequestParam String username,
			@RequestParam String email,
			@RequestParam String firstName,
			@RequestParam String lastName,
			@RequestParam(value = "roles", required = false) List<Long> roleIds,
			Authentication authentication) {
		
		Optional<User> userOpt = userService.findById(id);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.setUsername(username);
			user.setEmail(email);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			
			// Обновляем роли (только не-ADMIN роли)
			if (roleIds != null) {
				Set<Role> newRoles = new HashSet<>();
				for (Long roleId : roleIds) {
					roleRepository.findById(roleId).ifPresent(role -> {
						if (!role.getName().equals("ROLE_ADMIN")) {
							newRoles.add(role);
						}
					});
				}
				user.setRoles(newRoles);
			}
			
			userService.save(user);
		}
		return "redirect:/admin/users";
	}
}