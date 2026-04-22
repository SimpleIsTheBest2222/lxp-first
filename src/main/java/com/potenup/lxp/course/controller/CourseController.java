package com.potenup.lxp.course.controller;

import com.potenup.lxp.course.domain.Course;
import com.potenup.lxp.course.dto.CourseCreateRequest;
import com.potenup.lxp.course.dto.CourseUpdateRequest;
import com.potenup.lxp.course.service.CourseService;

import java.util.List;

public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    public Long createCourse(CourseCreateRequest request) {
        return courseService.createCourse(request);
    }

    public List<Course> getCourses() {
        return courseService.getCourses();
    }

    public Course getCourse(Long courseId) {
        return courseService.getCourse(courseId);
    }

    public Course updateCourse(Long courseId, CourseUpdateRequest request) {
        return courseService.updateCourse(courseId, request);
    }

    public void deleteCourse(Long courseId) {
        courseService.deleteCourse(courseId);
    }
}
