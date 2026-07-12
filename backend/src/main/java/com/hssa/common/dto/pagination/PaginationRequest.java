package com.hssa.common.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Common request DTO for paginated endpoints.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

    /**
     * The zero-based page index. Default is 0.
     */
    private int page = 0;

    /**
     * The size of the page to be returned. Default is 20.
     */
    private int size = 20;

    /**
     * The field to sort by. Can be null.
     */
    private String sort;

    /**
     * The sort direction (ASC or DESC).
     */
    private String direction = "ASC";
}
