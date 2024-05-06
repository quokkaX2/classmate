package com.quokka.classmate.domain.service;

import com.quokka.classmate.domain.document.SearchSubject;
import com.quokka.classmate.domain.dto.SearchSubjectTransfer;
import com.quokka.classmate.domain.dto.SubjectResponseDto;
import com.quokka.classmate.domain.repository.SearchSubjectRepositoryImpl;
import com.quokka.classmate.domain.repository.ElasticCommonSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ElasticSubjectServiceImpl implements ElasticSubjectService {

    private final SearchSubjectRepositoryImpl searchSubjectRepositoryImpl;
    private final ElasticCommonSubjectRepository elasticCommonSubjectRepository;

    /**
     * {@inheritDoc}
     * */
    @Override
    public Page<SubjectResponseDto> searchSubjectByTitle(Pageable pageable, String title) {
        Page<SearchSubject> searchSubjects = searchSubjectRepositoryImpl.searchSubjectsByTitle(title, pageable);
//        Page<SearchSubject> searchSubjects = elasticCommonSubjectRepository.findAllByTitleContaining(title, pageable);

        List<SubjectResponseDto> response = searchSubjects.stream()
                .map(SearchSubjectTransfer::fromDocument).collect(Collectors.toList());

        //content pageable, total
        return new PageImpl<>(response, pageable, searchSubjects.getTotalElements());
    }

    @Override
    public Page<SubjectResponseDto> searchNativeQuery(String title, int pageNumber, int pageSize) {

        Page<SearchSubject> searchSubjects = searchSubjectRepositoryImpl.searchSubjectsByTitleWithCursor(title, pageNumber, pageSize);

        List<SubjectResponseDto> response = searchSubjects.stream()
                .map(SearchSubjectTransfer::fromDocument).collect(Collectors.toList());


        //content pageable, total
        return new PageImpl<>(response, searchSubjects.getPageable(), searchSubjects.getTotalElements());

    }

}