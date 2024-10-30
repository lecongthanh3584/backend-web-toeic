package com.backend.spring.repositories;
import com.backend.spring.entities.FreeMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface FreeMaterialRepository extends JpaRepository<FreeMaterial, Integer> {

    @Query(value = "SELECT * FROM free_material fm " +
            "WHERE (fm.file_name LIKE CONCAT('%', ?1, '%') OR fm.title LIKE CONCAT('%', ?1, '%')) " +
            "AND fm.deleted_at IS NULL", nativeQuery = true)
    Page<FreeMaterial> findFreeMaterialWithoutStatus(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM free_material fm " +
            "WHERE (fm.file_name LIKE CONCAT('%', ?2, '%') OR fm.title LIKE CONCAT('%', ?2, '%')) " +
            "AND fm.material_status = ?1 AND fm.deleted_at IS NULL", nativeQuery = true)
    Page<FreeMaterial> findFreeMaterialHaveStatus(Integer status, String keyword, Pageable pageable);
}