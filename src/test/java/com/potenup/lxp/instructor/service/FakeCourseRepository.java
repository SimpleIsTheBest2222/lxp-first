package com.potenup.lxp.instructor.service;

import com.potenup.lxp.course.domain.Course;
import com.potenup.lxp.course.repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// InstructorService 테스트에서 강의 연결 여부만 제어하기 위한 가짜 저장소다.
class FakeCourseRepository implements CourseRepository {
	private final List<Long> assignedInstructorIds = new ArrayList<>();

	@Override
	public Long save(Course course) {
		return 1L;
	}

	@Override
	public List<Course> findAll() {
		return List.of();
	}

	@Override
	public Optional<Course> findById(Long courseId) {
		return Optional.empty();
	}

	@Override
	public void update(Course course) {
	}

	@Override
	public void deleteById(Long courseId) {
	}

	@Override
	public boolean existsById(Long courseId) {
		return courseId != null && courseId > 0;
	}

	@Override
	public boolean existsByInstructorId(Long instructorId) {
		return assignedInstructorIds.contains(instructorId);
	}

	// 특정 강사에게 연결된 강의가 있다고 가정하는 테스트 데이터를 만든다.
	void assignInstructor(Long instructorId) {
		assignedInstructorIds.add(instructorId);
	}
}
