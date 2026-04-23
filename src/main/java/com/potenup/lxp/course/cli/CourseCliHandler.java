package com.potenup.lxp.course.cli;

import com.potenup.lxp.course.controller.CourseController;
import com.potenup.lxp.course.domain.Course;
import com.potenup.lxp.course.domain.CourseLevel;
import com.potenup.lxp.course.dto.CourseCreateRequest;
import com.potenup.lxp.course.dto.CourseUpdateRequest;

import java.util.List;
import java.util.Scanner;

public class CourseCliHandler {
    private final CourseController courseController;
    private final Scanner scanner;

    public CourseCliHandler(CourseController courseController, Scanner scanner) {
        this.courseController = courseController;
        this.scanner = scanner;
    }

    public void run() {
        while (true) {
            printTitle("강의 관리");
            System.out.println("""
                    1. 강의 등록
                    2. 강의 조회
                    3. 뒤로 가기
                    """);

            int choice = readInt("선택 > ");
            if (choice == 1) {
                showCreateScreen();
            } else if (choice == 2) {
                showListScreen();
            } else if (choice == 3) {
                return;
            } else {
                System.out.println("잘못된 메뉴입니다.");
            }
        }
    }

    private void showCreateScreen() {
        while (true) {
            printTitle("강의 등록");

            String title = readLine("제목 > ");
            String description = readLine("설명 > ");
            int price = readInt("가격 > ");
            CourseLevel level = readCourseLevel("난이도(LOW/MEDIUM/HIGH) > ");
            Long instructorId = readLong("강사 ID > ");

            try {
                Long courseId = courseController.createCourse(
                        new CourseCreateRequest(title, description, price, level, instructorId)
                );
                System.out.println("강의가 등록되었습니다. id: " + courseId);
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
                continue;
            }

            System.out.println();
            System.out.println("""
                    1. 강의 등록
                    2. 강의 조회
                    """);

            int choice = readInt("선택 > ");
            if (choice == 1) {
                continue;
            }
            if (choice == 2) {
                showListScreen();
            }
            return;
        }
    }

    private void showListScreen() {
        while (true) {
            printTitle("강의 목록");
            List<Course> courses = courseController.getCourses();

            if (courses.isEmpty()) {
                System.out.println("등록된 강의가 없습니다.");
            } else {
                for (Course course : courses) {
                    System.out.println(course.getId() + ". " + course.getTitle());
                }
            }

            System.out.println();
            System.out.println("""
                    1. 강의 선택
                    2. 뒤로 가기
                    """);

            int choice = readInt("선택 > ");
            if (choice == 1) {
                Long courseId = readLong("강의 ID > ");
                showDetailScreen(courseId);
            } else if (choice == 2) {
                return;
            } else {
                System.out.println("잘못된 메뉴입니다.");
            }
        }
    }

    private void showDetailScreen(Long courseId) {
        while (true) {
            Course course;

            try {
                course = courseController.getCourse(courseId);
            } catch (IllegalArgumentException exception) {
                System.out.println("존재하지 않는 강의입니다.");
                return;
            }

            printTitle("강의 상세");
            printCourseDetail(course);
            System.out.println();
            System.out.println("""
                    1. 강의 수정
                    2. 강의 삭제
                    3. 뒤로 가기
                    """);

            int choice = readInt("선택 > ");
            if (choice == 1) {
                showUpdateScreen(course);
            } else if (choice == 2) {
                courseController.deleteCourse(courseId);
                System.out.println("삭제가 완료되었습니다. id: " + courseId);
                return;
            } else if (choice == 3) {
                return;
            } else {
                System.out.println("잘못된 메뉴입니다.");
            }
        }
    }

    private void showUpdateScreen(Course course) {
        while (true) {
            printTitle("강의 수정");
            System.out.println("빈 값 입력 시 기존 값이 유지됩니다.");
            String title = readLine("제목(" + course.getTitle() + ") > ");
            String description = readLine("설명(" + course.getDescription() + ") > ");
            Integer price = readOptionalInt("가격(" + course.getPrice() + ") > ");
            CourseLevel level = readOptionalCourseLevel("난이도(" + course.getLevel() + ") > ");
            Long instructorId = readOptionalLong("강사 ID(" + course.getInstructorId() + ") > ");

            try {
                Course updatedCourse = courseController.updateCourse(
                        course.getId(),
                        new CourseUpdateRequest(title, description, price, level, instructorId)
                );
                System.out.println("수정되었습니다.");
                printCourseDetail(updatedCourse);
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
                continue;
            }

            System.out.println();
            System.out.println("""
                    1. 강의 수정
                    2. 강의 삭제
                    3. 뒤로 가기
                    """);

            int choice = readInt("선택 > ");
            if (choice == 1) {
                continue;
            }
            if (choice == 2) {
                courseController.deleteCourse(course.getId());
                System.out.println("삭제가 완료되었습니다. id: " + course.getId());
            }
            return;
        }
    }

    private void printCourseDetail(Course course) {
        System.out.println("강의 id: " + course.getId());
        System.out.println("제목: " + course.getTitle());
        System.out.println("설명: " + course.getDescription());
        System.out.println("가격: " + course.getPrice());
        System.out.println("난이도: " + course.getLevel());
        System.out.println("강사 id: " + course.getInstructorId());
    }

    private void printTitle(String title) {
        System.out.println();
        System.out.println("==============================");
        System.out.println(title);
        System.out.println("==============================");
    }

    private int readInt(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }

    private Integer readOptionalInt(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (value.isBlank()) {
                return null;
            }
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }

    private Long readLong(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException exception) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }

    private Long readOptionalLong(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (value.isBlank()) {
                return null;
            }
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException exception) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }

    private CourseLevel readCourseLevel(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try {
                return CourseLevel.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException exception) {
                System.out.println("LOW, MEDIUM, HIGH 중 하나를 입력해주세요.");
            }
        }
    }

    private CourseLevel readOptionalCourseLevel(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (value.isBlank()) {
                return null;
            }
            try {
                return CourseLevel.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException exception) {
                System.out.println("LOW, MEDIUM, HIGH 중 하나를 입력해주세요.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
