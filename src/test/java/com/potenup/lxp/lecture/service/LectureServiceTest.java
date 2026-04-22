package com.potenup.lxp.lecture.service;

import com.potenup.lxp.lecture.domain.LectureLevel;
import com.potenup.lxp.lecture.dto.LectureCreateRequest;
import com.potenup.lxp.lecture.repository.LectureRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LectureServiceTest {
    @Test
    void createLectureReturnsSavedId() {
        LectureService lectureService = new LectureService(new LectureRepository());

        Long lectureId = lectureService.createLecture(
                new LectureCreateRequest("JDBC Basics", "Intro lecture", 30000, LectureLevel.LOW)
        );

        assertEquals(1L, lectureId);
    }
}
