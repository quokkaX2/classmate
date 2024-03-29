package com.quokka.classmate.service;

import com.quokka.classmate.repository.SubjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;


}
