package com.aviation.certification.repository;

import com.aviation.certification.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	List<Question> findByExamId(Long examId);
	
	@Query("SELECT q FROM Question q LEFT JOIN FETCH q.answers WHERE q.id = :id")
	Optional<Question> findByIdWithAnswers(@Param("id") Long id);
}
