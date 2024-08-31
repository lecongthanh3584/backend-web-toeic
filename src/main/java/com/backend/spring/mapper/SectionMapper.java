package com.backend.spring.mapper;

import com.backend.spring.entity.Section;
import com.backend.spring.payload.response.SectionResponse;

public class SectionMapper {
    public static SectionResponse mapFromEntityToResponse(Section section) {
        if(section == null) {
            return null;
        }

        return new SectionResponse(
                section.getId(),
                section.getName(),
                section.getStatus(),
                section.getImage(),
                section.getCreatedAt(),
                section.getUpdatedAt(),
                section.getDescription(),
                section.getType()
        );
    }

}
