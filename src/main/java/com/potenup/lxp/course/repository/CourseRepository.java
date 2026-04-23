package com.potenup.lxp.course.repository;

import com.potenup.lxp.course.domain.Course;

public interface CourseRepository {
    Long save(Course course);

    boolean existsById(Long courseId);

    boolean existsByInstructorId(Long instructorId);
}
