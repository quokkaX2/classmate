package com.quokka.classmate.service;

import com.quokka.classmate.domain.dto.SubjectResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ElasticSubjectService {

    Page<SubjectResponseDto> searchSubjectByTitle(Pageable pageable, String title);
}
