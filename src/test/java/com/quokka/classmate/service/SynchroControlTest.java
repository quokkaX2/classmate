package com.quokka.classmate.service;

import com.quokka.classmate.domain.entity.Student;
import com.quokka.classmate.domain.entity.Subject;
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
    private RegisteredSubjectRepository registeredSubjectRepository;

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

//    @AfterEach
//    void after() {
//        registeredSubjectRepository.deleteAllInBatch();
//        subjectRepository.deleteAllInBatch();
//        studentRepository.deleteAllInBatch();
//    }

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

//    @Test
//    @DisplayName("낙관적 락을 기반으로 한 동시성 제어")
//    void controlIssueByOptimisticLock() throws InterruptedException {
//
//        // when
//
//        int numbers = PEOPLE;
//
//        Subject subject = subjectRepository.findById(savedSubjectId).get();
//        List<Student> students = studentRepository.findAll();
//
//        ExecutorService executorService = Executors.newFixedThreadPool(numbers);
//        CountDownLatch latch = new CountDownLatch(numbers);
//        AtomicInteger count = new AtomicInteger(0);
//
//        for (Student student: students) {
//            executorService.submit(() -> {
//                try {
//                    optimisticLockFacade.registerByOptimisticLock(subject.getId(), student.getId());
//                    count.incrementAndGet();
//                    log.info("수강신청 성공한 학생: {}", student.getName());
//                } catch (Exception e) {
//                    log.error(e.getMessage());
//                    log.info("수강신청 실패한 학생: {}", student.getName());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        // then
//
//        assertEquals(LIMIT, count.get());
//    }

//    @Test
//    @DisplayName("lettuce 를 기반으로 한 동시성 제어")
//    void controlIssueByPessimisticLettuce() throws InterruptedException {
//
//        // when
//
//        int numbers = PEOPLE;
//
//        Subject subject = subjectRepository.findById(savedSubjectId).get();
//        List<Student> students = studentRepository.findAll();
//
//        ExecutorService executorService = Executors.newFixedThreadPool(numbers);
//        CountDownLatch latch = new CountDownLatch(numbers);
//        AtomicInteger count = new AtomicInteger(0);
//
//        for (Student student: students) {
//            executorService.submit(() -> {
//                try {
//                    registeredSubjectService.registrationSubjectByLettuce(subject.getId(), student.getId());
//                    count.incrementAndGet();
//                    log.info("수강신청 성공한 학생: {}", student.getName());
//                } catch (Exception e) {
//                    log.error(e.getMessage());
//                    log.info("수강신청 실패한 학생: {}", student.getName());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        // then
//
//        assertEquals(LIMIT, count.get());
//    }


//    @BeforeEach
//    @DisplayName("가짜 학생 100명과 가짜 과목 1개(제한 인원 10명) 설정")
//    void before() {
//        // given
//
//        Subject subject = new Subject("과목", LIMIT, 1, 3);
//        subjectRepository.save(subject);
//
//        Subject findSubject = subjectRepository.findByTitle("과목").get();
//        log.info("저장된 과목 : " + findSubject.getTitle());
//
//        for (int i = 1; i < PEOPLE + 1; i++) {
//            Student student = new Student(
//                    "student_" + i + "@test.com",
//                    "test",
//                    "student_" + i,
//                    0);
//
//            studentRepository.save(student);
//            Student findStudent = studentRepository.findByEmail("student_" + i + "@test.com").get();
//
//            registeredSubjectService.createRegisteredSubject(subject.getId(), student.getId());
//
//            log.info("생성된 학생: " + findStudent.getName());
//        }
//
//        log.info("등록된 강의들: " + registeredSubjectRepository.findAll().
//                stream().map(RegisteredSubject::isRegistered).toList());
//
//    }
//
//    @AfterEach
//    @DisplayName("테스트에 쓰인 가짜 데이터 전부 삭제")
//    void after() {
//        registeredSubjectRepository.deleteAll();
//        studentRepository.deleteAll();
//        subjectRepository.deleteAll();
//    }
//
//    @Test
////    @Rollback
//    @DisplayName("동시성 이슈 발생 확인")
//    void checkSynchroIssueTest() throws InterruptedException {
//        // when
//
//        Subject findSubject = subjectRepository.findByTitle("과목").get();
//        log.info("저장된 과목 : " + findSubject.getTitle());
//
//        int threadCount = PEOPLE;
//        ExecutorService executorService = Executors.newFixedThreadPool(100);
//        CountDownLatch latch = new CountDownLatch(threadCount); // 다른 스레드 작업 완료될 때까지 기다리게 함
//
//        List<Student> studentList = studentRepository.findAll();
//        AtomicInteger count = new AtomicInteger();
//
//        for (Student student: studentList) {
//            executorService.submit(() -> {
//                try {
//                    Long studentId = student.getId();
//                    Long subjectId = findSubject.getId();
//                    registeredSubjectService.registrationSubject(subjectId, studentId);
//                    count.addAndGet(1);
//
//                    log.info("학생 이름: " + student.getName());
//                    log.info("남은 인원: " + findSubject.getLimitCount());
//                    log.info("학생 학점: " + student.getCurrentCredit());
//                    log.info("수강신청한 과목: " + student.getRegisteredSubjects().stream().
//                            map(x -> x.getSubject().getTitle()).toList());
//
//                } catch(Exception ex) {
//                    log.error("신청 못 한 친구: " + student.getName());
//                    log.error(ex.getMessage());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        List<Boolean> successList =
//                registeredSubjectRepository.findAll().
//                        stream().map(RegisteredSubject::isRegistered).
//                        toList();
//
////        int count = (int) successList.stream().filter(b -> b).count();
//        log.info("성공 리스트: " + successList);
//        log.info("신청 개수: " + count);
//
//        // then
//
//        assertThat(count.get()).isNotEqualTo(LIMIT);
//    }
//
//    @Test
////    @Rollback
//    @DisplayName("lettuce 기반 동시성 이슈 제어 검증") // 어라... 동시성 제어가 안 된다....
//    void controlIssueByLettuce() throws InterruptedException {
//        // when
//
//        Subject findSubject = subjectRepository.findByTitle("과목").get();
//        log.info("저장된 과목 : " + findSubject.getTitle());
//
//        int threadCount = PEOPLE;
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
//        CountDownLatch latch = new CountDownLatch(threadCount); // 다른 스레드 작업 완료될 때까지 기다리게 함
//
//        List<Student> studentList = studentRepository.findAll();
//
//        for (Student student: studentList) {
//            executorService.submit(() -> {
//                try {
//                    student.plusCurrentCredit(findSubject.getCredit());
//
//                    Long studentId = student.getId();
//                    Long subjectId = findSubject.getId();
//                    lettuceLockFacade.registrationSubject(subjectId, studentId);
//
//                    log.info("학생 이름: " + student.getName());
//                    log.info("학생 학점: " + student.getCurrentCredit());
//                    log.info("수강신청한 과목: " + student.getRegisteredSubjects().stream().
//                            map(x -> x.getSubject().getTitle()).toList());
//
//                } catch(Exception ex) {
//                    log.error("신청 못 한 친구: " + student.getName());
//                    log.error(ex.getMessage());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        List<Boolean> successList =
//                registeredSubjectRepository.findAll().
//                        stream().map(RegisteredSubject::isRegistered).
//                        toList();
//
//        int count = (int) successList.stream().filter(b -> b).count();
//        log.info("성공 리스트: " + successList);
//        log.info("신청 개수: " + count);
//
//        // then
//
//        assertThat(count).isEqualTo(LIMIT);
//    }
}

