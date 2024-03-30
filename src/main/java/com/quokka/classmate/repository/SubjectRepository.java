package com.quokka.classmate.repository;

import com.quokka.classmate.domain.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    // 검색 기반 강의 조회
    List<Subject> findByNameContaining(String input);
}
