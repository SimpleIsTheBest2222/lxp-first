package com.potenup.lxp.instructor.dto;

// 강사 수정 화면에서 서비스 계층으로 전달하는 요청 DTO다.
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
