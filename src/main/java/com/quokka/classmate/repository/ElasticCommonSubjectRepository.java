package com.quokka.classmate.repository;
import com.quokka.classmate.domain.document.SearchSubject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


/**
 * 엘라스틱 서치를 사용해서 상품 검색을 위한 기본 레포지토리입니다.
 * */

public interface ElasticCommonSubjectRepository extends ElasticsearchRepository<SearchSubject, Long> {

    Page<SearchSubject> findByTitle(Pageable pageable, String title);

    Optional<SearchSubject> findById(Long subjectId);

    Page<SearchSubject> findAllByTitleContaining(String title,Pageable pageable);

}