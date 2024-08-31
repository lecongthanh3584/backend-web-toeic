package com.backend.spring.repository;
import com.backend.spring.entity.FreeMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FreeMaterialRepository extends JpaRepository<FreeMaterial, Integer> {

}