package com.aviation.certification.service;

import com.aviation.certification.model.*;
import com.aviation.certification.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class TestService {
	private final ExamRepository examRepository;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final TestResultRepository testResultRepository;
	private final SpecializationRepository specializationRepository;
	
	public TestService(ExamRepository examRepository,
			QuestionRepository questionRepository,
			AnswerRepository answerRepository,
			TestResultRepository testResultRepository,
			SpecializationRepository specializationRepository) {
		this.examRepository = examRepository;
		this.questionRepository = questionRepository;
		this.answerRepository = answerRepository;
		this.testResultRepository = testResultRepository;
		this.specializationRepository = specializationRepository;
	}
	
	public Optional<Exam> getExamById(Long id) {
		return examRepository.findByIdWithSpecializations(id);
	}
	
	public List<Exam> getAllTests() {
		return examRepository.findAllWithAssociations();
	}
	
	public Optional<Question> getQuestionById(Long id) {
		return questionRepository.findById(id);
	}
	
	public List<Answer> getAnswersByQuestionId(Long questionId) {
		return answerRepository.findByQuestionId(questionId);
	}
	
	public List<Exam> getExamsBySpecialization(Specialization specialization) {
		return examRepository.findBySpecializationAndIsVisible(specialization);
	}
	
	@Transactional(readOnly = true)
	public List<Question> getQuestionsByExamId(Long examId) {
		return questionRepository.findByExamId(examId);
	}
	
	public Exam saveExam(Exam exam) {
		return examRepository.save(exam);
	}
	
	@Transactional
	public void deleteExam(Long id) {
		Optional<Exam> exam = examRepository.findById(id);
		if (exam.isPresent()) {
			// Удаляем все вопросы и ответы связанные с тестом
			for (Question question : exam.get().getQuestions()) {
				answerRepository.deleteByQuestionId(question.getId());
				questionRepository.delete(question);
			}
			examRepository.deleteById(id);
		}
	}
	
	public Question saveQuestion(Question question) {
		return questionRepository.save(question);
	}
	
	@Transactional
	public void deleteQuestion(Long id) {
		// Сначала удаляем все ответы вопроса
		answerRepository.deleteByQuestionId(id);
		// Затем удаляем сам вопрос
		questionRepository.deleteById(id);
	}
	
	public Answer saveAnswer(Answer answer) {
		return answerRepository.save(answer);
	}
	
	public Optional<Answer> getAnswerById(Long id) {
		return answerRepository.findById(id);
	}
	
	public void deleteAnswersByQuestionId(Long questionId) {
		answerRepository.deleteByQuestionId(questionId);
	}
	
	public List<TestResult> getUserTestResults(User user) {
		return testResultRepository.findByUserOrderByCompletedAtDesc(user);
	}
	
	public TestResult saveTestResult(TestResult testResult) {
		return testResultRepository.save(testResult);
	}
	
	public List<Specialization> getAllSpecializations() {
		return specializationRepository.findAll();
	}
}