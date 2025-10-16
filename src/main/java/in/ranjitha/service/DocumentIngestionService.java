package in.ranjitha.service;

import in.ranjitha.model.DocumentMetadata;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentIngestionService {
    // Store uploaded document text in memory
    private final List<String> inMemoryDocs = new ArrayList<>();

    // Uploads PDF extracts text
    public DocumentMetadata ingestDocument(MultipartFile file) {
        try (PDDocument pdf = PDDocument.load(file.getInputStream())) {
            // Extract text
            String text = new PDFTextStripper().getText(pdf);

            // Store text in memory
            inMemoryDocs.add(text);

            return new DocumentMetadata(file.getOriginalFilename(), "Loaded into memory");

        } catch (IOException e) {
            throw new RuntimeException("Error reading PDF: " + e.getMessage());
        }
    }

    // Retrieve all stored documents
    public List<String> getAllDocuments() {
        return new ArrayList<>(inMemoryDocs);
    }

}