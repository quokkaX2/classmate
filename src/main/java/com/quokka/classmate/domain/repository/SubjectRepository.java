package com.quokka.classmate.domain.repository;

import com.quokka.classmate.domain.entity.Subject;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


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


    // 네이티브 SQL 쿼리를 사용하여 풀텍스트 검색 실행
    @Query(value = "SELECT * FROM subjects WHERE MATCH(title) AGAINST(?1 IN BOOLEAN MODE)", nativeQuery = true)
    List<Subject> searchByTitleFullText(String searchTerm);

    // FULLTEXT 사용 X
//    @Query(value = "SELECT * FROM subjects WHERE title LIKE %:input% AND (:cursor IS NULL OR subject_id > :cursor) ORDER BY subject_id ASC LIMIT :size", nativeQuery = true)
//    List<Subject> findByTitleWithCursor(@Param("input") String input, @Param("cursor") Long cursor, @Param("size") int size);

    // FULLTEXT 사용
    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM subjects WHERE MATCH(title) AGAINST(:input IN BOOLEAN MODE) AND (:cursor IS NULL OR subject_id > :cursor) ORDER BY subject_id ASC LIMIT :size", nativeQuery = true)
    List<Subject> findByTitleWithCursor(@Param("input") String input, @Param("cursor") Long cursor, @Param("size") int size);
    @Query("SELECT s FROM Subject s WHERE s.title LIKE CONCAT('%', :input, '%') AND (:cursor IS NULL OR s.id > :cursor)")
    Page<Subject> findByTitleContainingWithCursor(@Param("input") String input, @Param("cursor") Long cursor, Pageable pageable);
}
