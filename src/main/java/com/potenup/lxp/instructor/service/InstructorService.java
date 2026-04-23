package com.potenup.lxp.instructor.service;

import com.potenup.lxp.course.repository.CourseRepository;
import com.potenup.lxp.instructor.domain.Instructor;
import com.potenup.lxp.instructor.dto.InstructorCreateRequest;
import com.potenup.lxp.instructor.dto.InstructorUpdateRequest;
import com.potenup.lxp.instructor.repository.InstructorRepository;

import java.util.List;

public class InstructorService {
	private final InstructorRepository instructorRepository;
	private final CourseRepository courseRepository;

	public InstructorService(InstructorRepository instructorRepository, CourseRepository courseRepository) {
		this.instructorRepository = instructorRepository;
		this.courseRepository = courseRepository;
	}

	public Long createInstructor(InstructorCreateRequest request) {
		Instructor instructor = new Instructor(request.getName(), request.getIntroduction());
		return instructorRepository.save(instructor);
	}

	public List<Instructor> getInstructors() {
		return instructorRepository.findAll();
	}

	public Instructor getInstructor(Long instructorId) {
		return instructorRepository.findById(instructorId)
				.orElseThrow(() -> new IllegalArgumentException("강사를 찾을 수 없습니다."));
	}

	public Instructor updateInstructor(Long instructorId, InstructorUpdateRequest request) {
		Instructor currentInstructor = getInstructor(instructorId);

		String updatedName = isBlank(request.getName()) ? currentInstructor.getName() : request.getName();
		String updatedIntroduction = isBlank(request.getIntroduction())
				? currentInstructor.getIntroduction()
				: request.getIntroduction();

		Instructor updatedInstructor = new Instructor(
				currentInstructor.getId(),
				updatedName,
				updatedIntroduction
		);
		instructorRepository.update(updatedInstructor);
		return updatedInstructor;
	}

	public void deleteInstructor(Long instructorId) {
		if (!instructorRepository.existsById(instructorId)) {
			throw new IllegalArgumentException("강사를 찾을 수 없습니다.");
		}
		if (courseRepository.existsByInstructorId(instructorId)) {
			throw new IllegalStateException("연결된 코스가 있어 강사를 삭제할 수 없습니다.");
		}
		instructorRepository.deleteById(instructorId);
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}
}
