package com.backend.spring.mapper;

import com.backend.spring.entities.FreeMaterial;
import com.backend.spring.payload.response.FreeMaterialResponse;

public class FreeMaterialMapper {
    public static FreeMaterialResponse mapFromEntityToResponse(FreeMaterial freeMaterial) {
        if(freeMaterial == null) {
            return null;
        }

        return new FreeMaterialResponse(
                freeMaterial.getMaterialId(),
                freeMaterial.getTitle(),
                freeMaterial.getDescription(),
                freeMaterial.getFileName(),
                freeMaterial.getMaterialStatus(),
                freeMaterial.getCreatedAt(),
                freeMaterial.getUpdatedAt()
        );

    }
}
