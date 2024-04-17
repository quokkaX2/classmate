package com.quokka.classmate.repository;

import com.quokka.classmate.domain.entity.RegisteredSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegisteredSubjectRepository extends JpaRepository<RegisteredSubject, Long> {

    @Query("select rs from RegisteredSubject rs " +
            "join fetch rs.subject " +
            "join fetch rs.student " +
            "where rs.student.id = :studentId")
    List<RegisteredSubject> findAllByStudentId(Long studentId);

    // 학생 정보와 과목 정보 기반으로 조회
//    Optional<RegisteredSubject> findByStudentAndSubject(Student student, Subject subject);

    Optional<RegisteredSubject> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    @Query("select rs from RegisteredSubject rs " +
            "join fetch rs.subject " +
            "join fetch rs.student " +
            "where rs.student.id = :studentId " +
            "and rs.isRegistered = true")
    List<RegisteredSubject> findByStudentIdAndIsRegisteredTrue(Long studentId);

    long countBySubjectId(Long savedSubjectId);
}
