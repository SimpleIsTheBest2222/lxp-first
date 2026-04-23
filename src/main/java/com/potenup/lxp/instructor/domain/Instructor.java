package com.potenup.lxp.instructor.domain;

// 강사 도메인 객체로, 생성 시점에 기본 유효성 검증을 함께 수행한다.
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
		// DB 식별자는 1 이상의 값만 유효하다고 본다.
		if (id == null || id <= 0) {
			throw new IllegalArgumentException("Instructor id must be greater than zero.");
		}
	}

	private void validateName(String name) {
		// 이름은 비어 있을 수 없고 화면 정책에 맞는 최대 길이를 넘으면 안 된다.
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
		// 소개도 필수 값이며 저장 전에 길이를 제한한다.
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
