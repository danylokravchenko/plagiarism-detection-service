package kravchenko.danylo.plagiarism.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Value
@ToString
@Getter
@Setter
public class PlagiarismRequestItem {
    @JsonProperty("text_a")
    private String textA;
    @JsonProperty("text_b")
    private String textB;
}
