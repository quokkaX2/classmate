package com.quokka.classmate.service;

import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final int MAX_CREDIT = 20;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // 수강 신청 가능 여부를 확인하는 메서드
    public boolean canRegister(Long studentId, Integer subjectCredit) {
        Student student = studentRepository.findById(studentId).orElseThrow(() ->
                new IllegalArgumentException("유효하지 않은 학생 ID입니다."));

        int totalCredit = student.getCurrentCredit() + subjectCredit;

        return totalCredit <= MAX_CREDIT;
    }

}
