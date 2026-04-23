package com.potenup.lxp.instructor.service;

import com.potenup.lxp.course.repository.CourseRepository;
import com.potenup.lxp.instructor.domain.Instructor;
import com.potenup.lxp.instructor.dto.InstructorCreateRequest;
import com.potenup.lxp.instructor.dto.InstructorUpdateRequest;
import com.potenup.lxp.instructor.repository.InstructorRepository;

import java.util.List;

// 강사 관련 비즈니스 규칙을 처리하는 서비스다.
public class InstructorService {
	private final InstructorRepository instructorRepository;
	private final CourseRepository courseRepository;

	public InstructorService(InstructorRepository instructorRepository, CourseRepository courseRepository) {
		this.instructorRepository = instructorRepository;
		this.courseRepository = courseRepository;
	}

	public Long createInstructor(InstructorCreateRequest request) {
		// 도메인 객체 생성 과정에서 이름과 소개에 대한 검증이 수행된다.
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
		// 수정은 기존 데이터를 조회한 뒤, 비어 있는 입력만 기존 값으로 보존한다.
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
		// 삭제 전에 존재 여부와 연결된 강의 존재 여부를 순서대로 확인한다.
		if (!instructorRepository.existsById(instructorId)) {
			throw new IllegalArgumentException("강사를 찾을 수 없습니다.");
		}
		if (courseRepository.existsByInstructorId(instructorId)) {
			throw new IllegalStateException("연결된 코스가 있어 강사를 삭제할 수 없습니다.");
		}
		instructorRepository.deleteById(instructorId);
	}

	private boolean isBlank(String value) {
		// 수정 요청에서는 null과 공백을 동일하게 취급해 "미입력"으로 본다.
		return value == null || value.isBlank();
	}
}
