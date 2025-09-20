package com.aviation.certification.repository;

import com.aviation.certification.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	List<Question> findByExamId(Long examId);
}
