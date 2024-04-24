package com.quokka.classmate.domain.document;

import com.quokka.classmate.utility.WeekTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setting(settingPath = "/static/elastic/elastic-settings.json")
@Mapping(mappingPath = "/static/elastic/subject-mappings.json")
@Document(indexName = "subjects")
public class SearchSubject {
    @Id
    @Field(name = "subject_id", type = FieldType.Long)
    private Long id;

    @Field(name = "title", type = FieldType.Text)
    private String title;

    @Field(name = "limit_count", type = FieldType.Integer)
    private Integer limitCount;

    @Field(name = "time", type = FieldType.Integer)
    private Integer time;

    @Field(name = "credit", type = FieldType.Integer)
    private Integer credit;
    public SearchSubject(String title, Integer limitCount, Integer time, Integer credit) {
        this.title = title;
        this.limitCount = limitCount;
        this.time = time;
        this.credit = credit;
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
