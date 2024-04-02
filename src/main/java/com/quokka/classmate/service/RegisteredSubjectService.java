package com.quokka.classmate.service;

import com.quokka.classmate.domain.dto.CartResponseDto;
import com.quokka.classmate.domain.entity.RegisteredSubject;
import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.domain.entity.Subject;
import com.quokka.classmate.global.security.UserDetailsImpl;
import com.quokka.classmate.repository.RegisteredSubjectRepository;
import com.quokka.classmate.repository.StudentRepository;
import com.quokka.classmate.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegisteredSubjectService {

    private final RegisteredSubjectRepository registeredSubjectRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;

    // 장바구니에 담은 과목 전체 조회
    public List<CartResponseDto> findAll(Student student) {
        List<RegisteredSubject> registeredSubjects = registeredSubjectRepository.findAllByStudentId(student.getId());
        return registeredSubjects.stream()
                .map(CartResponseDto::new)
                .toList();
    }

    // 장바구니에 강의 담기(코드 최적화 x)
    @Transactional
    public ResponseEntity<String> createRegisteredSubject(
            Long subjectId, UserDetailsImpl userDetails) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new IllegalArgumentException("추가하려는 강의가 존재하지 않습니다.")
        );

        Student student = studentRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 회원 정보입니다.")
        );

        // 이미 신청된 과목은 신청할 수 없도록 예외처리를 해야 한다
        if (registeredSubjectRepository.findByStudentIdAndSubjectId(
                userDetails.getUser().getId(), subjectId).isPresent()) {
            throw new NullPointerException("이미 장바구니에 담긴 과목입니다.");
        }

        System.out.println("학생 이름: " + student.getName());
        System.out.println("과목 이름: " + subject.getTitle());

        registeredSubjectRepository.save(new RegisteredSubject(student, subject));

        return ResponseEntity.ok("장바구니에 담겼습니다.");
    }

    // 장바구니에서 과목 삭제
    // 등록 과목 자체의 아이디로 처리할 것인지, 혹은 학생과 과목의 정보 기반으로 처리할 것인지 논의 필요
    // 우선은 후자의 형태로 구현. 추후 논의사항 및 html 의 처리 여하에 따라서 수정 예정
    @Transactional
    public ResponseEntity<String> deleteRegisteredSubject(
            Long subjectId, UserDetailsImpl userDetails) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new IllegalArgumentException("추가하려는 강의가 존재하지 않습니다.")
        );

        Student student = studentRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 회원 정보입니다.")
        );

        Optional<RegisteredSubject> optionalRegisteredSubject =
                registeredSubjectRepository.findByStudentIdAndSubjectId(userDetails.getUser().getId(), subjectId);

        // 존재하지 않는 과목을 삭제할 수 없도록 예외처리를 해야 한다
        if (optionalRegisteredSubject.isEmpty()) {
            throw new NullPointerException("존재하지 않는 과목입니다.");
        }

        System.out.println("학생 이름: " + student.getName());
        System.out.println("과목 이름: " + subject.getTitle());

        registeredSubjectRepository.deleteById(optionalRegisteredSubject.get().getId());

        return ResponseEntity.ok("정상적으로 삭제됐습니다.");
    }
}
