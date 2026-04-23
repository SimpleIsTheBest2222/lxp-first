package com.potenup.lxp;

import com.potenup.lxp.common.jdbc.DatabaseConfig;
import com.potenup.lxp.common.jdbc.JdbcConnectionManager;
import com.potenup.lxp.common.query.QueryRegistry;
import com.potenup.lxp.course.cli.CourseCliHandler;
import com.potenup.lxp.course.controller.CourseController;
import com.potenup.lxp.course.repository.CourseRepository;
import com.potenup.lxp.course.repository.JdbcCourseRepository;
import com.potenup.lxp.course.service.CourseService;
import com.potenup.lxp.instructor.cli.InstructorCliHandler;
import com.potenup.lxp.instructor.controller.InstructorController;
import com.potenup.lxp.instructor.repository.InstructorRepository;
import com.potenup.lxp.instructor.repository.JdbcInstructorRepository;
import com.potenup.lxp.instructor.service.InstructorService;

import java.util.Scanner;

public class LxpApplication {
    public static void main(String[] args) {
        DatabaseConfig databaseConfig = DatabaseConfig.fromResource("/config.yml");
        JdbcConnectionManager connectionManager = new JdbcConnectionManager(databaseConfig);
        QueryRegistry queryRegistry = QueryRegistry.fromXmlResource("/queries.xml");

        InstructorRepository instructorRepository = new JdbcInstructorRepository(connectionManager, queryRegistry);
        CourseRepository courseRepository = new JdbcCourseRepository(connectionManager, queryRegistry);
        InstructorService instructorService = new InstructorService(instructorRepository, courseRepository);
        InstructorController instructorController = new InstructorController(instructorService);
        CourseService courseService = new CourseService(courseRepository, instructorRepository);
        CourseController courseController = new CourseController(courseService);

        try (Scanner scanner = new Scanner(System.in)) {
            InstructorCliHandler instructorCliHandler = new InstructorCliHandler(instructorController, scanner);
            CourseCliHandler courseCliHandler = new CourseCliHandler(courseController, scanner);
            runMainMenu(courseCliHandler, instructorCliHandler, scanner);
        }
    }

    private static void runMainMenu(
            CourseCliHandler courseCliHandler,
            InstructorCliHandler instructorCliHandler,
            Scanner scanner
    ) {
        while (true) {
            printTitle("강의 관리 콘솔");
            System.out.println("1. 강의 관리");
            System.out.println("2. 강사 관리");
            System.out.println("3. 종료");

            int choice = readInt(scanner, "선택 > ");
            if (choice == 1) {
                courseCliHandler.run();
            } else if (choice == 2) {
                instructorCliHandler.run();
            } else if (choice == 3) {
                System.out.println("프로그램을 종료합니다.");
                return;
            } else {
                System.out.println("잘못된 메뉴입니다.");
            }
        }
    }

    private static void printTitle(String title) {
        System.out.println();
        System.out.println("==============================");
        System.out.println(title);
        System.out.println("==============================");
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }
}
