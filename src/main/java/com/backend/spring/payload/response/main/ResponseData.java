package com.backend.spring.payload.response.main;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> {
    private int status;

    private String message;

    private PaginationData paginationData;

    private T data;

    //CREATE, UPDATE, DELETE
    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }

    //GET
    public ResponseData(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    //GET
    public ResponseData(int status, String message, PaginationData paginationData, T data) {
        this.status = status;
        this.message = message;
        this.paginationData = paginationData;
        this.data = data;
    }
}
