package com.potenup.lxp.course.domain;

import com.potenup.lxp.common.exception.ValidationException;

public enum CourseLevel {
    LOW,
    MEDIUM,
    HIGH;

    public static CourseLevel fromInput(String input) {
        for (CourseLevel level : values()) {
            if (level.name().equalsIgnoreCase(input)) {
                return level;
            }
        }
        throw new ValidationException(
            "유효하지 않은 레벨입니다: " + input + " (LOW, MEDIUM, HIGH 중 입력)"
        );
    }
}
