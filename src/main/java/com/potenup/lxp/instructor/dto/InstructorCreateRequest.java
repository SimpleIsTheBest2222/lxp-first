package com.potenup.lxp.instructor.dto;

// 강사 등록 화면에서 서비스 계층으로 전달하는 요청 DTO다.
public class InstructorCreateRequest {
	private String name;
	private String introduction;

	public InstructorCreateRequest(String name, String introduction) {
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
