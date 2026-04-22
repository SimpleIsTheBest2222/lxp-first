package com.potenup.lxp.instructor.service;

import com.potenup.lxp.course.domain.Course;
import com.potenup.lxp.course.repository.CourseRepository;
import com.potenup.lxp.instructor.domain.Instructor;
import com.potenup.lxp.instructor.dto.InstructorCreateRequest;
import com.potenup.lxp.instructor.dto.InstructorUpdateRequest;
import com.potenup.lxp.instructor.repository.InstructorRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InstructorServiceTest {
	@Test
	void createInstructorReturnsSavedId() {
		InstructorService instructorService = new InstructorService(
				new FakeInstructorRepository(),
				new FakeCourseRepository()
		);

		Long instructorId = instructorService.createInstructor(
			new InstructorCreateRequest("Kim", "Java and DB instructor")
		);

		assertEquals(1L, instructorId);
	}

	@Test
	void getInstructorsReturnsAllInstructors() {
		InstructorService instructorService = new InstructorService(
				new FakeInstructorRepository(),
				new FakeCourseRepository()
		);

		List<Instructor> instructors = instructorService.getInstructors();

		assertEquals(2, instructors.size());
		assertEquals("Kim", instructors.get(0).getName());
	}

	@Test
	void updateInstructorKeepsExistingValuesWhenBlankIsProvided() {
		InstructorService instructorService = new InstructorService(
				new FakeInstructorRepository(),
				new FakeCourseRepository()
		);

		Instructor updatedInstructor = instructorService.updateInstructor(1L, new InstructorUpdateRequest("", ""));

		assertEquals("Kim", updatedInstructor.getName());
		assertEquals("Java and DB instructor", updatedInstructor.getIntroduction());
	}

	@Test
	void deleteInstructorRemovesInstructor() {
		FakeInstructorRepository repository = new FakeInstructorRepository();
		InstructorService instructorService = new InstructorService(repository, new FakeCourseRepository());

		instructorService.deleteInstructor(1L);

		assertFalse(repository.existsById(1L));
	}

	@Test
	void deleteInstructorFailsWhenAssignedCourseExists() {
		FakeInstructorRepository instructorRepository = new FakeInstructorRepository();
		FakeCourseRepository courseRepository = new FakeCourseRepository();
		courseRepository.assignInstructor(1L);
		InstructorService instructorService = new InstructorService(instructorRepository, courseRepository);

		IllegalStateException exception = assertThrows(
				IllegalStateException.class,
				() -> instructorService.deleteInstructor(1L)
		);

		assertEquals("연결된 코스가 있어 강사를 삭제할 수 없습니다.", exception.getMessage());
		assertFalse(instructorRepository.findById(1L).isEmpty());
	}

	private static class FakeInstructorRepository implements InstructorRepository {
		private final List<Instructor> instructors = new ArrayList<>();

		private FakeInstructorRepository() {
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

	private static class FakeCourseRepository implements CourseRepository {
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

		private void assignInstructor(Long instructorId) {
			assignedInstructorIds.add(instructorId);
		}
	}
}
