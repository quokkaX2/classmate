package com.quokka.classmate.domain.dto;


import com.quokka.classmate.domain.document.SearchSubject;
import com.quokka.classmate.domain.dto.SubjectResponseDto;

public class SearchSubjectTransfer {

    public static SubjectResponseDto fromDocument(SearchSubject subject) {
        if (subject == null) {
            return null;
        }

        return new SubjectResponseDto(
                subject.getId(),
                subject.getTitle(),
                subject.getLimitCount(),
                subject.getClassTime(),
                subject.getCredit()
        );
    }
}
