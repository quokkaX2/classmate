package com.quokka.classmate.service;

import com.quokka.classmate.domain.document.SearchSubject;
import com.quokka.classmate.domain.dto.CartResponseDto;
import com.quokka.classmate.domain.entity.RegisteredSubject;
import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.domain.entity.Subject;
import com.quokka.classmate.repository.*;
import lombok.RequiredArgsConstructor;
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
    private final RedisRepository redisRepository;
    private final SearchSubjectRepositoryImpl searchSubjectRepositoryImpl;

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
            Long subjectId, Long studentId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new IllegalArgumentException("추가하려는 강의가 존재하지 않습니다.")
        );

        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 회원 정보입니다.")
        );

        // 이미 신청된 과목은 신청할 수 없도록 예외처리를 해야 한다
        if (registeredSubjectRepository.findByStudentIdAndSubjectId(
                studentId, subjectId).isPresent()) {
            throw new IllegalArgumentException("이미 장바구니에 담긴 과목입니다.");
        }

        registeredSubjectRepository.save(new RegisteredSubject(student, subject));
    }

    // 장바구니에서 과목 삭제
    // 등록 과목 자체의 아이디로 처리할 것인지, 혹은 학생과 과목의 정보 기반으로 처리할 것인지 논의 필요
    // 우선은 후자의 형태로 구현. 추후 논의사항 및 html 의 처리 여하에 따라서 수정 예정
    @Transactional
    public void deleteRegisteredSubject(
            Long subjectId, Long studentId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new IllegalArgumentException("삭제하려는 강의가 존재하지 않습니다.")
        );

        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 회원 정보입니다.")
        );

        Optional<RegisteredSubject> optionalRegisteredSubject =
                registeredSubjectRepository.findByStudentIdAndSubjectId(studentId, subjectId);

        // 존재하지 않는 과목을 삭제할 수 없도록 예외처리를 해야 한다
        if (optionalRegisteredSubject.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 과목입니다.");
        }

        registeredSubjectRepository.deleteById(optionalRegisteredSubject.get().getId());
    }

    // 장바구니에 담은 과목 --> '수강 신청'
    @Transactional
    public void registrationSubject(Long subjectId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 회원 정보입니다."));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("신청하려는 강의가 존재하지 않습니다."));

        // 장바구니 목록에서 해당하는 데이터 찾기
        RegisteredSubject registeredSubject =
                registeredSubjectRepository.findByStudentIdAndSubjectId(studentId, subjectId)
                        .orElseThrow(() -> new IllegalArgumentException("신청하려는 강의가 장바구니에 존재하지 않거나, 유효한 회원이 아닙니다."));

        // 수강 신청이 이미 완료되었는지 확인
        if (registeredSubject.isRegistered()) {
            throw new IllegalArgumentException("이미 수강신청이 완료된 과목입니다.");
        }

        // 수강 신청 가능한 학점을 초과하는지 확인한다.
        Integer subjectCredit = subject.getCredit();
        student.checkCurrentCredit(subjectCredit);

        // 과목의 잔여 자리 수 감소
        subject.cutCount();

        // 수강 신청 성공 시, 상태값 true로 변경 & 학생 학점 갱신
        student.plusCurrentCredit(subjectCredit);
        registeredSubject.changeRegisterStatus(); // 상태값
    }

    public List<CartResponseDto> getRegisteredSubjects(Student user) {
        return registeredSubjectRepository.findByStudentIdAndIsRegisteredTrue(user.getId())
                .stream().map(CartResponseDto::new).toList();
    }

    // 비관적 락
    @Transactional
    public Subject registrationSubjectByPessimisticLock(Long subjectId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 회원 정보입니다."));

        // 비관적 락 적용
        Subject subject = subjectRepository.findByIdForPessimistic(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("신청하려는 강의가 존재하지 않습니다."));

//        Subject subject = subjectRepository.findById(subjectId)
//                .orElseThrow(() -> new IllegalArgumentException("신청하려는 강의가 존재하지 않습니다."));

        // 장바구니 목록에서 해당하는 데이터 찾기
        RegisteredSubject registeredSubject =
                registeredSubjectRepository.findByStudentIdAndSubjectId(studentId, subjectId)
                        .orElseThrow(() -> new IllegalArgumentException("신청하려는 강의가 장바구니에 존재하지 않거나, 유효한 회원이 아닙니다."));

        // 수강 신청이 이미 완료되었는지 확인
        if (registeredSubject.isRegistered()) {
            throw new IllegalArgumentException("이미 수강신청이 완료된 과목입니다.");
        }

        // 수강 신청 가능한 학점을 초과하는지 확인한다.
        Integer subjectCredit = subject.getCredit();
        student.checkCurrentCredit(subjectCredit);

        // 과목의 잔여 자리 수 감소
        subject.cutCount();
        subjectRepository.save(subject);

        // 수강 신청 성공 시, 상태값 true로 변경 & 학생 학점 갱신
        student.plusCurrentCredit(subjectCredit);
        registeredSubject.changeRegisterStatus(); // 상태값

        return subject;
    }

    // 수강 신청 취소
    @Transactional
    public void cancel(Long subjectId, Long studentId) {
        RegisteredSubject registeredSubject = registeredSubjectRepository.findByStudentIdAndSubjectId(studentId, subjectId)
                .orElseThrow(() -> new IllegalArgumentException("유효한 회원이 아니거나, 신청된 강의가 존재하지 않습니다."));

        Student student = registeredSubject.getStudent();
        Subject subject = registeredSubject.getSubject();

        // 수강 신청 정보 삭제
        registeredSubjectRepository.delete(registeredSubject);

        // 과목 잔여석 1개 증가
        subject.increaseLimitCount();

        // 학생 학점 감소
        student.decreaseCurrentCredit(subject.getCredit());

        // redis 잔여석 1개 증가
        redisRepository.incrementLeftSeatInRedis(subject.getId());
    }
}

