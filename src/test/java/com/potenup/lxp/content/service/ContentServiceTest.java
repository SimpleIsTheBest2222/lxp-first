package com.potenup.lxp.content.service;

import com.potenup.lxp.content.domain.ContentType;
import com.potenup.lxp.content.dto.ContentCreateRequest;
import com.potenup.lxp.content.repository.ContentRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContentServiceTest {
	@Test
	void createVideoContentReturnsSavedId() {
		ContentService contentService = new ContentService(new ContentRepository());

		Long contentId = contentService.createContent(
			new ContentCreateRequest(
				"JDBC Connection Overview",
				ContentType.VIDEO,
				"https://example.com/video/1",
				null,
				null
			)
		);

		assertEquals(1L, contentId);
	}
}
