package com.quokka.classmate.repository;

import com.quokka.classmate.domain.entity.Subject;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    // 검색 기반 강의 조회
    List<Subject> findByTitleContaining(String input);

    Optional<Subject> findByTitle(String title);

    // 비관적 락 적용
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Subject s where s.id = :subjectId")
    Optional<Subject> findByIdForPessimistic(Long subjectId);

    Page<Subject> findByTitleContaining(String input, Pageable pageable);
}
