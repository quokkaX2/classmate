package com.quokka.classmate.domain.entity;

import com.quokka.classmate.utility.WeekTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "subjects", indexes = {
        @Index(name = "idx_title", columnList = "title"),
//        @Index(name = "idx_subject_id", columnList = "subject_id")  // 추가된 인덱스
})
//@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "limit_count")
    private Integer limitCount;

    @Column(nullable = false, name = "time")
    private Integer time;

    @Column(nullable = false, name = "credit")
    private Integer credit;

    @Column(name = "modification_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationTime;

    public Subject(String title, Integer limitCount, Integer time, Integer credit) {
        this.title = title;
        this.limitCount = limitCount;
        this.time = time;
        this.credit = credit;
        this.updateTime();
    }

    @PreUpdate
    @PrePersist
    public void updateTime() {
        this.modificationTime = new Date();
    }
    // 제한인원 카운팅 엔티티 로직 - 성공시 true 반환
    public void cutCount() throws IllegalArgumentException {
        if (this.limitCount == 0) {
            throw new IllegalArgumentException("수강 인원이 다 찼습니다");
        }
        this.limitCount--;
    }

    // 강의시간요일 String 반환
    public String getClassTime() {
        return WeekTime.calculateWeekTime(this.time);
    }

    // 강의 잔여석 +1
    public void increaseLimitCount() {
        this.limitCount++;
    }
}
