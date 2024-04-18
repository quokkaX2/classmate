package com.quokka.classmate.domain.entity;

import com.quokka.classmate.global.constant.GlobalVariables;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
//@Table(name = "students", indexes = {
//        @Index(name = "idx_email", columnList = "email", unique = true)
//})
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "current_credit")
    private Integer currentCredit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "student")
    private List<RegisteredSubject> registeredSubjects = new ArrayList<>();

    public Student(String email, String password, String name, Integer currentCredit) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.currentCredit = currentCredit;
        this.role = Role.USER; // 회원 권한
    }

    // 수강 신청 시, 학생 학점을 더해준다.
    public void plusCurrentCredit(Integer subjectCredit) {
        this.currentCredit += subjectCredit;
    }

    // 수강 신청 시, 학점을 초과하는지 체크
    public void checkCurrentCredit(Integer subjectCredit) {
        if (this.currentCredit + subjectCredit > GlobalVariables.MAX_CREDIT) {
            throw new IllegalArgumentException("신청 가능한 학점을 초과 하였습니다.");
        }
    }
}