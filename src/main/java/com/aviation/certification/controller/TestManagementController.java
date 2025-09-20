// TestManagementController.java
package com.aviation.certification.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.aviation.certification.model.*;
import com.aviation.certification.service.TestService;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/tests")
@PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
public class TestManagementController {
	
	private final TestService testService;
	
	public TestManagementController(TestService testService) {
		this.testService = testService;
	}
	
	@GetMapping("/manage")
	public String manageTests(Model model) {
		model.addAttribute("tests", testService.getAllTests());
		return "admin/test-management";
	}
	
	@GetMapping("/create")
	public String showCreateTestForm(Model model) {
		model.addAttribute("exam", new Exam());
		model.addAttribute("specializations", testService.getAllSpecializations());
		return "admin/test-create";
	}
	
	@PostMapping("/create")
	public String createTest(@ModelAttribute Exam exam,
			@RequestParam(value = "specializations", required = false) List<Long> specializationIds,
			Authentication authentication, Model model) {
		try {
			// Проверка на выбор специализаций
			if (specializationIds == null || specializationIds.isEmpty()) {
				model.addAttribute("error", "Необходимо выбрать хотя бы одну специализацию");
				model.addAttribute("exam", exam);
				model.addAttribute("specializations", testService.getAllSpecializations());
				return "admin/test-create";
			}
			
			User currentUser = (User) authentication.getPrincipal();
			exam.setCreatedBy(currentUser);
			
			// Очищаем существующие специализации и добавляем новые
			exam.getSpecializations().clear();
			for (Long specId : specializationIds) {
				Specialization spec = testService.getAllSpecializations().stream()
						.filter(s -> s.getId().equals(specId))
						.findFirst()
						.orElseThrow(() -> new RuntimeException("Specialization not found"));
				exam.addSpecialization(spec);
			}
			
			testService.saveExam(exam);
			return "redirect:/admin/tests/manage?success";
		} catch (Exception e) {
			model.addAttribute("error", "Ошибка при создании теста: " + e.getMessage());
			model.addAttribute("exam", exam);
			model.addAttribute("specializations", testService.getAllSpecializations());
			return "admin/test-create";
		}
	}
	
	@GetMapping("/view/{id}")
	public String viewTest(@PathVariable Long id, Model model) {
		Optional<Exam> exam = testService.getExamById(id);
		if (exam.isPresent()) {
			model.addAttribute("exam", exam.get());
			return "admin/test-view";
		}
		return "redirect:/admin/tests/manage";
	}
	
	@GetMapping("/edit/{id}")
	public String editTest(@PathVariable Long id, Model model) {
		Optional<Exam> exam = testService.getExamById(id);
		if (exam.isPresent()) {
			model.addAttribute("exam", exam.get());
			model.addAttribute("specializations", testService.getAllSpecializations());
			return "admin/test-edit";
		}
		return "redirect:/admin/tests/manage";
	}
	
	@PostMapping("/edit/{id}")
	public String updateTest(@PathVariable Long id, @ModelAttribute Exam exam,
			@RequestParam("specializations") List<Long> specializationIds) {
		exam.setId(id);
		
		exam.getSpecializations().clear();
		for (Long specId : specializationIds) {
			Specialization spec = testService.getAllSpecializations().stream()
					.filter(s -> s.getId().equals(specId))
					.findFirst()
					.orElseThrow(() -> new RuntimeException("Specialization not found"));
			exam.addSpecialization(spec);
		}
		
		testService.saveExam(exam);
		return "redirect:/admin/tests/manage";
	}
	
	@GetMapping("/questions/{testId}")
	public String manageQuestions(@PathVariable Long testId, Model model) {
		Optional<Exam> exam = testService.getExamById(testId);
		if (exam.isPresent()) {
			model.addAttribute("exam", exam.get());
			model.addAttribute("questions", testService.getQuestionsByExamId(testId));
			model.addAttribute("question", new Question());
			return "admin/question-management";
		}
		return "redirect:/admin/tests/manage";
	}
	
	@PostMapping("/questions/{testId}/add")
	public String addQuestion(@PathVariable Long testId, @ModelAttribute Question question,
			@RequestParam("answers") List<String> answerTexts,
			@RequestParam(value = "correctAnswers", required = false) List<Integer> correctAnswerIndices,
			@RequestParam(value = "multipleCorrect", defaultValue = "false") Boolean multipleCorrect) {
		Optional<Exam> exam = testService.getExamById(testId);
		if (exam.isPresent()) {
			question.setExam(exam.get());
			question.setQuestionType(multipleCorrect ? "MULTIPLE_CHOICE" : "SINGLE_CHOICE");
			
			Question savedQuestion = testService.saveQuestion(question);
			
			// Добавьте ответы
			for (int i = 0; i < answerTexts.size(); i++) {
				if (answerTexts.get(i) != null && !answerTexts.get(i).trim().isEmpty()) {
					Answer answer = new Answer();
					answer.setQuestion(savedQuestion);
					answer.setText(answerTexts.get(i));
					answer.setIsCorrect(correctAnswerIndices != null && correctAnswerIndices.contains(i));
					testService.saveAnswer(answer);
				}
			}
			
			return "redirect:/admin/tests/questions/" + testId;
		}
		return "redirect:/admin/tests/manage";
	}
	
	@GetMapping("/take/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'CANDIDATE')")
	public String takeTest(@PathVariable Long id, Model model) {
		Optional<Exam> exam = testService.getExamById(id);
		if (exam.isPresent()) {
			model.addAttribute("exam", exam.get());
			return "test-taking";
		}
		return "redirect:/dashboard";
	}
	
	@PostMapping("/submit/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'CANDIDATE')")
	public String submitTest(@PathVariable Long id,
			@RequestParam("answers") List<Long> answerIds,
			Authentication authentication) {
		Optional<Exam> exam = testService.getExamById(id);
		if (exam.isPresent()) {
			User user = (User) authentication.getPrincipal();
			
			// Логика проверки ответов и расчета результата
			int score = calculateScore(answerIds);
			int maxScore = exam.get().getQuestions().size();
			
			TestResult testResult = new TestResult(user, exam.get(), score, maxScore);
			testResult.completeTest();
			testService.saveTestResult(testResult);
			
			return "redirect:/test-results/" + testResult.getId();
		}
		return "redirect:/dashboard";
	}
	
	private int calculateScore(List<Long> answerIds) {
		// Логика расчета баллов на основе правильных ответов
		int score = 0;
		for (Long answerId : answerIds) {
			Optional<Answer> answer = testService.getAnswerById(answerId);
			if (answer.isPresent() && answer.get().getIsCorrect()) {
				score++;
			}
		}
		return score;
	}
	
	@GetMapping("/delete/{id}")
	public String deleteTest(@PathVariable Long id) {
		testService.deleteExam(id);
		return "redirect:/admin/tests/manage";
	}
	
	@GetMapping("/questions/delete/{questionId}")
	public String deleteQuestion(@PathVariable Long questionId, @RequestParam Long testId) {
		testService.deleteQuestion(questionId);
		return "redirect:/admin/tests/questions/" + testId;
	}
}