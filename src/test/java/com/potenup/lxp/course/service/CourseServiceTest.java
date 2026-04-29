package com.potenup.lxp.course.service;

import com.potenup.lxp.common.exception.NotFoundException;
import com.potenup.lxp.common.exception.ValidationException;
import com.potenup.lxp.course.domain.Course;
import com.potenup.lxp.course.domain.CourseLevel;
import com.potenup.lxp.course.dto.CourseCreateRequest;
import com.potenup.lxp.course.dto.CourseUpdateRequest;
import com.potenup.lxp.course.repository.CourseRepository;
import com.potenup.lxp.instructor.domain.Instructor;
import com.potenup.lxp.instructor.repository.InstructorRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseServiceTest {
    @Test
    void createCourseReturnsSavedId() {
        CourseService courseService = new CourseService(new FakeCourseRepository(), new FakeInstructorRepository());

        Long courseId = courseService.createCourse(
            new CourseCreateRequest("JDBC Basics", "Intro course", 30000, CourseLevel.LOW, 1L)
        );

        assertEquals(1L, courseId);
    }

    @Test
    void createCourseThrowsValidationExceptionWhenInstructorDoesNotExist() {
        CourseService courseService = new CourseService(new FakeCourseRepository(), new FakeInstructorRepository());

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> courseService.createCourse(
                new CourseCreateRequest("JDBC Basics", "Intro course", 30000, CourseLevel.LOW, 99L)
            )
        );

        assertTrue(exception.getMessage() != null && !exception.getMessage().isBlank());
    }

    @Test
    void getCoursesReturnsAllCourses() {
        CourseService courseService = new CourseService(new FakeCourseRepository(), new FakeInstructorRepository());

        List<Course> courses = courseService.getCourses();

        assertEquals(2, courses.size());
        assertEquals("JDBC Basics", courses.get(0).getTitle());
    }

    @Test
    void getCourseThrowsNotFoundExceptionWhenCourseDoesNotExist() {
        CourseService courseService = new CourseService(new FakeCourseRepository(), new FakeInstructorRepository());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> courseService.getCourse(99L)
        );

        assertTrue(exception.getMessage() != null && !exception.getMessage().isBlank());
    }

    @Test
    void updateCourseKeepsExistingValuesWhenBlankIsProvided() {
        CourseService courseService = new CourseService(new FakeCourseRepository(), new FakeInstructorRepository());

        Course updatedCourse = courseService.updateCourse(
            1L,
            new CourseUpdateRequest("", "", null, null, null)
        );

        assertEquals("JDBC Basics", updatedCourse.getTitle());
        assertEquals("Intro course", updatedCourse.getDescription());
        assertEquals(30000, updatedCourse.getPrice());
        assertEquals(CourseLevel.LOW, updatedCourse.getLevel());
    }

    @Test
    void deleteCourseRemovesCourse() {
        FakeCourseRepository repository = new FakeCourseRepository();
        CourseService courseService = new CourseService(repository, new FakeInstructorRepository());

        courseService.deleteCourse(1L);

        assertFalse(repository.existsById(1L));
    }

    private static class FakeCourseRepository implements CourseRepository {
        private final List<Course> courses = new ArrayList<>();

        private FakeCourseRepository() {
            courses.add(new Course(1L, "JDBC Basics", "Intro course", 30000, CourseLevel.LOW, 1L));
            courses.add(new Course(2L, "Spring Basics", "Web intro", 50000, CourseLevel.MEDIUM, 1L));
        }

        @Override
        public Long save(Course course) {
            return 1L;
        }

        @Override
        public List<Course> findAll() {
            return List.copyOf(courses);
        }

        @Override
        public Optional<Course> findById(Long courseId) {
            return courses.stream()
                .filter(course -> course.getId().equals(courseId))
                .findFirst();
        }

        @Override
        public void update(Course course) {
            for (int index = 0; index < courses.size(); index++) {
                if (courses.get(index).getId().equals(course.getId())) {
                    courses.set(index, course);
                    return;
                }
            }
        }

        @Override
        public boolean deleteById(Long courseId) {
            return courses.removeIf(course -> course.getId().equals(courseId));
        }

        @Override
        public boolean existsById(Long courseId) {
            return courses.stream()
                .anyMatch(course -> course.getId().equals(courseId));
        }

        @Override
        public boolean existsByInstructorId(Long instructorId) {
            return courses.stream()
                .anyMatch(course -> course.getInstructorId().equals(instructorId));
        }
    }

    private static class FakeInstructorRepository implements InstructorRepository {
        @Override
        public Long save(Instructor instructor) {
            return 1L;
        }

        @Override
        public List<Instructor> findAll() {
            return List.of(new Instructor(1L, "Kim", "Java instructor"));
        }

        @Override
        public Optional<Instructor> findById(Long instructorId) {
            if (instructorId != null && instructorId == 1L) {
                return Optional.of(new Instructor(1L, "Kim", "Java instructor"));
            }
            return Optional.empty();
        }

        @Override
        public void update(Instructor instructor) {
        }

        @Override
        public void deleteById(Long instructorId) {
        }

        @Override
        public boolean existsById(Long instructorId) {
            return instructorId != null && instructorId == 1L;
        }
    }
}
