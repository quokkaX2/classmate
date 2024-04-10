package com.quokka.classmate.service;

import com.quokka.classmate.domain.entity.RegisteredSubject;
import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.domain.entity.Subject;
import com.quokka.classmate.global.security.UserDetailsImpl;
import com.quokka.classmate.repository.RegisteredSubjectRepository;
import com.quokka.classmate.repository.StudentRepository;
import com.quokka.classmate.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
@ActiveProfiles("test")
class RegisteredSubjectServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(RegisteredSubjectServiceTest.class);

    @Autowired
    private RegisteredSubjectService registeredSubjectService;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RegisteredSubjectRepository registeredSubjectRepository;

    private Long savedSubjectId; // 저장된 Subject의 ID를 저장할 변수
    @BeforeEach
    void setUp() {

        registeredSubjectRepository.deleteAllInBatch();
        subjectRepository.deleteAllInBatch();
        studentRepository.deleteAllInBatch();

        // 테스트 데이터 준비
        Subject subject =  subjectRepository.save(new Subject("test 1", 30, 31, 2));
        savedSubjectId = subject.getId();

        List<Student> students = IntStream.range(0, 100)
                .mapToObj(i -> new Student("student" + i + "@example.com", "qwer1234", "Student " + i, 0))
                .map(studentRepository::save) // 학생 데이터 저장
                .peek(student -> {
                    RegisteredSubject registeredSubject = new RegisteredSubject(student, subject);
                    registeredSubjectRepository.save(registeredSubject); // 각 학생별로 과목을 장바구니에 담기
                })
                .collect(Collectors.toList());
    }
//    @Test
//    void testRegistrationSubjectConcurrency() throws InterruptedException {
//        int numberOfThreads = 100; // 동시 실행할 스레드 수
//
//        Subject subject = subjectRepository.findById(savedSubjectId)
//                .orElseThrow(() -> new RuntimeException("Subject not found"));
//        List<Student> students = studentRepository.findAll();
//
//        CountDownLatch latch = new CountDownLatch(numberOfThreads);
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//        AtomicInteger successCount = new AtomicInteger(0);
//
//        for (int i = 0; i < numberOfThreads; i++) {
//            Student student = students.get(i % students.size()); // 학생 선택
//            UserDetailsImpl userDetails = new UserDetailsImpl(student);
//
//            executorService.submit(() -> {
//                try {
//                    registeredSubjectService.registrationSubject(subject.getId(), userDetails);
//                    successCount.incrementAndGet();
//                } catch (Exception e) {
//                    System.err.println("Exception: " + e.getMessage());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await(); // 모든 스레드의 작업이 완료될 때까지 대기
//        executorService.shutdown();
//        executorService.awaitTermination(1, TimeUnit.MINUTES);
//
//        // 데이터베이스에서 실제로 등록된 과목의 수 카운트
//        long registeredSubjectCount = registeredSubjectRepository.countBySubjectId(savedSubjectId);
//
//        // 성공한 수강 신청 횟수와 데이터베이스에 등록된 과목의 수를 비교
//        assertEquals(registeredSubjectCount, successCount.get(),
//                "The number of successfully registered subjects does not match the expected count.");
//
//        // 성공한 수강 신청 횟수 확인 출력 (이 부분은 선택적)
//        System.out.println("Successful registrations: " + successCount.get() + ", Registered subjects in DB: " + registeredSubjectCount);
//    }

    @Test
    void testConcurrentSubjectRegistration() throws InterruptedException {
        final int numberOfStudents = 100;
        logger.info("Starting concurrent subject registration test with {} students", numberOfStudents);

        Subject subject = subjectRepository.findById(savedSubjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        List<Student> students = studentRepository.findAll();

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfStudents);
        CountDownLatch latch = new CountDownLatch(numberOfStudents);
        AtomicInteger successfulRegistrations = new AtomicInteger(0);


        for (int i = 0; i < numberOfStudents; i++) {
            // 각각의 스레드에서 수강 신청 로직 실행
            Student student = students.get(i % students.size()); // 학생 선택
            UserDetailsImpl userDetails = new UserDetailsImpl(student);
            executorService.submit(() -> {
                try {
                    registeredSubjectService.registerSubjectDirectly(subject.getId(), userDetails);
                    successfulRegistrations.incrementAndGet();
                    logger.info("Registration successful for student {}", userDetails.getUsername());

                } catch (Exception e) {
                    logger.error("Registration failed for student {}: {}", userDetails.getUsername(), e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();  // 모든 스레드의 작업이 끝날 때까지 대기
        executorService.shutdown();
        logger.info("Test completed. Successful registrations: {}", successfulRegistrations.get());

        // 성공적으로 수강 등록된 학생의 수가 과목 정원과 일치하는지 확인
        assertEquals(30, successfulRegistrations.get());
    }

}

