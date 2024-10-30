package com.backend.spring.services.FreeMaterial;

import com.backend.spring.entities.FreeMaterial;
import com.backend.spring.payload.request.FreeMaterialRequest;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface IFreeMaterialService {
    Page<FreeMaterial> getAllFreeMaterials(Integer pageNumber, String keyword, String... sortBys);
    Page<FreeMaterial> getAllFreeMaterialsEnable(Integer pageNumber, String keyword);
    FreeMaterial getFreeMaterialById(Integer id);
    FreeMaterial createFreeMaterial(FreeMaterialRequest freeMaterialRequest) throws IOException;
    FreeMaterial updateFreeMaterial(FreeMaterialRequest freeMaterialRequest) throws IOException;
    boolean updateFreeMaterialStatus(Integer freeMaterialId, Integer status);
    boolean deleteFreeMaterial(Integer id) throws IOException;
    long countTotalFreeMaterials();
}
