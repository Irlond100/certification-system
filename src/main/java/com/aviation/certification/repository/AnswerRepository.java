package com.aviation.certification.repository;

import com.aviation.certification.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
	List<Answer> findByQuestionId(Long questionId);
	void deleteByQuestionId(Long questionId);
	
	@Query("SELECT a FROM Answer a WHERE a.question.id = :questionId")
	List<Answer> findByQuestionIdWithDetails(@Param("questionId") Long questionId);
}
