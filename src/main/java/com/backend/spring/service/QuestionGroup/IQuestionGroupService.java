package com.backend.spring.service.QuestionGroup;

import com.backend.spring.entity.QuestionGroup;
import com.backend.spring.payload.request.QuestionGroupRequest;

import java.io.IOException;
import java.util.List;

public interface IQuestionGroupService {
    List<QuestionGroup> getAllQuestionGroups();
    QuestionGroup getQuestionGroupById(Integer groupId);
    boolean deleteQuestionGroup(Integer groupId) throws IOException;
    QuestionGroup createQuestionGroup(QuestionGroupRequest questionGroupRequest) throws IOException ;
    QuestionGroup updateQuestionGroup(QuestionGroupRequest questionGroupRequest) throws IOException;


}
