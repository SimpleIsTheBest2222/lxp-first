package com.potenup.lxp.instructor.service;

import com.potenup.lxp.instructor.domain.Instructor;
import com.potenup.lxp.instructor.repository.InstructorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// InstructorService 테스트에서 DB 대신 메모리 목록으로 동작하는 가짜 저장소다.
class FakeInstructorRepository implements InstructorRepository {
	private final List<Instructor> instructors = new ArrayList<>();

	FakeInstructorRepository() {
		instructors.add(new Instructor(1L, "Kim", "Java and DB instructor"));
		instructors.add(new Instructor(2L, "Lee", "Spring instructor"));
	}

	@Override
	public Long save(Instructor instructor) {
		return 1L;
	}

	@Override
	public boolean existsById(Long instructorId) {
		return instructors.stream()
				.anyMatch(instructor -> instructor.getId().equals(instructorId));
	}

	@Override
	public List<Instructor> findAll() {
		return List.copyOf(instructors);
	}

	@Override
	public Optional<Instructor> findById(Long instructorId) {
		return instructors.stream()
				.filter(instructor -> instructor.getId().equals(instructorId))
				.findFirst();
	}

	@Override
	public void update(Instructor instructor) {
		// 수정 테스트를 위해 같은 id의 강사를 새 객체로 교체한다.
		for (int index = 0; index < instructors.size(); index++) {
			if (instructors.get(index).getId().equals(instructor.getId())) {
				instructors.set(index, instructor);
				return;
			}
		}
	}

	@Override
	public void deleteById(Long instructorId) {
		instructors.removeIf(instructor -> instructor.getId().equals(instructorId));
	}
}
