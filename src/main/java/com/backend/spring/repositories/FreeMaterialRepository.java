package com.backend.spring.repositories;
import com.backend.spring.entities.FreeMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FreeMaterialRepository extends JpaRepository<FreeMaterial, Integer> {

}