package com.quokka.classmate.domain.redisEnum;

import lombok.Getter;

@Getter
public enum Redis {

    CLOSED(Redis.Category.CLOSED),
    ADD_QUEUE(Redis.Category.ADD_QUEUE),
    SUCCESS(Redis.Category.SUCCESS),
    FAIL(Redis.Category.FAIL);


    private final String category;

    Redis(String category) {
        this.category = category;
    }

    public String getCategory() {
            return this.category;

    }

    public static class Category {
        public static final String CLOSED = "-- 신청 인원이 마감되었습니다. --";
        public static final String ADD_QUEUE = "queue";
        public static final String SUCCESS = "success";
        public static final String FAIL = "fail";

    }
}
