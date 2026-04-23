package com.potenup.lxp.instructor.cli;

import com.potenup.lxp.instructor.controller.InstructorController;
import com.potenup.lxp.instructor.domain.Instructor;
import com.potenup.lxp.instructor.dto.InstructorCreateRequest;
import com.potenup.lxp.instructor.dto.InstructorUpdateRequest;

import java.util.List;
import java.util.Scanner;

// 강사 관리 화면의 입력과 출력 흐름을 담당하는 CLI 핸들러다.
public class InstructorCliHandler {
	private final InstructorController instructorController;
	private final Scanner scanner;

	public InstructorCliHandler(InstructorController instructorController, Scanner scanner) {
		this.instructorController = instructorController;
		this.scanner = scanner;
	}

	public void run() {
		// 강사 관리 메뉴에서 사용자가 뒤로 가기를 선택할 때까지 반복한다.
		while (true) {
			printTitle("강사 관리");
			System.out.println("""
				1. 강사 등록
				2. 강사 조회
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
			printTitle("강사 등록");

			String name = readLine("이름 > ");
			String introduction = readLine("소개 > ");

			try {
				Long instructorId = instructorController.createInstructor(
					new InstructorCreateRequest(name, introduction)
				);
				System.out.println("강사가 등록되었습니다. id: " + instructorId);
			} catch (IllegalArgumentException exception) {
				// 도메인 검증 실패 메시지는 그대로 사용자에게 보여 준다.
				System.out.println(exception.getMessage());
				continue;
			}

			System.out.println();
			System.out.println("""
				1. 강사 등록
				2. 강사 조회
				""");
			int choice = readInt("선택 > ");
			if (choice == 1) {
				continue;
			}
			if (choice == 2) {
				showListScreen();
			} else {
				System.out.println("잘못된 메뉴입니다.");
				continue;
			}
			return;
		}
	}

	private void showListScreen() {
		while (true) {
			printTitle("강사 목록");
			// 목록은 항상 최신 상태를 보이도록 화면 진입 시마다 다시 조회한다.
			List<Instructor> instructors = instructorController.getInstructors();

			if (instructors.isEmpty()) {
				System.out.println("등록된 강사가 없습니다.");
			} else {
				for (Instructor instructor : instructors) {
					System.out.println(instructor.getId() + ". " + instructor.getName());
				}
			}

			System.out.println();
			System.out.println("""
				1. 강사 선택
				2. 뒤로 가기
				""");

			int choice = readInt("선택 > ");
			if (choice == 1) {
				Long instructorId = readLong("강사 ID > ");
				showDetailScreen(instructorId);
			} else if (choice == 2) {
				return;
			} else {
				System.out.println("잘못된 메뉴입니다.");
			}
		}
	}

	private void showDetailScreen(Long instructorId) {
		while (true) {
			Instructor instructor;

			// 조회 대상이 없으면 상세 화면에 머무르지 않고 목록으로 돌아간다.
			try {
				instructor = instructorController.getInstructor(instructorId);
			} catch (IllegalArgumentException exception) {
				System.out.println("존재하지 않는 강사입니다.");
				return;
			}

			printTitle("강사 상세");
			System.out.println("강사 id: " + instructor.getId());
			System.out.println("이름: " + instructor.getName());
			System.out.println("소개: " + instructor.getIntroduction());
			System.out.println();
			System.out.println("""
				1. 강사 수정
				2. 강사 삭제
				3. 뒤로 가기
				""");

			int choice = readInt("선택 > ");
			if (choice == 1) {
				showUpdateScreen(instructor);
			} else if (choice == 2) {
				// 강사에 연결된 강의가 있으면 삭제할 수 없으므로 안내만 하고 화면을 유지한다.
				try {
					instructorController.deleteInstructor(instructorId);
					System.out.println("삭제가 완료되었습니다. id: " + instructorId);
					return;
				} catch (IllegalStateException exception) {
					System.out.println(exception.getMessage());
				}
			} else if (choice == 3) {
				return;
			} else {
				System.out.println("잘못된 메뉴입니다.");
			}
		}
	}

	private void showUpdateScreen(Instructor currentInstructor) {
		Instructor instructor = currentInstructor;
		while (true) {
			printTitle("강사 수정");
			System.out.println("빈 값 입력 시 기존 값이 유지됩니다.");
			String name = readLine("이름(" + instructor.getName() + ") > ");
			String introduction = readLine("소개(" + instructor.getIntroduction() + ") > ");

			// 공백 입력은 기존 값을 유지하도록 서비스 계층에서 해석한다.
			try {
				instructor = instructorController.updateInstructor(
					instructor.getId(),
					new InstructorUpdateRequest(name, introduction)
				);
				System.out.println("수정되었습니다.");
				System.out.println("강사 id: " + instructor.getId());
				System.out.println("이름: " + instructor.getName());
				System.out.println("소개: " + instructor.getIntroduction());
			} catch (IllegalArgumentException exception) {
				System.out.println(exception.getMessage());
				continue;
			}

			System.out.println();
			System.out.println("""
				1. 강사 수정
				2. 강사 삭제
				3. 뒤로 가기
				""");

			int choice = readInt("선택 > ");
			if (choice == 1) {
				continue;
			}
			if (choice == 2) {
				// 삭제 조건을 만족하지 않으면 수정 화면에서 바로 재시도할 수 있게 유지한다.
				try {
					instructorController.deleteInstructor(instructor.getId());
					System.out.println("삭제가 완료되었습니다. id: " + instructor.getId());
				} catch (IllegalStateException exception) {
					System.out.println(exception.getMessage());
					continue;
				}
			}
			return;
		}
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

	private String readLine(String prompt) {
		System.out.print(prompt);
		return scanner.nextLine().trim();
	}
}
