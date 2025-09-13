package com.aviation.certification.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "questions")
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "test_id")
	private Exam exam;
	
	private String text;
	
	@Column(name = "question_type")
	private String questionType;
	
	@Column(name = "display_order")
	private Integer displayOrder;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Answer> answers = new HashSet<>();
	
	// Конструкторы
	public Question() {
		this.questionType = "SINGLE_CHOICE";
	}
	
	public Question(Exam exam, String text, Integer displayOrder) {
		this();
		this.exam = exam;
		this.text = text;
		this.displayOrder = displayOrder;
	}
	
	// Геттеры и сеттеры
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public Exam getExam() { return exam; }
	public void setExam(Exam exam) { this.exam = exam; }
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	public String getQuestionType() { return questionType; }
	public void setQuestionType(String questionType) { this.questionType = questionType; }
	
	public Integer getDisplayOrder() { return displayOrder; }
	public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
	
	public Set<Answer> getAnswers() { return answers; }
	public void setAnswers(Set<Answer> answers) { this.answers = answers; }
	
	// Метод для добавления ответа
	public void addAnswer(Answer answer) {
		this.answers.add(answer);
		answer.setQuestion(this);
	}
}