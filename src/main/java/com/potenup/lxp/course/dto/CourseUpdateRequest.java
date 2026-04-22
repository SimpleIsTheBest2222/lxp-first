package com.potenup.lxp.course.dto;

import com.potenup.lxp.course.domain.CourseLevel;

public class CourseUpdateRequest {
	private final String title;
	private final String description;
	private final Integer price;
	private final CourseLevel level;
	private final Long instructorId;

	public CourseUpdateRequest(String title, String description, Integer price, CourseLevel level, Long instructorId) {
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

	public Integer getPrice() {
		return price;
	}

	public CourseLevel getLevel() {
		return level;
	}

	public Long getInstructorId() {
		return instructorId;
	}
}
