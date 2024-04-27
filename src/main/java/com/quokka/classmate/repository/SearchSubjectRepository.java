package com.quokka.classmate.repository;


import com.quokka.classmate.domain.document.SearchSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchSubjectRepository {
    Page<SearchSubject> searchSubjectsByTitle(String title, Pageable pageable);

    Page<SearchSubject> searchSubjectsByTitleWithCursor(String title, int pageNumber, int pageSize);
}
