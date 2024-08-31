package com.backend.spring.mapper;

import com.backend.spring.entity.QuestionGroup;
import com.backend.spring.payload.response.QuestionGroupResponse;

public class QuestionGroupMapper {
    public static QuestionGroupResponse mapFromEntityToResponse(QuestionGroup questionGroup) {
        if(questionGroup == null) {
            return null;
        }

        return new QuestionGroupResponse(
                questionGroup.getGroupId(),
                questionGroup.getGroupImage(),
                questionGroup.getGroupScript(),
                questionGroup.getGroupAudio(),
                questionGroup.getGroupPassage(),
                questionGroup.getGroupText()
        );

    }
}
