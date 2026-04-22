package com.potenup.lxp.lecture.service;

import com.potenup.lxp.lecture.domain.Lecture;
import com.potenup.lxp.lecture.dto.LectureCreateRequest;
import com.potenup.lxp.lecture.repository.LectureRepository;

public class LectureService {
    private final LectureRepository lectureRepository;

    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public Long createLecture(LectureCreateRequest request) {
        Lecture lecture = new Lecture(
                request.getTitle(),
                request.getDescription(),
                request.getPrice(),
                request.getLevel()
        );
        return lectureRepository.save(lecture);
    }
}
