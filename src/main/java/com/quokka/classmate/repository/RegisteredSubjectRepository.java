package com.quokka.classmate.repository;

import com.quokka.classmate.domain.entity.RegisteredSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegisteredSubjectRepository extends JpaRepository<RegisteredSubject, Long> {

    List<RegisteredSubject> findAllByStudentId(Long id);
}
