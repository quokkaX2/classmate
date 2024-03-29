package com.quokka.classmate.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

    public Subject(String name, Integer limitCount, Integer time, Integer credit) {
        this.name = name;
        this.limitCount = limitCount;
        this.time = time;
        this.credit = credit;
    }

    // 제한인원 카운팅 엔티티 로직
    public void cutCount() throws IllegalArgumentException {
        if (this.limitCount == 0) {
            throw new IllegalArgumentException("수강 인원이 다 찼습니다");
        }

        this.limitCount--;
    }
}