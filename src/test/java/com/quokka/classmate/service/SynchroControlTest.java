package com.quokka.classmate.service;

import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.domain.entity.Subject;
import com.quokka.classmate.facade.RedissonLockFacade;
import com.quokka.classmate.repository.RegisteredSubjectRepository;
import com.quokka.classmate.repository.StudentRepository;
import com.quokka.classmate.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
//@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
@ActiveProfiles("test")
public class SynchroControlTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RegisteredSubjectService registeredSubjectService;

    @Autowired
    private RedissonLockFacade redissonLockFacade;

    private final Integer PEOPLE = 100;
    private final Integer LIMIT = 200;
    private Long savedSubjectId;

    @BeforeEach
    @DisplayName("가짜 학생 100명과 가짜 과목 1개(제한 인원 10명) 설정")
    void before() {
        // 특정 엔티티 타입에 대해 일괄적으로 모든 레코드를 데이터베이스에서 삭제하는 기능

        Subject subject =  subjectRepository.save(new Subject("과목12345", LIMIT, 1, 3));
        savedSubjectId = subject.getId();
        log.info("준비된 과목: {}", subject.getTitle());

        for (int i = 1; i < PEOPLE + 1; i++) {
            Student student = new Student(
                    "student_" + i + "@test.com",
                    "test",
                    "student_" + i,
                    0);

            studentRepository.save(student);
            registeredSubjectService.createRegisteredSubject(subject.getId(), student.getId());
            log.info("생성된 학생: " + student.getName());
        }
    }

    @Test
    @DisplayName("동시성 이슈 발생 확인")
    void checkSynchroIssueTest() throws InterruptedException {

        // when

        int numbers = PEOPLE;

        Subject subject = subjectRepository.findById(savedSubjectId).get();
        List<Student> students = studentRepository.findAll();

        ExecutorService executorService = Executors.newFixedThreadPool(numbers);
        CountDownLatch latch = new CountDownLatch(numbers);

        for (Student student: students) {
            executorService.submit(() -> {
                try {
                    registeredSubjectService.registrationSubject(subject.getId(), student.getId());
                    log.info("수강신청 성공한 학생: {}", student.getName());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    log.info("수강신청 실패한 학생: {}", student.getName());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then

        log.info("과목 아이디: {}", savedSubjectId);
        Subject findSubject = subjectRepository.findById(savedSubjectId).get();
        assertNotEquals(LIMIT - PEOPLE, findSubject.getLimitCount());
    }

    @Test
    @DisplayName("비관적 락을 기반으로 한 동시성 제어")
    void controlIssueByPessimisticLock() throws InterruptedException {

        // when

        int numbers = PEOPLE;

        Subject subject = subjectRepository.findById(savedSubjectId).get();
        List<Student> students = studentRepository.findAll();

        ExecutorService executorService = Executors.newFixedThreadPool(numbers);
        CountDownLatch latch = new CountDownLatch(numbers);

        for (Student student: students) {
            executorService.submit(() -> {
                try {
                    registeredSubjectService.registrationSubjectByPessimisticLock(subject.getId(), student.getId());
                    log.info("수강신청 성공한 학생: {}", student.getName());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    log.info("수강신청 실패한 학생: {}", student.getName());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then

        log.info("과목 아이디: {}", savedSubjectId);
        Subject findSubject = subjectRepository.findById(savedSubjectId).get();
        assertEquals(LIMIT - PEOPLE, findSubject.getLimitCount());
    }

    @Test
    @DisplayName("redisson facade 기반으로 한 동시성 제어")
    void controlIssueByRedissonLock() throws InterruptedException {

        // when

        int numbers = PEOPLE;

        Subject subject = subjectRepository.findById(savedSubjectId).get();
        List<Student> students = studentRepository.findAll();

        ExecutorService executorService = Executors.newFixedThreadPool(numbers);
        CountDownLatch latch = new CountDownLatch(numbers);

        for (Student student: students) {
            executorService.submit(() -> {
                try {
                    redissonLockFacade.registerSubject(subject.getId(), student.getId());
                    log.info("수강신청 성공한 학생: {}", student.getName());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    log.info("수강신청 실패한 학생: {}", student.getName());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then

        log.info("과목 아이디: {}", savedSubjectId);
        Subject findSubject = subjectRepository.findById(savedSubjectId).get();
        assertEquals(LIMIT - PEOPLE, findSubject.getLimitCount());
    }
}

