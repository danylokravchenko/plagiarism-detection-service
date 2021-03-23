package kravchenko.danylo.plagiarism.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlagiarismResponseItem {
    @JsonProperty("plagiarism")
    private boolean isPlagiarism;
    private float accuracy;
    @JsonProperty("text_a")
    private String textA;
    @JsonProperty("text_b")
    private String textB;
}
