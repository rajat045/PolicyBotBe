package in.ranjitha.controller;

import in.ranjitha.model.AnswerResponse;
import in.ranjitha.model.QuestionRequest;
import in.ranjitha.service.RagService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FaqController {

    private final RagService ragService;

    public FaqController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ask")
    public ResponseEntity<AnswerResponse> answerQuestion(@Valid @RequestBody QuestionRequest questionRequest) {
        return ResponseEntity.ok(ragService.answerQuestion(questionRequest));
    }
}


