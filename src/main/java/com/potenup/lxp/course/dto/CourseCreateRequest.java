package com.potenup.lxp.course.dto;

import com.potenup.lxp.course.domain.CourseLevel;

public class CourseCreateRequest {
    private String title;
    private String description;
    private int price;
    private CourseLevel level;
    private Long instructorId;

    public CourseCreateRequest(String title, String description, int price, CourseLevel level, Long instructorId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.level = level;
        this.instructorId = instructorId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public CourseLevel getLevel() {
        return level;
    }

    public Long getInstructorId() {
        return instructorId;
    }
}
