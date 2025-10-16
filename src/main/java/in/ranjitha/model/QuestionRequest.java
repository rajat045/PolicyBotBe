package in.ranjitha.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionRequest {
    @NotBlank
    private String question;

}
