package com.aviation.certification.repository;

import com.aviation.certification.model.Exam;
import com.aviation.certification.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
	List<Exam> findByIsVisible(Boolean isVisible);
	
	@Query("SELECT e FROM Exam e JOIN e.specializations s WHERE s = :specialization AND e.isVisible = true")
	List<Exam> findBySpecializationAndIsVisible(@Param("specialization") Specialization specialization);
	
	@Query("SELECT e FROM Exam e JOIN e.specializations s WHERE s.code = :specializationCode AND e.isVisible = true")
	List<Exam> findBySpecializationCodeAndIsVisible(@Param("specializationCode") String specializationCode);
}