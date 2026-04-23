package com.potenup.lxp.course.repository;

import com.potenup.lxp.course.domain.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Long save(Course course);

    List<Course> findAll();

    Optional<Course> findById(Long courseId);

    void update(Course course);

    void deleteById(Long courseId);

    boolean existsById(Long courseId);

    boolean existsByInstructorId(Long instructorId);
}
