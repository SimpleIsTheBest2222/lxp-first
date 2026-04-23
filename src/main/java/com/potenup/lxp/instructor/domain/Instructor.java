package com.potenup.lxp.instructor.domain;

public class Instructor {
	private static final int MAX_NAME_LENGTH = 10;
	private static final int MAX_INTRODUCTION_LENGTH = 100;

	private Long id;
	private String name;
	private String introduction;

	public Instructor(String name, String introduction) {
		validateName(name);
		validateIntroduction(introduction);

		this.name = name;
		this.introduction = introduction;
	}

	public Instructor(Long id, String name, String introduction) {
		validateId(id);
		validateName(name);
		validateIntroduction(introduction);

		this.id = id;
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

	private void validateId(Long id) {
		if (id == null || id <= 0) {
			throw new IllegalArgumentException("Instructor id must be greater than zero.");
		}
	}

	private void validateName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Instructor name is required.");
		}
		if (name.length() > MAX_NAME_LENGTH) {
			throw new IllegalArgumentException(
					"Instructor name must be " + MAX_NAME_LENGTH + " characters or less."
			);
		}
	}

	private void validateIntroduction(String introduction) {
		if (introduction == null || introduction.isBlank()) {
			throw new IllegalArgumentException("Instructor introduction is required.");
		}
		if (introduction.length() > MAX_INTRODUCTION_LENGTH) {
			throw new IllegalArgumentException(
					"Instructor introduction must be " + MAX_INTRODUCTION_LENGTH + " characters or less."
			);
		}
	}
}
