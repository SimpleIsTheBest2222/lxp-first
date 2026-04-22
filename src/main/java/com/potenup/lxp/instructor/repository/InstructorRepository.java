package com.potenup.lxp.instructor.repository;

import com.potenup.lxp.instructor.domain.Instructor;

import java.util.List;
import java.util.Optional;

public interface InstructorRepository {
	Long save(Instructor instructor);

	List<Instructor> findAll();

	Optional<Instructor> findById(Long instructorId);

	void update(Instructor instructor);

	void deleteById(Long instructorId);

	boolean existsById(Long instructorId);
}
