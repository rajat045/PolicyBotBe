package in.ranjitha.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnswerResponse {
    private String question;
    private List<String> answer;
}
