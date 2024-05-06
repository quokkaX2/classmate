package com.quokka.classmate.domain.utility;

public class WeekTime {

    // 요일과 시간대를 같이 출력하기 위한 간이 모듈려 연산식 + 배열 인덱스
    public static String calculateWeekTime(Integer time) {

        String[] days = {"월", "화", "수", "목", "금"}; // 요일
        String[] times = {
                "09:00 ~ 10:00",
                "10:00 ~ 11:00",
                "11:00 ~ 12:00",
                "12:00 ~ 13:00",
                "13:00 ~ 14:00",
                "14:00 ~ 15:00",
                "15:00 ~ 16:00",
                "16:00 ~ 17:00",
                "17:00 ~ 18:00"
        }; // 강의 시간대

        String day = days[time / 10];
        String classTime = times[time % 10 - 1]; // 인덱스는 0부터 시작하므로

        return day + "요일 " + classTime;
    }
}
