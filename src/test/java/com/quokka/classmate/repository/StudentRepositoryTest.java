//package com.quokka.classmate.repository;
//
//import com.quokka.classmate.domain.entity.Student;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@DataJpaTest
//@ActiveProfiles("test")
//public class StudentRepositoryTest {
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    @Test
//    @DisplayName("이메일로 학생 찾기 - 성공")
//    void findByEmail_success() {
//        String email = "test@example.com";
//        Student student = new Student(
//                email,
//                "password",
//                "Test Student",
//                0
//        );
//        studentRepository.save(student);
//
//        Optional<Student> found = studentRepository.findByEmail(email);
//
//        assertThat(found).isPresent();
//        assertThat(found.get().getEmail()).isEqualTo(email);
//    }
//
//    @Test
//    @DisplayName("이메일로 학생 찾기 - 실패: 이메일이 존재하지 않음")
//    void findByEmail_failure_emailDoesNotExist() {
//        String email = "nonexistent@example.com";
//
//        Optional<Student> found = studentRepository.findByEmail(email);
//
//        assertThat(found).isNotPresent();
//    }
//
//    @Test
//    @DisplayName("학생 저장- 실패: 이메일이 중복됨")
//    void save_failure_emailDuplicated() {
//        String email = "duplicate@example.com";
//        Student student1 = new Student(
//                email,
//                "password1",
//                "Student 1",
//                0
//        );
//        studentRepository.save(student1);
//
//        Student student2 = new Student(
//                email,
//                "password1",
//                "Student 2",
//                0
//        );
//
//        assertThrows(DataIntegrityViolationException.class, () -> {
//            studentRepository.save(student2);
//        });
//    }
//}
