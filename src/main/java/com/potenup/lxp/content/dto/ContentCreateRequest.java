package com.potenup.lxp.content.dto;

import com.potenup.lxp.content.domain.ContentType;

public class ContentCreateRequest {
	private String title;
	private ContentType type;
	private String url;
	private String body;
	private String filePath;

	public ContentCreateRequest(String title, ContentType type, String url, String body, String filePath) {
		this.title = title;
		this.type = type;
		this.url = url;
		this.body = body;
		this.filePath = filePath;
	}

	public String getTitle() {
		return title;
	}

	public ContentType getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public String getBody() {
		return body;
	}

	public String getFilePath() {
		return filePath;
	}
}
