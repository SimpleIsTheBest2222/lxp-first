package com.potenup.lxp.lecture.domain;

import java.sql.Timestamp;

public class Lecture {
    private Long id;
    private String title;
    private String description;
    private int price;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private LectureLevel level;

    public Lecture(String title, String description, int price, LectureLevel level) {
        validateTitle(title);
        validateDescription(description);
        validatePrice(price);
        validateLevel(level);

        this.title = title;
        this.description = description;
        this.price = price;
        this.level = level;
    }

    public Long getId() {
        return id;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public LectureLevel getLevel() {
        return level;
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Lecture title is required.");
        }
        if (title.length() > 50) {
            throw new IllegalArgumentException("Lecture title must be 50 characters or less.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Lecture description is required.");
        }
        if (description.length() > 200) {
            throw new IllegalArgumentException("Lecture description must be 200 characters or less.");
        }
    }

    private void validatePrice(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("Lecture price must be zero or greater.");
        }
    }

    private void validateLevel(LectureLevel level) {
        if (level == null) {
            throw new IllegalArgumentException("Lecture level is required.");
        }
    }
}
