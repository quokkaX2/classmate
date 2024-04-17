package com.quokka.classmate.repository;

import com.quokka.classmate.domain.entity.Subject;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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

    // 네이티브 SQL 쿼리를 사용하여 풀텍스트 검색 실행
    @Query(value = "SELECT * FROM subjects WHERE MATCH(title) AGAINST(?1 IN BOOLEAN MODE)", nativeQuery = true)
    Page<Subject> searchByTitleFullText(String input, Pageable pageable);

    Page<Subject> findByTitleContaining(String input, Pageable pageable);
}
