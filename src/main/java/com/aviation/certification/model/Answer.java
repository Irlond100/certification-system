package com.aviation.certification.model;

import jakarta.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "question_id")
	private Question question;
	
	private String text;
	
	@Column(name = "is_correct")
	private Boolean isCorrect;
	
	// Конструкторы
	public Answer() {
		this.isCorrect = false;
	}
	
	public Answer(Question question, String text, Boolean isCorrect) {
		this();
		this.question = question;
		this.text = text;
		this.isCorrect = isCorrect;
	}
	
	// Геттеры и сеттеры
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public Question getQuestion() { return question; }
	public void setQuestion(Question question) { this.question = question; }
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	public Boolean getIsCorrect() { return isCorrect; }
	public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
}