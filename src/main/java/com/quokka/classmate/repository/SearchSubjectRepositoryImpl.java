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
    @PostConstruct
    public void syncSubjectToElasticsearch() {
        List<Subject> subjects = subjectRepository.findAll(); // 애시당초 이걸 쓰는 게 맞으려나?
        List<SearchSubject> elasticSubjects =
                subjects.stream().map(this::convertToElasticSubject).toList();

        elasticsearchOperations.save(elasticSubjects);
    }
    private SearchSubject convertToElasticSubject(Subject subject) {
        return new SearchSubject(
                subject.getId(),
                subject.getTitle(),
                subject.getLimitCount(),
                subject.getTime(),
                subject.getCredit());
    }
    @Override
    public Page<SearchSubject> searchSubjectsByTitle(String title, Pageable pageable) {
        Criteria criteria = Criteria.where("title").contains(title);

        Query query = new CriteriaQuery(criteria).setPageable(pageable);

        SearchHits<SearchSubject> search = elasticsearchOperations.search(query, SearchSubject.class);

        List<SearchSubject> list = search.stream().map(SearchHit::getContent).collect(Collectors.toList());

        return new PageImpl<>(list, pageable, search.getTotalHits());
    }

    public Optional<SearchSubject> findById(Long id) {
        SearchSubject searchSubject = elasticsearchOperations
                .get(String.valueOf(id), SearchSubject.class);
        return Optional.ofNullable(searchSubject);
    }

    public SearchSubject save(SearchSubject searchSubject) {
        return elasticsearchOperations.save(searchSubject);
    }
}
