package com.backend.spring.elastics.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "exams")
public class ExamDocument {
    @Id
    private Integer examId;

    @Field(type = FieldType.Text)
    private String examName;

    @Field(type = FieldType.Integer)
    private Integer examType;

    @Field(type = FieldType.Integer)
    private Integer examDuration;

    @Field(type = FieldType.Integer)
    private Integer examStatus;

    @Field(type = FieldType.Date)
    private String createdAt;

    @Field(type = FieldType.Date)
    private String updatedAt;
}
