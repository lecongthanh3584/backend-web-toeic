package com.backend.spring.service.GrammarContentService;

import com.backend.spring.enums.EStatus;
import com.backend.spring.entity.Grammar;
import com.backend.spring.entity.GrammarContent;
import com.backend.spring.payload.request.GrammarContentRequest;
import com.backend.spring.repository.GrammarContentRepository;
import com.backend.spring.repository.GrammarRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class GrammarContentService implements IGrammarContentService {

    @Autowired
    private GrammarContentRepository grammarContentRepository;

    @Autowired
    private GrammarRepository grammarRepository;

    @Override
    public void uploadGrammarContentFromExcel(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        List<GrammarContent> grammarContents = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() == 0) {
                // Bỏ qua dòng tiêu đề (nếu có)
                continue;
            }

            GrammarContent grammarContent = new GrammarContent();

            grammarContent.setTitle(row.getCell(0).getStringCellValue());
            grammarContent.setContent(row.getCell(1).getStringCellValue());

            // Lấy grammar_id từ cột số 2 (chỉnh index cột tương ứng trong file Excel)
            Integer grammarId = (int) row.getCell(3).getNumericCellValue();
            Grammar grammar = new Grammar();
            grammar.setGrammarId(grammarId);

            grammarContent.setGrammar(grammar);
            grammarContent.setGrammarContentStatus(EStatus.ENABLE.getValue());
            grammarContent.setCreatedAt(LocalDateTime.now());
            grammarContent.setUpdatedAt(LocalDateTime.now());

            grammarContents.add(grammarContent);
        }

        grammarContentRepository.saveAll(grammarContents);
    }

    @Override
    public GrammarContent createGrammarContent(GrammarContentRequest grammarContentRequest) {
        Optional<Grammar> grammarOptional = grammarRepository.findById(grammarContentRequest.getGrammarId());
        if(grammarOptional.isEmpty()) {
            return null;
        }

        GrammarContent grammarContent = new GrammarContent();

        grammarContent.setGrammar(grammarOptional.get());
        grammarContent.setTitle(grammarContentRequest.getTitle());
        grammarContent.setContent(grammarContentRequest.getContent());
        grammarContent.setGrammarContentStatus(EStatus.ENABLE.getValue());

        grammarContent.setCreatedAt(LocalDateTime.now());
        grammarContent.setUpdatedAt(LocalDateTime.now());

        return grammarContentRepository.save(grammarContent);
    }

    @Override
    public GrammarContent updateGrammarContent(GrammarContentRequest grammarContentRequest) {
        Optional<GrammarContent> grammarContentOptional = grammarContentRepository.findById(grammarContentRequest.getContentId());
        Optional<Grammar> grammarOptional = grammarRepository.findById(grammarContentRequest.getGrammarId());

        if(grammarContentOptional.isEmpty() || grammarOptional.isEmpty()) {
            return null;
        }

        GrammarContent grammarContentUpdate = grammarContentOptional.get();

        grammarContentUpdate.setGrammar(grammarOptional.get());
        grammarContentUpdate.setTitle(grammarContentRequest.getTitle());
        grammarContentUpdate.setContent(grammarContentRequest.getContent());
        grammarContentUpdate.setGrammarContentStatus(grammarContentRequest.getGrammarContentStatus());

        grammarContentUpdate.setUpdatedAt(LocalDateTime.now());

        return grammarContentRepository.save(grammarContentUpdate);
    }

    @Override
    public GrammarContent updateGrammarContentStatus(Integer grammarContentId, Integer newStatus) {
        Optional<GrammarContent> grammarContentOptional = grammarContentRepository.findById(grammarContentId);
        if(grammarContentOptional.isEmpty()) {
            return null;
        }

        GrammarContent grammarContentUpdate = grammarContentOptional.get();
        if(newStatus.equals(EStatus.DISABLE.getValue())) {
            grammarContentUpdate.setGrammarContentStatus(newStatus);
        } else if(newStatus.equals(EStatus.ENABLE.getValue())) {
            grammarContentUpdate.setGrammarContentStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Invalid status value");
        }

        grammarContentUpdate.setUpdatedAt(LocalDateTime.now());

        return grammarContentRepository.save(grammarContentUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrammarContent> getAllGrammarContents() {
        return grammarContentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public GrammarContent getGrammarContentById(Integer id) {
        return grammarContentRepository.findById(id).orElse(null);
    }


    @Override
    public boolean deleteGrammarContent(Integer id) {
        Optional<GrammarContent> grammarContentOptional = grammarContentRepository.findById(id);
        if(grammarContentOptional.isEmpty()) {
            return false;
        }

        grammarContentRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrammarContent> getGrammarContentsByGrammarId(Integer grammarId) {
        Optional<Grammar> grammarOptional = grammarRepository.findById(grammarId);
        if(grammarOptional.isEmpty()) {
            return Collections.emptyList();
        }

        return grammarContentRepository.findByGrammar(grammarOptional.get());
    }
}
