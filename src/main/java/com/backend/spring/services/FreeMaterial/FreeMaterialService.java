package com.backend.spring.services.FreeMaterial;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.entities.FreeMaterial;
import com.backend.spring.repositories.FreeMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class FreeMaterialService implements IFreeMaterialService {

    private final FreeMaterialRepository freeMaterialRepository;

    @Override
    @Transactional(readOnly = true)
     public Page<FreeMaterial> getAllFreeMaterials(Integer pageNumber, String keyword, String... sortBys) {

        List<Sort.Order> orders = getListSort(sortBys);

        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(orders));
        return freeMaterialRepository.findFreeMaterialWithoutStatus(keyword, pageable);
    }

    @Override
    public Page<FreeMaterial> getAllFreeMaterialsEnable(Integer pageNumber, String keyword) {
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "created_at"));

        return freeMaterialRepository.findFreeMaterialHaveStatus(EStatus.ACTIVATE.getValue(), keyword, pageable);
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

    private List<Sort.Order> getListSort(String... sortBys) {
        List<Sort.Order> orders = new ArrayList<>();

        for(String sortBy : sortBys) {
            String[] sort = sortBy.split(":"); //Tách từng phần để xác định xem là sắp xếp tăng dần hay giảm dần

            if (sort.length == 2) {
                String field = sort[0].trim();
                String direction = sort[1].trim();

                if (direction.equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, field));
                } else if (direction.equalsIgnoreCase("desc")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, field));
                }
            } else {
                throw new RuntimeException(MessageConstant.INVALID_PARAMETER);
            }
        }

        return orders;
    }

}
