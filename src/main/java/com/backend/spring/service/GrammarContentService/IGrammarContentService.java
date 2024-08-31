package com.backend.spring.service.GrammarContentService;

import com.backend.spring.entity.GrammarContent;
import com.backend.spring.payload.request.GrammarContentRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IGrammarContentService {
    void uploadGrammarContentFromExcel(MultipartFile file) throws IOException;
    GrammarContent createGrammarContent(GrammarContentRequest grammarContentRequest);
    GrammarContent updateGrammarContent(GrammarContentRequest grammarContentRequest);
    GrammarContent updateGrammarContentStatus(Integer grammarContentId, Integer newStatus);
    List<GrammarContent> getAllGrammarContents();
    GrammarContent getGrammarContentById(Integer id);
    boolean deleteGrammarContent(Integer id);
    List<GrammarContent> getGrammarContentsByGrammarId(Integer grammarId);
}
