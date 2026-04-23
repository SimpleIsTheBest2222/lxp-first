package com.potenup.lxp.instructor.repository;

import com.potenup.lxp.instructor.domain.Instructor;

import java.util.List;
import java.util.Optional;

// 강사 저장소의 추상화다. 서비스는 구현체 대신 이 인터페이스에 의존한다.
public interface InstructorRepository {
	Long save(Instructor instructor);

	List<Instructor> findAll();

	Optional<Instructor> findById(Long instructorId);

	void update(Instructor instructor);

	void deleteById(Long instructorId);

	boolean existsById(Long instructorId);
}
