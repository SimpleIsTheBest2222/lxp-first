package com.potenup.lxp.instructor.controller;

import com.potenup.lxp.instructor.dto.InstructorCreateRequest;
import com.potenup.lxp.instructor.service.InstructorService;

public class InstructorController {
	private final InstructorService instructorService;

	public InstructorController(InstructorService instructorService) {
		this.instructorService = instructorService;
	}

	public Long createInstructor(InstructorCreateRequest request) {
		return instructorService.createInstructor(request);
	}
}
