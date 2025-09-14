package com.aviation.certification.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.aviation.certification.model.*;
import com.aviation.certification.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
class RegistrationController {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final SpecializationRepository specializationRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	
	public RegistrationController(UserRepository u, RoleRepository r, SpecializationRepository s,
			PasswordEncoder p, AuthenticationManager a) {
		this.userRepository = u;
		this.roleRepository = r;
		this.specializationRepository = s;
		this.passwordEncoder = p;
		this.authenticationManager = a;
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("specializations", specializationRepository.findAll());
		return "register";
	}
	
	@PostMapping("/register")
	public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String email,
			@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String specializationCode, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		if (userRepository.existsByUsername(username)) {
			model.addAttribute("error", "Пользователь с таким логином уже существует");
			model.addAttribute("specializations", specializationRepository.findAll());
			return "register";
		}
		if (userRepository.existsByEmail(email)) {
			model.addAttribute("error", "Пользователь с таким email уже существует");
			model.addAttribute("specializations", specializationRepository.findAll());
			return "register";
		}
		
		User user = new User(username, passwordEncoder.encode(password), email, firstName, lastName);
		Role candidateRole = roleRepository.findByName("ROLE_CANDIDATE")
				.orElseThrow(() -> new RuntimeException("Role not found"));
		user.getRoles().add(candidateRole);
		Specialization spec = specializationRepository.findByCode(specializationCode)
				.orElseThrow(() -> new RuntimeException("Specialization not found"));
		user.getSpecializations().add(spec);
		userRepository.save(user);
		
		// Auto-login
		try {
			UsernamePasswordAuthenticationToken authRequest =
					new UsernamePasswordAuthenticationToken(username, password);
			Authentication authentication = authenticationManager.authenticate(authRequest);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			return "redirect:/candidate/dashboard";
		} catch (Exception e) {
			return "redirect:/login";
		}
	}
}