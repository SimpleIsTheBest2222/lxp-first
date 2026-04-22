package com.potenup.lxp.course.domain;

import java.sql.Timestamp;

public class Course {
	private static final int MAX_TITLE_LENGTH = 50;
	private static final int MAX_DESCRIPTION_LENGTH = 200;
	private static final int MIN_PRICE = 0;
	private static final long MIN_VALID_ID = 1L;

	private Long id;
	private String title;
	private String description;
	private int price;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private CourseLevel level;
	private Long instructorId;

	public Course(String title, String description, int price, CourseLevel level, Long instructorId) {
		this(null, title, description, price, level, instructorId);
	}

	public Course(Long id, String title, String description, int price, CourseLevel level, Long instructorId) {
		validateId(id);
		validateTitle(title);
		validateDescription(description);
		validatePrice(price);
		validateLevel(level);
		validateInstructorId(instructorId);

		this.id = id;
		this.title = title;
		this.description = description;
		this.price = price;
		this.level = level;
		this.instructorId = instructorId;
	}

	public Long getId() {
		return id;
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

	public Timestamp getCreatedAt() {
		return createdAt == null ? null : new Timestamp(createdAt.getTime());
	}

	public Timestamp getUpdatedAt() {
		return updatedAt == null ? null : new Timestamp(updatedAt.getTime());
	}

	public CourseLevel getLevel() {
		return level;
	}

	public Long getInstructorId() {
		return instructorId;
	}

	private void validateId(Long id) {
		if (id != null && id < MIN_VALID_ID) {
			throw new IllegalArgumentException("Course id must be greater than zero.");
		}
	}

	private void validateTitle(String title) {
		if (title == null || title.isBlank()) {
			throw new IllegalArgumentException("Course title is required.");
		}
		if (title.length() > MAX_TITLE_LENGTH) {
			throw new IllegalArgumentException(
					"Course title must be " + MAX_TITLE_LENGTH + " characters or less."
			);
		}
	}

	private void validateDescription(String description) {
		if (description == null || description.isBlank()) {
			throw new IllegalArgumentException("Course description is required.");
		}
		if (description.length() > MAX_DESCRIPTION_LENGTH) {
			throw new IllegalArgumentException(
					"Course description must be " + MAX_DESCRIPTION_LENGTH + " characters or less."
			);
		}
	}

	private void validatePrice(int price) {
		if (price < MIN_PRICE) {
			throw new IllegalArgumentException("Course price must be " + MIN_PRICE + " or greater.");
		}
	}

	private void validateLevel(CourseLevel level) {
		if (level == null) {
			throw new IllegalArgumentException("Course level is required.");
		}
	}

	private void validateInstructorId(Long instructorId) {
		if (instructorId == null || instructorId < MIN_VALID_ID) {
			throw new IllegalArgumentException("Instructor id is required.");
		}
	}
}
