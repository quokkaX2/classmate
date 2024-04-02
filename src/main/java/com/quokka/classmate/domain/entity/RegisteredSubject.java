package com.quokka.classmate.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "registered_subjects")
public class RegisteredSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registered_subject_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    // JPA 의 boolean 타입은 MySQL 에서 bit 타입으로 됨
    // 그럼에도 bit 대신 대부분 tinyint 를 쓰는 이유는,
    // java Mysql 드라이버에서 COALESCE() 메서드에서 bit 가 사용될 때, 2진 데이터로 사용하기 때문에 따로 캐스팅을 해야한다.
    // bit 라는 것을 프로그램마다 의미하는게 약간 다른 경우가 있어서, 이런 경우에 같다 착각하고 시행착오를 겪을 수 있다.
    @Column(columnDefinition = "TINYINT(1)") // TINYINT(1)의 경우 0~255
    private boolean isRegistered; // 0 - false, 1 - true

    // boolean 필드는 false 가 초기값
    public RegisteredSubject(Student student, Subject subject) {
        this.student = student;
        this.subject = subject;
    }

    // 수강 신청 상태 변경. false -> true
    public void changeRegisterStatus() {
        this.isRegistered = true;
    }
}
