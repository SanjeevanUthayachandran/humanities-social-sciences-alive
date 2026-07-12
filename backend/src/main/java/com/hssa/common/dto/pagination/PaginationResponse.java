package com.hssa.common.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Common response DTO for paginated data.
 * @param <T> the type of the content list elements.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> {

    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private boolean hasNext;
    private boolean hasPrevious;

    /**
     * Helper method to map a Spring Data Page to a PaginationResponse.
     * @param page the Spring Data Page object.
     * @param <T> the type of the content.
     * @return a constructed PaginationResponse.
     */
    public static <T> PaginationResponse<T> fromPage(Page<T> page) {
        return PaginationResponse.<T>builder()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
