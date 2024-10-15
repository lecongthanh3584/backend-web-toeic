package com.backend.spring.mapper;

import com.backend.spring.entities.Test;
import com.backend.spring.payload.response.TestResponse;

public class TestMapper {
    public static TestResponse MapFromEntityToResponse(Test test) {
        if(test == null) {
            return null;
        }

        return new TestResponse(
                test.getTestId(),
                test.getTestName(),
                test.getTestParticipants(),
                test.getTestStatus(),
                test.getCreatedAt(),
                test.getUpdatedAt(),
                test.getSection()
        );
    }
}
