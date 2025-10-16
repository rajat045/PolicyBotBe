package in.ranjitha.controller;

import in.ranjitha.model.DocumentMetadata;
import in.ranjitha.service.RagService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final RagService ragService;

    public DocumentController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public DocumentMetadata uploadDocument(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String content = extractText(file);
        return ragService.ingestDocument(file.getOriginalFilename(), content);
    }

    /* Extracts text based on file type. */
    private String extractText(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename().toLowerCase();

        if (filename.endsWith(".pdf")) {
            return extractTextFromPdf(file);
        } else if (filename.endsWith(".docx")) {
            return extractTextFromDocx(file);
        } else if (filename.endsWith(".doc")) {
            return extractTextFromDoc(file);
        } else if (filename.endsWith(".txt")) {
            return new String(file.getBytes());
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + filename);
        }
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractTextFromDocx(MultipartFile file) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(file.getInputStream());
             XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {
            return extractor.getText();
        }
    }

    private String extractTextFromDoc(MultipartFile file) throws IOException {
        try (HWPFDocument doc = new HWPFDocument(file.getInputStream());
             WordExtractor extractor = new WordExtractor(doc)) {
            return extractor.getText();
        }
    }
}


