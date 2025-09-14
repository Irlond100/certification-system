package com.aviation.certification.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.aviation.certification.model.Exam;
import com.aviation.certification.model.Specialization;
import com.aviation.certification.model.User;
import com.aviation.certification.service.TestService;
import com.aviation.certification.service.UserService;

@Controller
@RequestMapping("/candidate")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateController { // Добавляем public
	private final TestService testService;
	private final UserService userService;
	
	public CandidateController(TestService testService, UserService userService) {
		this.testService = testService;
		this.userService = userService;
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		User user = (User) authentication.getPrincipal();
		if (!user.getSpecializations().isEmpty()) {
			// Упрощенная логика, нужно улучшить для поддержки нескольких специализаций
			Specialization specialization = user.getSpecializations().iterator().next();
			List<Exam> availableExams = testService.getExamsBySpecialization(specialization);
			model.addAttribute("availableExams", availableExams);
		}
		model.addAttribute("user", user);
		model.addAttribute("testHistory", testService.getUserTestResults(user));
		return "candidate/dashboard";
	}
	
	@GetMapping("/exam/{examId}")
	public String takeExam(@PathVariable Long examId, Model model, Authentication authentication) {
		// Логика проверки доступа и отображения экзамена
		return "candidate/exam";
	}
}