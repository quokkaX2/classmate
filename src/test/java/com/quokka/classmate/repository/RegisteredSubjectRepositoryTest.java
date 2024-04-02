package com.quokka.classmate.repository;

import com.quokka.classmate.domain.entity.RegisteredSubject;
import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.domain.entity.Subject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // 테스트 종료 후, 트랜잭션 롤백이 돼서 실제 DB에 저장되지 않음
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// 테스트용 데이터베이스를 사용하는 것이 아닌, 실제 연동된 데이터베이스를 기반으로 테스트하겠다는 의미
// 얘가 추후에 어떤 영향을 끼칠 지는 추가 확인 더 필요
class RegisteredSubjectRepositoryTest {

    @Autowired
    private RegisteredSubjectRepository registeredSubjectRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;


    @Test
    @DisplayName("학생 아이디와 과목 아이디를 기반으로 장바구니 등록 과목을 조회할 수 있다.")
    void findByStudentAndSubject() {
        // given
        Student mockStudent = new Student("mock@test.com", "password", "이름", 18);
        Subject mockSubject = new Subject("과목", 30, 3, 3);

        studentRepository.save(mockStudent);
        subjectRepository.save(mockSubject);

        registeredSubjectRepository.save(new RegisteredSubject(mockStudent, mockSubject));

        // when
        Optional<RegisteredSubject> optionalRegisteredSubject =
                registeredSubjectRepository.findByStudentIdAndSubjectId(mockStudent.getId(), mockSubject.getId());

        // then
        assertTrue(optionalRegisteredSubject.isPresent()); // 조회 시, 존재해야 함

        // 조회 학생의 이름 및 조회 과목의 이름 비교
        RegisteredSubject foundRegisteredSubject = optionalRegisteredSubject.get();
        assertEquals(mockStudent.getName(), foundRegisteredSubject.getStudent().getName());
        assertEquals(mockSubject.getTitle(), foundRegisteredSubject.getSubject().getTitle());
    }
}

// 삭제 테스트는 매개값을 어떤 것으로 두냐에 따라서 테스트 코드가 달라질 것으로 생각돼서 우선은 보류