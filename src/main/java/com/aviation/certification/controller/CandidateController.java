package com.aviation.certification.controller;

import com.aviation.certification.model.Exam;
import com.aviation.certification.model.Specialization;
import com.aviation.certification.model.TestResult;
import com.aviation.certification.model.User;
import com.aviation.certification.service.TestService;
import com.aviation.certification.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/candidate")
public class CandidateController {
	private final TestService testService;
	private final UserService userService;
	
	public CandidateController(TestService testService, UserService userService) {
		this.testService = testService;
		this.userService = userService;
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		String username = authentication.getName();
		Optional<User> userOptional = userService.findByUsername(username);
		
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			
			// Получаем первую специализацию пользователя
			Specialization specialization = user.getSpecializations().iterator().next();
			
			// Получаем доступные экзамены
			List<Exam> availableExams = testService.getExamsBySpecialization(specialization);
			
			// Получаем историю тестирований
			List<TestResult> testHistory = testService.getUserTestResults(user);
			
			model.addAttribute("user", user);
			model.addAttribute("availableExams", availableExams);
			model.addAttribute("testHistory", testHistory);
		}
		
		return "candidate/dashboard";
	}
	
	@GetMapping("/exam/{examId}")
	public String takeExam(@PathVariable Long examId, Model model, Authentication authentication) {
		String username = authentication.getName();
		Optional<User> userOptional = userService.findByUsername(username);
		Optional<Exam> examOptional = testService.getExamById(examId);
		
		if (userOptional.isPresent() && examOptional.isPresent()) {
			User user = userOptional.get();
			Exam exam = examOptional.get();
			
			// Проверяем, имеет ли пользователь доступ к этому экзамену
			boolean hasAccess = user.getSpecializations().stream()
					.anyMatch(s -> exam.getSpecializations().contains(s));
			
			if (hasAccess) {
				model.addAttribute("exam", exam);
				return "candidate/exam";
			}
		}
		
		return "redirect:/candidate/dashboard?error=access_denied";
	}
}