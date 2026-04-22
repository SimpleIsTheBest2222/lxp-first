package com.potenup.lxp.instructor.dto;

public class InstructorUpdateRequest {
	private String name;
	private String introduction;

	public InstructorUpdateRequest(String name, String introduction) {
		this.name = name;
		this.introduction = introduction;
	}

	public String getName() {
		return name;
	}

	public String getIntroduction() {
		return introduction;
	}
}
