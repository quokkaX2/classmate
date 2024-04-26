package com.quokka.classmate.repository;

import com.quokka.classmate.domain.document.SearchSubject;
import com.quokka.classmate.domain.entity.Subject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.elasticsearch.core.SearchHit;

@RequiredArgsConstructor
@Repository
public class SearchSubjectRepositoryImpl implements SearchSubjectRepository{


    // query를 받아서 elasticsearch에 요청을 보내는 역할을 한다.
    private final ElasticsearchOperations elasticsearchOperations;
    private final SubjectRepository subjectRepository;
    public Page<SearchSubject> searchSubjectsByTitle(String title, Pageable pageable) {
        Criteria criteria = Criteria.where("title").contains(title);

        Query query = new CriteriaQuery(criteria).setPageable(pageable);

        SearchHits<SearchSubject> search = elasticsearchOperations.search(query, SearchSubject.class);

        List<SearchSubject> list = search.stream().map(SearchHit::getContent).collect(Collectors.toList());

        return new PageImpl<>(list, pageable, search.getTotalHits());
    }
}
