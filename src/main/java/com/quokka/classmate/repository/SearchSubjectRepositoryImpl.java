package com.quokka.classmate.repository;

import com.quokka.classmate.domain.document.SearchSubject;
import com.quokka.classmate.domain.dto.SubjectResponseDto;
import com.quokka.classmate.domain.entity.Subject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.elasticsearch.core.SearchHit;

@RequiredArgsConstructor
@Repository
public class SearchSubjectRepositoryImpl implements SearchSubjectRepository{


    // query를 받아서 elasticsearch에 요청을 보내는 역할을 한다.
    private final ElasticsearchOperations elasticsearchOperations;

    public Page<SearchSubject> searchSubjectsByTitle(String title, Pageable pageable) {
//        Criteria criteria = Criteria.where("title").contains(title);

        // 1번 개선책
//        Criteria criteria = new Criteria("title").matches(title);

        // 2번 개선책
        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .match(m -> m
                                .field("title")
                                .query(title)
                        )
                )
                .withPageable(pageable)
                .build();

        // 3번 개선책 : Json 쿼리 직접 박아넣기

//        Query query = new CriteriaQuery(criteria).setPageable(pageable);

        SearchHits<SearchSubject> search = elasticsearchOperations.search(query, SearchSubject.class);

        List<SearchSubject> list = search.stream().map(SearchHit::getContent).collect(Collectors.toList());

        return new PageImpl<>(list, pageable, search.getTotalHits());
    }

    // with Cursor
//    public List<SubjectResponseDto> findSubjectWithCursor(String title, int size) {
//        // 초기 커서 기반 페이징 쿼리를 생성합니다.
//        Query query = NativeQuery.builder()
//                .withQuery(q -> q
//                        .match(m -> m
//                                .field("title")
//                                .query(title)
//                        )
//                )
//                .withPageable(PageRequest.of(0, size))
//                .build();
//
//        // 첫 번째 페이지의 결과를 가져옵니다.
//        SearchHits<Subject> searchHits = query.search(query, Subject.class);
//        List<Subject> result = searchHits.getSearchHits().stream()
//                .map(SearchHit::getContent)
//                .toList();
//
//        // 스크롤 ID를 가져옵니다.
//        String scrollId = searchHits.getScrollId();
//
//        // 추가 페이지를 가져오기 위해 스크롤을 사용합니다.
//        while (result.size() < size) {
//            // 스크롤을 사용하여 다음 페이지의 결과를 가져옵니다.
//            searchHits = query.searchScroll(scrollId, Duration.ofMinutes(1), Subject.class);
//
//            // 새로운 결과를 결과 목록에 추가합니다.
//            result.addAll(searchHits.getSearchHits().stream()
//                    .map(SearchHit::getContent)
//                    .collect(Collectors.toList()));
//
//            // 스크롤 ID를 업데이트합니다.
//            scrollId = searchHits.getScrollId();
//
//            // 더 이상 결과가 없는 경우 루프를 종료합니다.
//            if (searchHits.getSearchHits().isEmpty()) {
//                break;
//            }
//        }
//
//        // 사용이 완료된 스크롤을 삭제합니다.
//        query.clearScroll(scrollId);
//
//        return result;
//    }
}
