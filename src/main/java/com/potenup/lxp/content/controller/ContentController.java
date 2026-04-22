package com.potenup.lxp.content.controller;

import com.potenup.lxp.content.dto.ContentCreateRequest;
import com.potenup.lxp.content.service.ContentService;

public class ContentController {
	private final ContentService contentService;

	public ContentController(ContentService contentService) {
		this.contentService = contentService;
	}

	public Long createContent(ContentCreateRequest request) {
		return contentService.createContent(request);
	}
}
