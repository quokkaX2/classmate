package com.quokka.classmate.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
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

    public Student(String email, String password, String name, Integer currentCredit) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.currentCredit = currentCredit;
        this.role = Role.USER; // 회원 권한
    }
}