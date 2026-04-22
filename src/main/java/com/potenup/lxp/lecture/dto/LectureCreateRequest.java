package com.potenup.lxp.lecture.dto;

import com.potenup.lxp.lecture.domain.LectureLevel;

public class LectureCreateRequest {
    private String title;
    private String description;
    private int price;
    private LectureLevel level;

    public LectureCreateRequest(String title, String description, int price, LectureLevel level) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public LectureLevel getLevel() {
        return level;
    }
}
