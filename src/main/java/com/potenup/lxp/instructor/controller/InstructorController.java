package com.potenup.lxp.instructor.controller;

import com.potenup.lxp.instructor.domain.Instructor;
import com.potenup.lxp.instructor.dto.InstructorCreateRequest;
import com.potenup.lxp.instructor.dto.InstructorUpdateRequest;
import com.potenup.lxp.instructor.service.InstructorService;

import java.util.List;

public class InstructorController {
	private final InstructorService instructorService;

	public InstructorController(InstructorService instructorService) {
		this.instructorService = instructorService;
	}

	public Long createInstructor(InstructorCreateRequest request) {
		return instructorService.createInstructor(request);
	}

	public List<Instructor> getInstructors() {
		return instructorService.getInstructors();
	}

	public Instructor getInstructor(Long instructorId) {
		return instructorService.getInstructor(instructorId);
	}

	public Instructor updateInstructor(Long instructorId, InstructorUpdateRequest request) {
		return instructorService.updateInstructor(instructorId, request);
	}

	public void deleteInstructor(Long instructorId) {
		instructorService.deleteInstructor(instructorId);
	}
}
