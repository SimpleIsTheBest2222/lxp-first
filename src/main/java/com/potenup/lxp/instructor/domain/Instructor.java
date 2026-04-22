package com.potenup.lxp.instructor.domain;

import java.sql.Timestamp;

public class Instructor {
	private Long id;
	private String name;
	private String introduction;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public Instructor(String name, String introduction) {
		validateName(name);

		this.name = name;
		this.introduction = introduction;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	private void validateName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Instructor name is required.");
		}
		if (name.length() > 10) {
			throw new IllegalArgumentException("Instructor name must be 10 characters or less.");
		}
	}
}
