package com.backend.spring.services.Section;

import com.backend.spring.entities.Section;
import com.backend.spring.payload.request.SectionRequest;

import java.io.IOException;
import java.util.List;

public interface ISectionService {
    Section createSection(SectionRequest sectionRequest) throws IOException;
    List<Section> getAllSections();
    Section getSectionById(Integer id);
    Section updateSection(SectionRequest sectionRequest) throws IOException;
    Section updateSectionStatus(Integer id, Integer newStatus);
    boolean deleteSection(Integer id) throws IOException;
    String getSectionNameById(Integer sectionId);
    boolean isSectionNameExists(String name);
    boolean isSectionNameExistsAndIdNot(String name, Integer sectionId);

}
