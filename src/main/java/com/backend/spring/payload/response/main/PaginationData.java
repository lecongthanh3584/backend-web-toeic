package com.backend.spring.payload.response.main;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationData {
    private int totalPage;

    private long totalElement;

    private int pageNumber;

    private int pageSize;
}
