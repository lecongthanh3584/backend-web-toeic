package com.backend.spring.services.FreeMaterial;

import com.backend.spring.enums.EStatus;
import com.backend.spring.entities.FreeMaterial;
import com.backend.spring.repositories.FreeMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backend.spring.payload.request.FreeMaterialRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class FreeMaterialService implements IFreeMaterialService {

    private final FreeMaterialRepository freeMaterialRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FreeMaterial> getAllFreeMaterials() {
        return freeMaterialRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public FreeMaterial getFreeMaterialById(Integer id) {
        return freeMaterialRepository.findById(id).orElse(null);
    }

    @Override
    public FreeMaterial createFreeMaterial(FreeMaterialRequest freeMaterialRequest) throws IOException {

        MultipartFile filePdf = freeMaterialRequest.getFilePdf();
        if (filePdf == null || filePdf.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn file để upload");
        }

        String pdfName = filePdf.getOriginalFilename();
        String pdfPath = "pdfs/";
        Path uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", pdfPath);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path file = uploadPath.resolve(pdfName);
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(filePdf.getBytes());
        }

        FreeMaterial freeMaterial = new FreeMaterial();
        freeMaterial.setTitle(freeMaterialRequest.getTitle());
        freeMaterial.setDescription(freeMaterialRequest.getDescription());
        freeMaterial.setFileName(pdfName);
        freeMaterial.setMaterialStatus(EStatus.ENABLE.getValue());
        freeMaterial.setCreatedAt(LocalDateTime.now());
        freeMaterial.setUpdatedAt(LocalDateTime.now());

        return freeMaterialRepository.save(freeMaterial);
    }

    @Override
    public FreeMaterial updateFreeMaterial(FreeMaterialRequest freeMaterialRequest) throws IOException {
        Optional<FreeMaterial> freeMaterialOptional = freeMaterialRepository.findById(freeMaterialRequest.getMaterialId());
        if(freeMaterialOptional.isEmpty()) {
            return null;
        }

        FreeMaterial existingFreeMaterial = freeMaterialOptional.get();
        existingFreeMaterial.setTitle(freeMaterialRequest.getTitle());
        existingFreeMaterial.setDescription(freeMaterialRequest.getDescription());
        existingFreeMaterial.setMaterialStatus(freeMaterialRequest.getMaterialStatus());
        existingFreeMaterial.setUpdatedAt(LocalDateTime.now());

        MultipartFile filePdf = freeMaterialRequest.getFilePdf();
        if (filePdf != null && !filePdf.isEmpty()) {
            String pdfName = filePdf.getOriginalFilename();
            String pdfPath = "pdfs/";
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", pdfPath);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Xóa PDF cũ
            String oldPdf = existingFreeMaterial.getFileName();
            if (oldPdf != null && !oldPdf.isEmpty()) {
                Path oldPdfFile = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", oldPdf);
                Files.deleteIfExists(oldPdfFile);
            }

            // Lưu PDF mới
            Path pdfFile = uploadPath.resolve(pdfName);
            try (OutputStream os = Files.newOutputStream(pdfFile)) {
                os.write(filePdf.getBytes());
            }
            existingFreeMaterial.setFileName(pdfName);
        }

        return freeMaterialRepository.save(existingFreeMaterial);
    }

    @Override
    public boolean updateFreeMaterialStatus(Integer freeMaterialId, Integer status) {
        Optional<FreeMaterial> freeMaterialOptional = freeMaterialRepository.findById(freeMaterialId);
        if(freeMaterialOptional.isEmpty()) {
            return false;
        }

        FreeMaterial freeMaterialUpdate = freeMaterialOptional.get();

        if(status.equals(EStatus.ENABLE.getValue())) {
            freeMaterialUpdate.setMaterialStatus(status);
        } else if(status.equals(EStatus.DISABLE.getValue())) {
            freeMaterialUpdate.setMaterialStatus(status);
        } else {
            throw new IllegalArgumentException("Invalid status value!");
        }

        freeMaterialUpdate.setUpdatedAt(LocalDateTime.now());

        freeMaterialRepository.save(freeMaterialUpdate);

        return true;
    }

    @Override
    public boolean deleteFreeMaterial(Integer id) throws IOException {
        Optional<FreeMaterial> freeMaterialOptional = freeMaterialRepository.findById(id);
        if(freeMaterialOptional.isEmpty()) {
            return false;
        }

        String oldPdf = freeMaterialOptional.get().getFileName(); //Xoá file material
        if (oldPdf != null && !oldPdf.isEmpty()) {
            Path oldPdfFile = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", oldPdf);
            Files.deleteIfExists(oldPdfFile);
        }

        freeMaterialRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotalFreeMaterials() {
        return freeMaterialRepository.count();
    }

}
