package com.quokka.classmate.service;

import com.quokka.classmate.global.exception.ApiResponseDto;
import com.quokka.classmate.domain.dto.CartResponseDto;
import com.quokka.classmate.domain.entity.RegisteredSubject;
import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.domain.entity.Subject;
import com.quokka.classmate.global.constant.GlobalVariables;
import com.quokka.classmate.global.security.UserDetailsImpl;
import com.quokka.classmate.repository.RegisteredSubjectRepository;
import com.quokka.classmate.repository.StudentRepository;
import com.quokka.classmate.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
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
    public void createRegisteredSubject(
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
            throw new IllegalArgumentException("이미 장바구니에 담긴 과목입니다.");
        }

        registeredSubjectRepository.save(new RegisteredSubject(student, subject));
    }

    // 장바구니에서 과목 삭제
    // 등록 과목 자체의 아이디로 처리할 것인지, 혹은 학생과 과목의 정보 기반으로 처리할 것인지 논의 필요
    // 우선은 후자의 형태로 구현. 추후 논의사항 및 html 의 처리 여하에 따라서 수정 예정
    @Transactional
    public void deleteRegisteredSubject(
            Long subjectId, UserDetailsImpl userDetails) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new IllegalArgumentException("삭제하려는 강의가 존재하지 않습니다.")
        );

        Student student = studentRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 회원 정보입니다.")
        );

        Optional<RegisteredSubject> optionalRegisteredSubject =
                registeredSubjectRepository.findByStudentIdAndSubjectId(userDetails.getUser().getId(), subjectId);

        // 존재하지 않는 과목을 삭제할 수 없도록 예외처리를 해야 한다
        if (optionalRegisteredSubject.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 과목입니다.");
        }

        registeredSubjectRepository.deleteById(optionalRegisteredSubject.get().getId());
    }

    // 장바구니에 담은 과목 --> '수강 신청'
    @Transactional
    public void registrationSubject(Long subjectId, UserDetailsImpl userDetails) {

        Student student = studentRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 회원 정보입니다."));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("신청하려는 강의가 존재하지 않습니다."));

        // 장바구니 목록에서 해당하는 데이터 찾기
        RegisteredSubject registeredSubject =
                registeredSubjectRepository.findByStudentIdAndSubjectId(userDetails.getUser().getId(), subjectId)
                        .orElseThrow(() -> new IllegalArgumentException("신청하려는 강의가 장바구니에 존재하지 않거나, 유효한 회원이 아닙니다."));

        // 수강 신청이 이미 완료되었는지 확인
        if (registeredSubject.isRegistered()) {
            throw new IllegalArgumentException("이미 수강신청이 완료된 과목입니다.");
        }

        // 수강 신청 가능한 학점을 초과하는지 확인한다.
        Integer subjectCredit = subject.getCredit();
        if (student.getCurrentCredit() + subjectCredit > GlobalVariables.MAX_CREDIT) {
            throw new IllegalArgumentException("신청 가능한 학점을 초과 하였습니다.");
        }

        // 과목의 수강 가능 여부 확인
        if (subject.getLimitCount() <= 0) {
            throw new IllegalArgumentException("수강 인원이 다 찼습니다.");
        }

        // 과목 정보 에서, 수강 가능 count - 1 해준다.
//        int updated = subjectRepository.decrementLimitCountById(subjectId);
//        if (updated == 0) {
//            throw new IllegalStateException("Unable to register due to limit count.");
//        }
        boolean updated = subject.cutCount();
        if (!updated) {
            throw new IllegalStateException("Unable to register due to limit count.");
        }
        log.info("신청 가능한 남은 과목 수: {}", subject.getLimitCount());

        // 수강 신청 성공 시, 상태값 true로 변경 & 학생 학점 갱신
        registeredSubject.changeRegisterStatus(); // 상태값 true로 변경
        student.plusCurrentCredit(subject.getCredit());
    }

    public List<CartResponseDto> getRegisteredSubjects(Student user) {
        return registeredSubjectRepository.findByStudentIdAndIsRegisteredTrue(user.getId())
                .stream().map(CartResponseDto::new).toList();
    }

    @Transactional
    public void registerSubjectDirectly(Long subjectId, UserDetailsImpl userDetails) {
        Student student = studentRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 회원 정보입니다."));

        // 비관적 락을 사용하여 과목 정보 조회
        Subject subject = subjectRepository.findByIdForUpdate(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("신청하려는 강의가 존재하지 않습니다."));

        // 학생의 현재 학점 확인
        Integer subjectCredit = subject.getCredit();
        if (student.getCurrentCredit() + subjectCredit > GlobalVariables.MAX_CREDIT) {
            throw new IllegalArgumentException("신청 가능한 학점을 초과 하였습니다.");
        }
//
        // 과목의 수강 가능 여부 확인
        if (subject.getLimitCount() <= 0) {
            throw new IllegalArgumentException("수강 인원이 다 찼습니다.");
        }

        // 수강 신청 로직 (과목 수강 인원 감소, 학생 학점 증가)
        subject.cutCount(); // 과목의 수강 가능 인원 감소
        student.plusCurrentCredit(subjectCredit); // 학생의 학점 증가

        // 수강 신청 상태를 나타내는 새로운 RegisteredSubject 엔티티 저장
        registeredSubjectRepository.save(new RegisteredSubject(student, subject, true));
    }

}
