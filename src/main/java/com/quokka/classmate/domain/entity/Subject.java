package com.quokka.classmate.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long id;

    @Column(nullable = false, unique = true, name = "subject_name")
    private String name;

    @Column(nullable = false, name = "limit_count")
    private Integer limitCount;

    @Column(nullable = false, name = "time")
    private Integer time;

    @Column(nullable = false, name = "credit")
    private Integer credit;
}
