package kravchenko.danylo.plagiarism.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class PlagiarismReview {

    private List<PlagiarismTextHighlight> highlights;

    private double plagiarismLevel;

    public PlagiarismReview() {
        highlights = new ArrayList<>();
        plagiarismLevel = 0;
    }

    public void addHighlight(final int startIndexA, final int endIndexA,
                             final int startIndexB, final int endIndexB, final double plagiarismLevel) {
        highlights.add(new PlagiarismTextHighlight(startIndexA, endIndexA, startIndexB, endIndexB, plagiarismLevel));
    }


    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    private static class PlagiarismTextHighlight {
        private PlagiarismHighlight textA;
        private PlagiarismHighlight textB;

        private double plagiarismLevel;

        public PlagiarismTextHighlight(final int startIndexA, final int endIndexA,
                                       final int startIndexB, final int endIndexB, final double plagiarismLevel) {
            textA = new PlagiarismHighlight(startIndexA, endIndexA);
            textB = new PlagiarismHighlight(startIndexB, endIndexB);
            this.plagiarismLevel = plagiarismLevel;
        }
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    private static class PlagiarismHighlight {
        private int startIndex;
        private int endIndex;
    }
}

