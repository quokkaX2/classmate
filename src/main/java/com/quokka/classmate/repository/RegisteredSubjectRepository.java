package com.quokka.classmate.repository;

import com.quokka.classmate.domain.entity.RegisteredSubject;
import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.domain.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegisteredSubjectRepository extends JpaRepository<RegisteredSubject, Long> {

    List<RegisteredSubject> findAllByStudentId(Long id);

    // 학생 정보와 과목 정보 기반으로 조회
//    Optional<RegisteredSubject> findByStudentAndSubject(Student student, Subject subject);

    Optional<RegisteredSubject> findByStudentIdAndSubjectId(Long studentId, Long subjectId);
}
