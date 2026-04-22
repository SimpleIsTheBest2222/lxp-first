package com.potenup.lxp.content.service;

import com.potenup.lxp.content.domain.Content;
import com.potenup.lxp.content.dto.ContentCreateRequest;
import com.potenup.lxp.content.repository.ContentRepository;

public class ContentService {
	private final ContentRepository contentRepository;

	public ContentService(ContentRepository contentRepository) {
		this.contentRepository = contentRepository;
	}

	public Long createContent(ContentCreateRequest request) {
		Content content = new Content(
			request.getTitle(),
			request.getType(),
			request.getUrl(),
			request.getBody(),
			request.getFilePath()
		);
		return contentRepository.save(content);
	}
}
