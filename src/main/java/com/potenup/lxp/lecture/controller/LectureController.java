package com.potenup.lxp.lecture.controller;

import com.potenup.lxp.lecture.dto.LectureCreateRequest;
import com.potenup.lxp.lecture.service.LectureService;

public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    public Long createLecture(LectureCreateRequest request) {
        return lectureService.createLecture(request);
    }
}
