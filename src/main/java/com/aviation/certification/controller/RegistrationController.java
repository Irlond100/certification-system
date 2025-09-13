package com.aviation.certification.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.aviation.certification.model.Role;
import com.aviation.certification.model.Specialization;
import com.aviation.certification.model.User;
import com.aviation.certification.repository.RoleRepository;
import com.aviation.certification.repository.SpecializationRepository;
import com.aviation.certification.repository.UserRepository;

@Controller
public class RegistrationController {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final SpecializationRepository specializationRepository;
	private final PasswordEncoder passwordEncoder;
	
	public RegistrationController(UserRepository userRepository, RoleRepository roleRepository,
			SpecializationRepository specializationRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.specializationRepository = specializationRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("specializations", specializationRepository.findAll());
		return "register";
	}
	
	@PostMapping("/register")
	public String registerUser(
			@RequestParam String username,
			@RequestParam String password,
			@RequestParam String email,
			@RequestParam String firstName,
			@RequestParam String lastName,
			@RequestParam String specializationCode,
			Model model) {
		
		if (userRepository.existsByUsername(username)) {
			model.addAttribute("error", "Username already exists");
			return "register";
		}
		
		if (userRepository.existsByEmail(email)) {
			model.addAttribute("error", "Email already exists");
			return "register";
		}
		
		// Создание нового пользователя
		User user = new User(username, passwordEncoder.encode(password), email, firstName, lastName);
		
		// Назначение роли кандидата
		Role candidateRole = roleRepository.findByName("ROLE_CANDIDATE")
				.orElseThrow(() -> new RuntimeException("Error: Role not found."));
		user.getRoles().add(candidateRole);
		
		// Добавление специализации
		Specialization specialization = specializationRepository.findByCode(specializationCode)
				.orElseThrow(() -> new RuntimeException("Error: Specialization not found."));
		user.getSpecializations().add(specialization);
		
		userRepository.save(user);
		
		return "redirect:/login?registered";
	}
}