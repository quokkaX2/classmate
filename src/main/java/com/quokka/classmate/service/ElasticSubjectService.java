package com.quokka.classmate.service;

import com.quokka.classmate.domain.dto.SubjectResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ElasticSubjectService {

    Page<SubjectResponseDto> searchSubjectByTitle(Pageable pageable, String title);

    //    @Override
//    public Page<SubjectResponseDto> searchSubjectByTitle(Pageable pageable, String title) {
//        Page<SearchSubject> searchSubjects = searchSubjectRepositoryImpl.searchSubjectsByTitle(title, pageable);
////        Page<SearchSubject> searchSubjects = elasticCommonSubjectRepository.findAllByTitleContaining(title, pageable);
//
//        List<SubjectResponseDto> response = searchSubjects.stream()
//                .map(SearchSubjectTransfer::fromDocument).collect(Collectors.toList());
//
//        //content pageable, total
//        return new PageImpl<>(response, pageable, searchSubjects.getTotalElements());
//    }

    Page<SubjectResponseDto> searchNativeQuery(String title, int pageNumber, int pageSize);
}
