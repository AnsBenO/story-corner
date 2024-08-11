package com.ansbeno.books_service.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record PagedResultDto<T>(
            List<T> data,
            long totalElements,
            int pageNumber,
            int totalPages,
            boolean isFirst,
            boolean isLast,
            boolean hasNext,
            boolean hasPrevious) {
}
