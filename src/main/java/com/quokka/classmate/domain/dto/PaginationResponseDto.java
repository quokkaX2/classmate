package com.quokka.classmate.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PaginationResponseDto<T> {
    private final List<T> dataList;
    private final long totalDataCount;
    private final int currentPage;
    private final int totalPage;
    private final String nextCursor;
}
