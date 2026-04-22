package com.potenup.lxp.course.service;

import com.potenup.lxp.course.domain.Course;
import com.potenup.lxp.course.dto.CourseCreateRequest;
import com.potenup.lxp.course.dto.CourseUpdateRequest;
import com.potenup.lxp.course.repository.CourseRepository;
import com.potenup.lxp.instructor.repository.InstructorRepository;

import java.util.List;

public class CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    public CourseService(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    public Long createCourse(CourseCreateRequest request) {
        if (!instructorRepository.existsById(request.getInstructorId())) {
            throw new IllegalArgumentException("강사가 존재하지 않습니다. 강사를 먼저 등록해주세요.");
        }

        Course course = new Course(
                request.getTitle(),
                request.getDescription(),
                request.getPrice(),
                request.getLevel(),
                request.getInstructorId()
        );
        return courseRepository.save(course);
    }

    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    public Course getCourse(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다."));
    }

    public Course updateCourse(Long courseId, CourseUpdateRequest request) {
        Course currentCourse = getCourse(courseId);

        Long updatedInstructorId = request.getInstructorId() == null
                ? currentCourse.getInstructorId()
                : request.getInstructorId();

        if (!instructorRepository.existsById(updatedInstructorId)) {
            throw new IllegalArgumentException("강사가 존재하지 않습니다. 강사를 먼저 등록해주세요.");
        }

        Course updatedCourse = new Course(
                currentCourse.getId(),
                isBlank(request.getTitle()) ? currentCourse.getTitle() : request.getTitle(),
                isBlank(request.getDescription()) ? currentCourse.getDescription() : request.getDescription(),
                request.getPrice() == null ? currentCourse.getPrice() : request.getPrice(),
                request.getLevel() == null ? currentCourse.getLevel() : request.getLevel(),
                updatedInstructorId
        );

        courseRepository.update(updatedCourse);
        return updatedCourse;
    }

    public void deleteCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new IllegalArgumentException("강의를 찾을 수 없습니다.");
        }
        courseRepository.deleteById(courseId);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
