package com.potenup.lxp.instructor.service;

import com.potenup.lxp.instructor.domain.Instructor;
import com.potenup.lxp.instructor.dto.InstructorCreateRequest;
import com.potenup.lxp.instructor.repository.InstructorRepository;

public class InstructorService {
	private final InstructorRepository instructorRepository;

	public InstructorService(InstructorRepository instructorRepository) {
		this.instructorRepository = instructorRepository;
	}

	public Long createInstructor(InstructorCreateRequest request) {
		Instructor instructor = new Instructor(request.getName(), request.getIntroduction());
		return instructorRepository.save(instructor);
	}
}
