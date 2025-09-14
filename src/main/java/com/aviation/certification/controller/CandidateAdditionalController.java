package com.aviation.certification.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/candidate")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateAdditionalController {
	
	@GetMapping("/results")
	public String candidateResults() {
		return "candidate/results";
	}
	
	@GetMapping("/materials")
	public String candidateMaterials() {
		return "candidate/materials";
	}
}