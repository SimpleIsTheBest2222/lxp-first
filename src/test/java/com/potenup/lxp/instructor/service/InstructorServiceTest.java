package com.potenup.lxp.instructor.service;

import com.potenup.lxp.instructor.dto.InstructorCreateRequest;
import com.potenup.lxp.instructor.repository.InstructorRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InstructorServiceTest {
	@Test
	void createInstructorReturnsSavedId() {
		InstructorService instructorService = new InstructorService(new InstructorRepository());

		Long instructorId = instructorService.createInstructor(
			new InstructorCreateRequest("Kim", "Java and DB instructor")
		);

		assertEquals(1L, instructorId);
	}
}
