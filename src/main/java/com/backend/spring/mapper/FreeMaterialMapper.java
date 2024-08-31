package com.backend.spring.mapper;

import com.backend.spring.entity.FreeMaterial;
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
                freeMaterial.getFilePdf(),
                freeMaterial.getMaterialStatus(),
                freeMaterial.getCreatedAt(),
                freeMaterial.getUpdatedAt()
        );

    }
}
