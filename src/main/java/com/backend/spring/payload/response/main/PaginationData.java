package com.backend.spring.payload.response.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationData {
    private int totalPage;

    private int totalElement;

    private int pageNumber;

    private int pageSize;
}
