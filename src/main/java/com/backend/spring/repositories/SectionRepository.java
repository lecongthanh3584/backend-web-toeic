package com.backend.spring.repositories;
import com.backend.spring.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    boolean existsByName(String name);

    boolean existsByNameAndSectionIdNot(String name, Integer sectionId);

}