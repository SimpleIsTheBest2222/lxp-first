package com.potenup.lxp.content.domain;

import java.sql.Timestamp;

public class Content {
	private Long id;
	private String title;
	private ContentType type;
	private String url;
	private String body;
	private String filePath;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public Content(String title, ContentType type, String url, String body, String filePath) {
		validateTitle(title);
		validateType(type);
		validateTypeFields(type, url, body, filePath);

		this.title = title;
		this.type = type;
		this.url = url;
		this.body = body;
		this.filePath = filePath;
	}

	public Long getId() {
		return id;
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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	private void validateTitle(String title) {
		if (title == null || title.isBlank()) {
			throw new IllegalArgumentException("Content title is required.");
		}
	}

	private void validateType(ContentType type) {
		if (type == null) {
			throw new IllegalArgumentException("Content type is required.");
		}
	}

	private void validateTypeFields(ContentType type, String url, String body, String filePath) {
		if (type == ContentType.VIDEO && isBlank(url)) {
			throw new IllegalArgumentException("VIDEO content requires a URL.");
		}
		if (type == ContentType.TEXT && isBlank(body)) {
			throw new IllegalArgumentException("TEXT content requires a body.");
		}
		if (type == ContentType.FILE && isBlank(filePath)) {
			throw new IllegalArgumentException("FILE content requires a file path.");
		}
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}
}
