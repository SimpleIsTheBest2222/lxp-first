package com.potenup.lxp.instructor.service;

import com.potenup.lxp.common.exception.ConstraintViolationException;
import com.potenup.lxp.instructor.domain.Instructor;
import com.potenup.lxp.instructor.dto.InstructorCreateRequest;
import com.potenup.lxp.instructor.dto.InstructorUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// InstructorService의 비즈니스 규칙을 가짜 저장소로 검증하는 테스트다.
class InstructorServiceTest {
    private FakeInstructorRepository instructorRepository;
    private FakeCourseRepository courseRepository;
    private InstructorService instructorService;

    @BeforeEach
    void setUp() {
        // 각 테스트가 서로 영향을 주지 않도록 매번 새로운 가짜 저장소와 서비스를 만든다.
        instructorRepository = new FakeInstructorRepository();
        courseRepository = new FakeCourseRepository();
        instructorService = new InstructorService(instructorRepository, courseRepository);
    }

    @Test
    void createInstructorReturnsSavedId() {
        Long instructorId = instructorService.createInstructor(
            new InstructorCreateRequest("Kim", "Java and DB instructor")
        );

        assertEquals(1L, instructorId);
    }

    @Test
    void getInstructorsReturnsAllInstructors() {
        // 서비스가 저장소의 강사 목록을 그대로 반환하는지 확인한다.
        List<Instructor> instructors = instructorService.getInstructors();

        assertEquals(2, instructors.size());
        assertEquals("Kim", instructors.get(0).getName());
    }

    @Test
    void updateInstructorKeepsExistingValuesWhenBlankIsProvided() {
        // 빈 문자열이면 기존 값이 유지되는 수정 규칙을 검증한다.
        Instructor updatedInstructor = instructorService.updateInstructor(1L, new InstructorUpdateRequest("", ""));

        assertEquals("Kim", updatedInstructor.getName());
        assertEquals("Java and DB instructor", updatedInstructor.getIntroduction());
    }

    @Test
    void deleteInstructorRemovesInstructor() {
        instructorService.deleteInstructor(1L);

        assertFalse(instructorRepository.existsById(1L));
    }

    @Test
    void deleteInstructorFailsWhenAssignedCourseExists() {
        // 삭제 실패 시나리오를 만들기 위해 1번 강사에 연결된 강의가 있다고 가정한다.
        courseRepository.assignInstructor(1L);

        ConstraintViolationException exception = assertThrows(
            ConstraintViolationException.class,
            () -> instructorService.deleteInstructor(1L)
        );

        assertTrue(exception.getMessage() != null && !exception.getMessage().isBlank());
        assertFalse(instructorRepository.findById(1L).isEmpty());
    }
}
